package ru.extas.web.commons.container;

import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.SortableLazyList;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Контейненер для получения данных из базы через JPA
 *
 * @author Valery Orlov
 *         Date: 08.04.2015
 *         Time: 18:58
 */
public class JpaLazyListContainer<TEntityType extends IdentifiedObject> extends ListContainer<TEntityType> {

    private static final int CONTAINER_PAGE_SIZE = 50;
    private final Class<TEntityType> entityClass;
    private final List<String> nestedProps = newArrayList();

    public JpaLazyListContainer(Class<TEntityType> type) {
        super(type);
        this.entityClass = type;
        setCollection(new SortableLazyList<TEntityType>(new LazyEntityProvider(), CONTAINER_PAGE_SIZE));
    }

    public void addNestedContainerProperty(String nestedProp) {
        nestedProps.add(nestedProp);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        final Collection<String> propertyIds = super.getContainerPropertyIds();
        propertyIds.addAll(nestedProps);
        return propertyIds;
    }

    private class LazyEntityProvider implements SortableLazyList.SortableEntityProvider {

        @Override
        public int size() {
            final EntityManager em = lookup(EntityManager.class);
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<Long> query = cb.createQuery(Long.class);
            final Root<TEntityType> root = query.from(entityClass);

            query.select(cb.countDistinct(root.get(IdentifiedObject_.id)));

            final TypedQuery<Long> tq = em.createQuery(query);
            return tq.getSingleResult().intValue();
        }

        @Override
        public List findEntities(int firstRow, boolean sortAscending, String property) {
            final EntityManager em = lookup(EntityManager.class);
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<TEntityType> query = cb.createQuery(entityClass);
            final Root<TEntityType> root = query.from(entityClass);

            query.select(root);
            if(property != null)
                query.orderBy(sortAscending ? cb.asc(root.get(property)) : cb.asc(root.get(property)));

            final TypedQuery<TEntityType> tq = em.createQuery(query);
            tq.setFirstResult(firstRow);
            tq.setMaxResults(CONTAINER_PAGE_SIZE);
            return tq.getResultList();
        }
    }
}