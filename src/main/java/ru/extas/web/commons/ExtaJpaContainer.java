package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.common.ArchivedObject;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.server.SpringEntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
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
    }

    @Override
    public boolean isArchiveExcluded() {
        return archiveExcluded;
    }

    @Override
    public void setArchiveExcluded(boolean archiveExcluded) {
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

    }
}
