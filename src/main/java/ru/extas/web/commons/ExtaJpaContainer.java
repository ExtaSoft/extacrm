package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.SortBy;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.util.filter.Compare;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import ru.extas.model.common.ArchivedObject;
import ru.extas.model.common.AuditedObject;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.server.SpringEntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>ExtaDataContainer class.</p>
 *
 * @author Valery Orlov
 *         Date: 02.04.2014
 *         Time: 18:11
 * @version $Id: $Id
 * @since 0.3.0
 */
public class ExtaJpaContainer<TEntityType extends IdentifiedObject> extends JPAContainer<TEntityType> implements ArchivedContainer {

    private static final long serialVersionUID = -7891940552175752858L;

    private final Compare.Equal archiveFilter = new Compare.Equal(ArchivedObject.PROPERTY_NAME, false);
    private boolean archiveExcluded;

    /**
     * <p>Constructor for ExtaDataContainer.</p>
     *
     * @param entityClass a {@link java.lang.Class} object.
     */
    public ExtaJpaContainer(final Class<TEntityType> entityClass) {
        super(entityClass);
        // We need an entity provider to create a container
        setEntityProvider(new ExtaLocalEntityProvider<>(entityClass));

        // Отсекаем архивные записи
        setArchiveExcluded(true);

        // По умолчанию последние изменившиеся записи вверху
        if (AuditedObject.class.isAssignableFrom(entityClass))
            sort(new Object[]{"lastModifiedDate"}, new boolean[]{false});

    }

    @Override
    public boolean isArchiveExcluded() {
        return archiveExcluded;
    }

    @Override
    public void setArchiveExcluded(final boolean archiveExcluded) {
        if (this.archiveExcluded != archiveExcluded && ArchivedObject.class.isAssignableFrom(getEntityClass())) {
            this.archiveExcluded = archiveExcluded;
            if (archiveExcluded)
                addContainerFilter(archiveFilter);
            else
                removeContainerFilter(archiveFilter);
        }
    }

    public List<TEntityType> getEntitiesList() {
        return newArrayList(getItemIds().stream().map(id -> getItem(id).getEntity()).iterator());
    }

    public Set<TEntityType> getEntitiesSet() {
        return newHashSet(getItemIds().stream().map(id -> getItem(id).getEntity()).iterator());
    }

    @Override
    public int indexOfId(Object itemId) {
        return doGetEntityProvider().getIndexOfId(this,
                itemId, getAppliedFiltersAsConjunction(), getSortByList());
    }

    @Override
    protected ExtaLocalEntityProvider<TEntityType> doGetEntityProvider() throws IllegalStateException {
        return (ExtaLocalEntityProvider<TEntityType>) super.doGetEntityProvider();
    }

    protected static class InjectEntityManagerProvider implements EntityManagerProvider, Serializable {
        @Override
        public EntityManager getEntityManager() {
            return lookup(SpringEntityManagerProvider.class).getEntityManager();
        }
    }

    private static class ExtaLocalEntityProvider<TEntityType extends IdentifiedObject> extends MutableLocalEntityProvider<TEntityType> {

        public ExtaLocalEntityProvider(final Class<TEntityType> entityClass) {
            super(entityClass);
            setTransactionsHandledByProvider(false);
            setEntitiesDetached(false);

            setEntityManagerProvider(new InjectEntityManagerProvider());
        }

        @Override
        public TEntityType refreshEntity(final TEntityType entity) {
            final Object[] res = new Object[1];
            runInTransaction(() -> res[0] = super.refreshEntity(entity));
            return (TEntityType) res[0];
        }

        @Override
        protected void runInTransaction(final Runnable operation) {
            lookup(SpringEntityManagerProvider.class)
                    .runInTransaction(() -> super.runInTransaction(operation));
        }

        @Override
        protected int doGetEntityCount(final EntityContainer<TEntityType> container, final Filter filter) {
            final String entityIdPropertyName = getEntityClassMetadata()
                    .getIdentifierProperty().getName();

            final CriteriaBuilder cb = doGetEntityManager().getCriteriaBuilder();
            final CriteriaQuery<Long> query = cb.createQuery(Long.class);
            final Root<TEntityType> root = query.from(getEntityClassMetadata().getMappedClass());

            tellDelegateQueryWillBeBuilt(container, cb, query);

            final List<Predicate> predicates = new ArrayList<Predicate>();
            if (filter != null) {
                predicates.add(FilterConverter.convertFilter(filter, cb, root));
            }
            tellDelegateFiltersWillBeAdded(container, cb, query, predicates);
            if (!predicates.isEmpty()) {
                query.where(CollectionUtil.toArray(Predicate.class, predicates));
            }
            tellDelegateFiltersWereAdded(container, cb, query);

            if (getEntityClassMetadata().hasEmbeddedIdentifier()) {
                /*
                 * Hibernate will generate SQL for "count(obj)" that does not run on
                 * HSQLDB. "count(*)" works fine, but then EclipseLink won't work.
                 * With this hack, this method should work with both Hibernate and
                 * EclipseLink.
                 */

                query.select(cb.countDistinct(root.get(entityIdPropertyName).get(
                        getEntityClassMetadata().getIdentifierProperty()
                                .getTypeMetadata().getPersistentPropertyNames()
                                .iterator().next())));
            } else {
                query.select(cb.countDistinct(root.get(entityIdPropertyName)));
            }
            tellDelegateQueryHasBeenBuilt(container, cb, query);
            final TypedQuery<Long> tq = doGetEntityManager().createQuery(query);
            return tq.getSingleResult().intValue();
        }
        // QueryModifierDelegate helper methods

        private void tellDelegateQueryWillBeBuilt(final EntityContainer<TEntityType> container,
                                                  final CriteriaBuilder cb, final CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().queryWillBeBuilt(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().queryWillBeBuilt(cb, query);
            }
        }

        private void tellDelegateQueryHasBeenBuilt(final EntityContainer<TEntityType> container,
                                                   final CriteriaBuilder cb, final CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().queryHasBeenBuilt(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().queryHasBeenBuilt(cb, query);
            }
        }

        private void tellDelegateFiltersWillBeAdded(final EntityContainer<TEntityType> container,
                                                    final CriteriaBuilder cb, final CriteriaQuery<?> query,
                                                    final List<Predicate> predicates) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().filtersWillBeAdded(cb, query, predicates);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().filtersWillBeAdded(cb, query,
                        predicates);
            }
        }

        private void tellDelegateFiltersWereAdded(final EntityContainer<TEntityType> container,
                                                  final CriteriaBuilder cb, final CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().filtersWereAdded(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().filtersWereAdded(cb, query);
            }
        }

        public int getIndexOfId(ExtaJpaContainer<TEntityType> container, Object itemId, Filter filter, List<SortBy> sortBy) {
            if (sortBy == null)
                sortBy = Collections.emptyList();

            final String entityIdPropertyName = getEntityClassMetadata()
                    .getIdentifierProperty().getName();

            final EntityManager em = doGetEntityManager();
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery query = cb.createQuery();
            final Root<TEntityType> root = query.from(getEntityClassMetadata().getMappedClass());

            tellDelegateQueryWillBeBuilt(container, cb, query);

            final List<Predicate> predicates = new ArrayList<Predicate>();
            if (filter != null) {
                predicates.add(FilterConverter.convertFilter(filter, cb, root));
            }
            tellDelegateFiltersWillBeAdded(container, cb, query, predicates);
            if (!predicates.isEmpty()) {
                query.where(CollectionUtil.toArray(Predicate.class, predicates));
            }
            tellDelegateFiltersWereAdded(container, cb, query);

            List<Order> orderBy = newArrayList();
            if (sortBy != null && sortBy.size() > 0) {
                for (SortBy sortedProperty : sortBy) {
                    orderBy.add(translateSortBy(sortedProperty, false, cb,
                            root));
                }
            }
            tellDelegateOrderByWillBeAdded(container, cb, query, orderBy);
            query.orderBy(orderBy);
            tellDelegateOrderByWereAdded(container, cb, query);

            final Path<Object> idPath = root.get(entityIdPropertyName);
            query.select(idPath).distinct(true);

            tellDelegateQueryHasBeenBuilt(container, cb, query);
            final TypedQuery<Long> tq = em.createQuery(query);
            final DatabaseQuery databaseQuery = tq.unwrap(JpaQuery.class).getDatabaseQuery();
            final DatabaseRecord translationRow = new DatabaseRecord();
            final JpaEntityManager jpaEm = em.unwrap(JpaEntityManager.class);
            databaseQuery.prepareCall(jpaEm.getSession(), translationRow);
            String querySql = databaseQuery.getTranslatedSQLString(jpaEm.getSession(), translationRow);
            Query nativeQuery = em.createNativeQuery(
                    MessageFormat.format(
                            "SELECT q.rownum AS indx " +
                                    "FROM (SELECT s.{2} AS id, @rownum:=@rownum + 1 AS rownum " +
                                    "           FROM (SELECT @rownum:=0) i, ({0}) s) q " +
                                    "WHERE q.id = ''{1}''", querySql, itemId, entityIdPropertyName));
//            nativeQuery.unwrap(JpaQuery.class).getDatabaseQuery().setTranslationRow(translationRow);
            Object result = nativeQuery.getSingleResult();

            return result != null ? (int) (double) result : -1;
        }

        private void tellDelegateOrderByWillBeAdded(EntityContainer<TEntityType> container,
                                                    CriteriaBuilder cb, CriteriaQuery<?> query, List<Order> orderBy) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().orderByWillBeAdded(cb, query, orderBy);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().orderByWillBeAdded(cb, query,
                        orderBy);
            }
        }

        private void tellDelegateOrderByWereAdded(EntityContainer<TEntityType> container,
                                                  CriteriaBuilder cb, CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().orderByWasAdded(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().orderByWasAdded(cb, query);
            }
        }
    }
}
