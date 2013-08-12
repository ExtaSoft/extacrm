package ru.extas.vaadin.addon.jdocontainer;

import com.vaadin.data.util.BeanItem;

/**
 * JdoContainer enables loading JPA entities in non lazy manner in single read operation
 * using same query backend as LCQ.
 * 
 * @param <E>
 *            Jdo class.
 * @author Valery Orlov
 */
public final class JdoContainer<E> extends LazyQueryContainer {
	/**
	 * Java serialization version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 * 
	 * @param pm
	 *            The JPA JdoManager.
	 * @param entityClass
	 *            The entity class.
	 * @param idPropertyId
	 *            The ID of the ID property or null if item index is used as ID.
	 * @param maximumQueryResultSize
	 *            The maximum size of the query result.
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext.
	 */
	public JdoContainer(final Class<E> entityClass,
						final Object idPropertyId,
						final int maximumQueryResultSize) {
		super(new JdoQueryDefinition<E>(entityClass, maximumQueryResultSize, idPropertyId),
				new JdoQueryFactory());
		getQueryView().getQueryDefinition().setMaxQuerySize(maximumQueryResultSize);
	}

	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 * 
	 * @param entityClass
	 *            The entity class.
	 * @param maximumQueryResultSize
	 *            Maximum number of items in the container.
	 * @param nativeSortPropertyIds
	 *            Properties participating in the native sort.
	 * @param nativeSortPropertyAscendingStates
	 *            List of property sort directions
	 *            for the native sort.
	 * @param idPropertyId
	 *            Property containing the property ID.
	 */
	public JdoContainer(final Class<E> entityClass, final int maximumQueryResultSize,
						final Object[] nativeSortPropertyIds, final boolean[]
						nativeSortPropertyAscendingStates,
						final Object idPropertyId) {
		super(new JdoQueryDefinition<E>(entityClass, maximumQueryResultSize, idPropertyId),
				new JdoQueryFactory());
		getQueryView().getQueryDefinition().setMaxQuerySize(maximumQueryResultSize);
		getQueryView().getQueryDefinition().setDefaultSortState(nativeSortPropertyIds,
				nativeSortPropertyAscendingStates);
	}

	/**
	 * Adds entity to the container as first item i.e. at index 0.
	 * 
	 * @return the new constructed entity.
	 */
	public E addJdoEntity() {
		final Object itemId = addItem();
		return getJdoEntity(indexOfId(itemId));
	}

	/**
	 * Removes given entity at given index and returns it.
	 * 
	 * @param index
	 *            Index of the entity to be removed.
	 * @return The removed entity.
	 */
	public E removeJdoEntity(final int index) {
		final E entityToRemove = getJdoEntity(index);
		removeItem(getIdByIndex(index));
		return entityToRemove;
	}

	/**
	 * Gets entity by ID.
	 * 
	 * @param id
	 *            The ID of the entity.
	 * @return the entity.
	 */
	public E getJdoEntity(final Object id) {
		return getJdoEntity(indexOfId(id));
	}

	/**
	 * Gets entity at given index.
	 * 
	 * @param index
	 *            The index of the entity.
	 * @return the entity.
	 */
	@SuppressWarnings("unchecked")
	public E getJdoEntity(final int index) {
		return ((BeanItem<E>)getItem(getIdByIndex(index))).getBean();
	}
}
