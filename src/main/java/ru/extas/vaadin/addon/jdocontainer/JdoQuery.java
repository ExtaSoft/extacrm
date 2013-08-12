package ru.extas.vaadin.addon.jdocontainer;

import static com.google.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import org.apache.commons.lang3.text.WordUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;

/**
 * Jdo query implementation which dynamically injects missing query
 * definition properties to CompositeItems.
 * 
 * @param <E>
 *            the entity type
 * 
 * @author Valery Orlov
 */
public class JdoQuery<E> implements Query, Serializable {
	private static final String PRM_PREFIX = "qprm";
	/**
	 * Java serialization version UID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The JDO entity class.
	 */
	private final Class<E> entityClass;
	/**
	 * QueryDefinition contains definition of the query properties and batch
	 * size.
	 */
	private final JdoQueryDefinition<E> queryDefinition;
	/**
	 * The size of the query.
	 */
	private int querySize = -1;

	/**
	 * Constructor for configuring the query.
	 * 
	 * @param entityQueryDefinition
	 *            The entity query definition.
	 * @param pm
	 *            The entity manager.
	 */
	public JdoQuery(final JdoQueryDefinition<E> entityQueryDefinition) {
		this.queryDefinition = entityQueryDefinition;
		this.entityClass = entityQueryDefinition.getJdoClass();
	}

	/**
	 * Constructs new item based on QueryDefinition.
	 * 
	 * @return new item.
	 */
	@Override
	public final Item constructItem() {
		try {
			final E entity = entityClass.newInstance();
			return toItem(entity);
		} catch (final Exception e) {
			throw new RuntimeException("Error in bean construction or property population with default values.", e);
		}
	}

	/**
	 * Number of beans returned by query.
	 * 
	 * @return number of beans.
	 */
	@Override
	public final int size() {

		if (querySize == -1) {
			final PersistenceManager pm = lookup(PersistenceManager.class);
			final javax.jdo.Query query = pm.newQuery(entityClass);
			try {
				final Map<String, Object> parameters = newHashMap();
				setWhereCriteria(query, parameters);
				QueryUtils.declareParameters(query, parameters);

				query.setResult("count(this)");
				final long count = (long)query.executeWithMap(parameters);

				querySize = (int)count;
			} finally {
				query.closeAll();
				pm.close();
			}
		}
		return querySize;
	}

	/**
	 * @param query
	 */
	private void setWhereCriteria(final javax.jdo.Query query, final Map<String, Object> parameters) {
		final List<Container.Filter> filters = new ArrayList<Container.Filter>();
		filters.addAll(queryDefinition.getDefaultFilters());
		filters.addAll(queryDefinition.getFilters());

		Container.Filter rootFilter;
		if (filters.size() > 0) {
			rootFilter = filters.remove(0);
		} else {
			rootFilter = null;
		}
		while (filters.size() > 0) {
			final Container.Filter filter = filters.remove(0);
			rootFilter = new And(rootFilter, filter);
		}

		if (rootFilter != null) {

			query.setFilter(getFilter(query, rootFilter, parameters).toString());
		}
	}

	/**
	 * @param query
	 * @param rootFilter
	 * @param parameters
	 * @return
	 */
	private StringBuilder getFilter(final javax.jdo.Query query, final Filter filter,
			final Map<String, Object> parameters) {

		final StringBuilder strFilter = new StringBuilder(128);
		if (filter instanceof And) {
			final And and = (And)filter;
			final List<Container.Filter> filters = new ArrayList<Container.Filter>(and.getFilters());

			strFilter.append("(")
					.append(getFilter(query, filters.remove(0), parameters))
					.append(")");
			while (filters.size() > 0) {
				strFilter.append(" && ");
				strFilter.append("(")
						.append(getFilter(query, filters.remove(0), parameters))
						.append(")");
			}

			return strFilter;
		}

		if (filter instanceof Or) {
			final Or or = (Or)filter;
			final List<Container.Filter> filters = new ArrayList<Container.Filter>(or.getFilters());

			strFilter.append("(")
					.append(getFilter(query, filters.remove(0), parameters))
					.append(")");
			while (!filters.isEmpty()) {
				strFilter.append(" || ")
						.append("(")
						.append(getFilter(query, filters.remove(0), parameters))
						.append(")");
			}

			return strFilter;
		}

		if (filter instanceof Not) {
			final Not not = (Not)filter;
			strFilter.append("!(")
					.append(getFilter(query, not.getFilter(), parameters))
					.append(")");
			return strFilter;
		}

		if (filter instanceof Between) {
			final Between between = (Between)filter;
			strFilter.append(between.getPropertyId()).append(" >= ").append(getPrmName(parameters));
			parameters.put(getPrmName(parameters), between.getStartValue());
			strFilter.append(" && ");
			strFilter.append(between.getPropertyId()).append(" <= ").append(getPrmName(parameters));
			parameters.put(getPrmName(parameters), between.getEndValue());
			return strFilter;
		}

		if (filter instanceof Compare) {
			final Compare compare = (Compare)filter;
			strFilter.append(compare.getPropertyId());
			switch (compare.getOperation()) {
			case EQUAL:
				strFilter.append(" == ");
				break;
			case GREATER:
				strFilter.append(" > ");
				break;
			case GREATER_OR_EQUAL:
				strFilter.append(" >= ");
				break;
			case LESS:
				strFilter.append(" < ");
				break;
			case LESS_OR_EQUAL:
				strFilter.append(" <= ");
				break;
			default:
			}
			strFilter.append(getPrmName(parameters));
			parameters.put(getPrmName(parameters), compare.getValue());
			return strFilter;
		}

		if (filter instanceof IsNull) {
			final IsNull isNull = (IsNull)filter;
			strFilter
					.append(isNull.getPropertyId())
					.append(" == null");
			return strFilter;
		}

		if (filter instanceof Like) {
			final Like like = (Like)filter;
			final Object property = like.getPropertyId();
			final String strCriteria = like.getValue();
			final boolean isIgnoreCase = !like.isCaseSensitive();
			return getMatchesFilter(parameters, property, strCriteria, isIgnoreCase);
		}

		if (filter instanceof SimpleStringFilter) {
			final SimpleStringFilter ssFilter = (SimpleStringFilter)filter;
			final Object property = ssFilter.getPropertyId();
			String strCriteria = ssFilter.getFilterString();
			final boolean isIgnoreCase = ssFilter.isIgnoreCase();
			if (ssFilter.isOnlyMatchPrefix()) {
				strFilter.append(property);
				strFilter.append(".startsWith(").append(getPrmName(parameters)).append(")");
				parameters.put(getPrmName(parameters), WordUtils.capitalizeFully(strCriteria));
				return strFilter;
			} else {
				strCriteria = "%" + strCriteria + "%";
				return getMatchesFilter(parameters, property, strCriteria, isIgnoreCase);
			}
		}

		throw new UnsupportedOperationException("Vaadin filter: " + filter.getClass().getName() + " is not supported.");
	}

	/**
	 * @param parameters
	 * @param property
	 * @param strCriteria
	 * @param isIgnoreCase
	 * @return
	 */
	private StringBuilder getMatchesFilter(final Map<String, Object> parameters, final Object property,
			final String strCriteria,
			final boolean isIgnoreCase) {
		final StringBuilder likeFilter = new StringBuilder(128);
		likeFilter.append(property);
		likeFilter.append(".matches(");
		likeFilter.append(getPrmName(parameters)).append(")");
		parameters.put(getPrmName(parameters), QueryUtils.likeToRegex(strCriteria, isIgnoreCase));

		return likeFilter;
	}

	/**
	 * @param parameters
	 * @return
	 */
	private String getPrmName(final Map<String, Object> parameters) {
		return PRM_PREFIX + parameters.size();
	}

	/**
	 * Load batch of items.
	 * 
	 * @param startIndex
	 *            Starting index of the item list.
	 * @param count
	 *            Count of the items to be retrieved.
	 * @return List of items.
	 */
	@Override
	public final List<Item> loadItems(final int startIndex, final int count) {

		final PersistenceManager pm = lookup(PersistenceManager.class);
		final javax.jdo.Query query = pm.newQuery(entityClass);

		try {
			final Map<String, Object> parameters = newHashMap();
			setWhereCriteria(query, parameters);
			setOrderClause(query);
			QueryUtils.declareParameters(query, parameters);
			QueryUtils.setRange(query, startIndex, count);

			@SuppressWarnings("unchecked")
			final List<E> entities = (List<E>)query.executeWithMap(parameters);
			final List<Item> items = new ArrayList<Item>();
			for (final E entity : entities) {
				items.add(toItem(entity));
			}

			return items;
		} finally {
			query.closeAll();
			pm.close();
		}
	}

	/**
	 * @param query
	 */
	private void setOrderClause(final javax.jdo.Query query) {
		Object[] sortPropertyIds;
		boolean[] sortPropertyAscendingStates;

		if (queryDefinition.getSortPropertyIds().length == 0) {
			sortPropertyIds = queryDefinition.getDefaultSortPropertyIds();
			sortPropertyAscendingStates = queryDefinition.getDefaultSortPropertyAscendingStates();
		} else {
			sortPropertyIds = queryDefinition.getSortPropertyIds();
			sortPropertyAscendingStates = queryDefinition.getSortPropertyAscendingStates();
		}

		QueryUtils.setOrdering(query, sortPropertyIds, sortPropertyAscendingStates);
	}

	/**
	 * Saves the modifications done by container to the query result. Query will
	 * be discarded after changes have been saved and new query loaded so that
	 * changed items are sorted appropriately.
	 * 
	 * @param addedItems
	 *            Items to be inserted.
	 * @param modifiedItems
	 *            Items to be updated.
	 * @param removedItems
	 *            Items to be deleted.
	 */
	@Override
	public final void saveItems(final List<Item> addedItems, final List<Item> modifiedItems,
			final List<Item> removedItems) {
// if (applicationTransactionManagement) {
// persistenceManager.getTransaction().begin();
// }
// try {
// for (final Item item : addedItems) {
// if (!removedItems.contains(item)) {
// persistenceManager.persist(fromItem(item));
// }
// }
// for (final Item item : modifiedItems) {
// if (!removedItems.contains(item)) {
// Object entity = fromItem(item);
// if (queryDefinition.isDetachedEntities()) {
// entity = persistenceManager.merge(entity);
// }
// persistenceManager.persist(entity);
// }
// }
// for (final Item item : removedItems) {
// if (!addedItems.contains(item)) {
// Object entity = fromItem(item);
// if (queryDefinition.isDetachedEntities()) {
// entity = persistenceManager.merge(entity);
// }
// persistenceManager.remove(entity);
// }
// }
// if (applicationTransactionManagement) {
// persistenceManager.getTransaction().commit();
// }
// } catch (final Exception e) {
// if (applicationTransactionManagement) {
// if (persistenceManager.getTransaction().isActive()) {
// persistenceManager.getTransaction().rollback();
// }
// }
// throw new RuntimeException(e);
// }
		// throw new UnsupportedOperationException("COntainer in readonly mode.");

	}

	/**
	 * Removes all items. Query will be discarded after delete all items has
	 * been called.
	 * 
	 * @return true if the operation succeeded or false in case of a failure.
	 */
	@Override
	public final boolean deleteAllItems() {
// if (applicationTransactionManagement) {
// persistenceManager.getTransaction().begin();
// }
// try {
// final CriteriaBuilder cb = persistenceManager.getCriteriaBuilder();
// final CriteriaQuery<E> cq = cb.createQuery(entityClass);
// final Root<E> root = cq.from(entityClass);
//
// cq.select(root);
//
// setWhereCriteria(cb, cq, root);
//
// setOrderClause(cb, cq, root);
//
// final javax.persistence.TypedQuery<E> query = persistenceManager.createQuery(cq);
//
// final List<?> entities = query.getResultList();
// for (final Object entity : entities) {
// persistenceManager.remove(entity);
// }
//
// if (applicationTransactionManagement) {
// persistenceManager.getTransaction().commit();
// }
// } catch (final Exception e) {
// if (applicationTransactionManagement) {
// if (persistenceManager.getTransaction().isActive()) {
// persistenceManager.getTransaction().rollback();
// }
// }
// throw new RuntimeException(e);
// }
// return true;
		throw new UnsupportedOperationException("COntainer in readonly mode.");
	}

	/**
	 * Converts bean to Item. Implemented by encapsulating the Bean first to
	 * BeanItem and then to CompositeItem.
	 * 
	 * @param entity
	 *            bean to be converted.
	 * @return item converted from bean.
	 */
	protected final Item toItem(final E entity) {
		final NestingBeanItem<E> beanItem = new NestingBeanItem<E>(entity, 1, queryDefinition.getPropertyIds());
		return beanItem;
	}

	/**
	 * Converts item back to bean.
	 * 
	 * @param item
	 *            Item to be converted to bean.
	 * @return Resulting bean.
	 */
	protected final Object fromItem(final Item item) {
		return ((BeanItem<?>)item).getBean();
	}

	/**
	 * @return the queryDefinition
	 */
	protected final JdoQueryDefinition<E> getQueryDefinition() {
		return queryDefinition;
	}

}
