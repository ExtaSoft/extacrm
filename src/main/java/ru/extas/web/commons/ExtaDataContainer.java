package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
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

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 02.04.2014
 *         Time: 18:11
 */
public class ExtaDataContainer<TEntityType extends IdentifiedObject> extends JPAContainer<TEntityType> {

    private static final long serialVersionUID = -7891940552175752858L;

    public ExtaDataContainer(Class<TEntityType> entityClass) {
        super(entityClass);
        // We need an entity provider to create a container
        CachingLocalEntityProvider<TEntityType> entityProvider =
                new ExtaLocalEntityProvider<>(entityClass);

        //entityProvider.setCacheEnabled(false);
        entityProvider.setEntitiesDetached(false);

        entityProvider.setEntityManagerProvider(new InjectEntityManagerProvider());

        setEntityProvider(entityProvider);
    }


    protected static class InjectEntityManagerProvider implements EntityManagerProvider, Serializable {
        @Override
        public EntityManager getEntityManager() {
            return lookup(SpringEntityManagerProvider.class).getEntityManager();
        }
    }

    private static class ExtaLocalEntityProvider<TEntityType extends IdentifiedObject> extends CachingLocalEntityProvider<TEntityType> {
        public ExtaLocalEntityProvider(Class<TEntityType> entityClass) {
            super(entityClass);
        }

        @Override
        protected int doGetEntityCount(EntityContainer<TEntityType> container, Filter filter) {
            String entityIdPropertyName = getEntityClassMetadata()
                    .getIdentifierProperty().getName();

            CriteriaBuilder cb = doGetEntityManager().getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<TEntityType> root = query.from(getEntityClassMetadata().getMappedClass());

            tellDelegateQueryWillBeBuilt(container, cb, query);

            List<Predicate> predicates = new ArrayList<Predicate>();
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
            TypedQuery<Long> tq = doGetEntityManager().createQuery(query);
            return tq.getSingleResult().intValue();
        }
        // QueryModifierDelegate helper methods

        private void tellDelegateQueryWillBeBuilt(EntityContainer<TEntityType> container,
                                                  CriteriaBuilder cb, CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().queryWillBeBuilt(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().queryWillBeBuilt(cb, query);
            }
        }

        private void tellDelegateQueryHasBeenBuilt(EntityContainer<TEntityType> container,
                                                   CriteriaBuilder cb, CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().queryHasBeenBuilt(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().queryHasBeenBuilt(cb, query);
            }
        }

        private void tellDelegateFiltersWillBeAdded(EntityContainer<TEntityType> container,
                                                    CriteriaBuilder cb, CriteriaQuery<?> query,
                                                    List<Predicate> predicates) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().filtersWillBeAdded(cb, query, predicates);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().filtersWillBeAdded(cb, query,
                        predicates);
            }
        }

        private void tellDelegateFiltersWereAdded(EntityContainer<TEntityType> container,
                                                  CriteriaBuilder cb, CriteriaQuery<?> query) {
            if (getQueryModifierDelegate() != null) {
                getQueryModifierDelegate().filtersWereAdded(cb, query);
            } else if (container.getQueryModifierDelegate() != null) {
                container.getQueryModifierDelegate().filtersWereAdded(cb, query);
            }
        }

    }
}
