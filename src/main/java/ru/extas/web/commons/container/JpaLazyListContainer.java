package ru.extas.web.commons.container;

import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.metadata.EntityClassMetadata;
import com.vaadin.addon.jpacontainer.metadata.MetadataFactory;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.support.TransactionTemplate;
import org.vaadin.viritin.LazyList;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.collect.Lists.newLinkedList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Контейненер для получения данных из базы через JPA
 *
 * @author Valery Orlov
 *         Date: 08.04.2015
 *         Time: 18:58
 */
public class JpaLazyListContainer<TEntityType extends IdentifiedObject> extends NListContainer<TEntityType> implements Container.Filterable {

    private static final int CONTAINER_PAGE_SIZE = LazyList.DEFAULT_PAGE_SIZE;
    private final Class<TEntityType> entityClass;
    private final LazyList<TEntityType> entityList;
    private final AdvancedFilterableSupport filterSupport;
    private List<Pair<String, Boolean>> sortByList = newLinkedList();
    private final EntityClassMetadata<TEntityType> entityClassMetadata;


    public JpaLazyListContainer(final Class<TEntityType> type) {
        super(type);
        this.entityClass = type;
        this.entityClassMetadata = MetadataFactory.getInstance().getEntityClassMetadata(entityClass);
        entityList = new LazyList<TEntityType>(new LazyEntityProvider(), CONTAINER_PAGE_SIZE);
        setCollection(entityList);
        this.filterSupport = new AdvancedFilterableSupport();
        this.filterSupport.addListener(e -> refresh());

        updateFilterablePropertyIds();
    }

    protected void updateFilterablePropertyIds() {
        this.filterSupport.setFilterablePropertyIds(getContainerPropertyIds());
    }

    @Override
    public void addNestedContainerProperty(final String nestedProp) {
        super.addNestedContainerProperty(nestedProp);
        updateFilterablePropertyIds();
    }

    public void refresh() {
        entityList.reset();
        fireItemSetChange();
    }

    public Class<TEntityType> getEntityClass() {
        return entityClass;
    }

    public TEntityType refreshItem(final Object itemId) {
        final TEntityType entity = (TEntityType) itemId;
        // Refresh from DB
        final EntityManager em = lookup(EntityManager.class);
        final TEntityType freshEntity =
                lookup(TransactionTemplate.class).execute(
                        status -> {
                            final TEntityType e = em.find(entityClass, entity.getId());
                            if (e != null)
                                em.refresh(e);
                            return e;
                        });
        // Сущность была удалена
        if (freshEntity == null) {
            fireItemSetChange();
            return freshEntity;
        }
//        Collection<String> itemPropertyIds = getItemPropertyIds();
//        for (String string : itemPropertyIds) {
//            getItemProperty(string).fireValueChangeEvent();
//        }
        return freshEntity;
    }

    @Override
    public void sort(final Object[] propertyId, final boolean[] ascending) {
        checkNotNull(propertyId);
        checkNotNull(ascending);
        checkArgument(propertyId.length == ascending.length);
        // Grid in 7.4 may call this method with empty sorting instructions...
        if (propertyId.length > 0) {
            sortByList.clear();
            for (int i = 0; i < propertyId.length; i++) {
                sortByList.add(new ImmutablePair<>((String) propertyId[i], ascending[i]));
            }
            refresh();
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        // Думается, что все свойства могут участвовать в сортировке
        return getContainerPropertyIds();
    }

    @Override
    public void addContainerFilter(final Filter filter) throws UnsupportedFilterException {
        filterSupport.addFilter(filter);
    }

    @Override
    public void removeContainerFilter(final Filter filter) {
        filterSupport.removeFilter(filter);
    }

    @Override
    public void removeAllContainerFilters() {
        filterSupport.removeAllFilters();
    }

    @Override
    public Collection<Filter> getContainerFilters() {
        return filterSupport.getAppliedFilters();
    }

    public List<Filter> getAppliedFilters() {
        return filterSupport.getAppliedFilters();
    }

    protected Filter getAppliedFiltersAsConjunction() {
        if (getAppliedFilters().isEmpty()) {
            return null;
        } else if (getAppliedFilters().size() == 1) {
            return getAppliedFilters().iterator().next();
        } else {
            return new And(CollectionUtil.toArray(Filter.class,
                    getAppliedFilters()));
        }
    }

    private int listSize() {
        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<TEntityType> root = query.from(entityClass);

        query.select(cb.countDistinct(root.get(IdentifiedObject_.id)));
        // Добавляем фильтр контейнера
        final List<Predicate> predicates = createContainerFilterPredicates(cb, query, root);
        if (!predicates.isEmpty()) {
            query.where(CollectionUtil.toArray(Predicate.class, predicates));
        }

        final TypedQuery<Long> tq = em.createQuery(query);
        return tq.getSingleResult().intValue();
    }


    public List findListEntities(final int firstRow) {
        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<TEntityType> query = cb.createQuery(entityClass);
        final Root<TEntityType> root = query.from(entityClass);

        query.select(root);

        // Добавляем фильтр контейнера
        final List<Predicate> predicates = createContainerFilterPredicates(cb, query, root);
        if (!predicates.isEmpty()) {
            query.where(CollectionUtil.toArray(Predicate.class, predicates));
        }

        // Добавляем сортировку
        if (!sortByList.isEmpty()) {
            final List<Order> orders = newArrayListWithCapacity(sortByList.size());
            for (final Pair<String, Boolean> sortBy : sortByList)
                orders.add(translateSortBy(sortBy, cb, root));
            query.orderBy(orders);
        }

        final TypedQuery<TEntityType> tq = em.createQuery(query);
        tq.setFirstResult(firstRow);
        tq.setMaxResults(CONTAINER_PAGE_SIZE);
        return tq.getResultList();
    }

    protected List<Predicate> createContainerFilterPredicates(final CriteriaBuilder cb, final CriteriaQuery<?> query, final Root<TEntityType> root) {
        final Filter filter = getAppliedFiltersAsConjunction();
        final List<Predicate> predicates = newLinkedList();
        if (filter != null) {
            predicates.add(FilterConverter.convertFilter(filter, cb, root));
        }
        return predicates;
    }

    private Order translateSortBy(final Pair<String, Boolean> sortBy, final CriteriaBuilder cb, final Root<TEntityType> root) {
        final String sortedPropId = sortBy.getLeft();
        // First split the id and build a Path.
        final String[] idStrings = sortedPropId.split("\\.");
        Path<TEntityType> path = null;
        if (idStrings.length > 1 && !isEmbedded(idStrings[0])) {
            // This is a nested property, we need to LEFT JOIN
            path = root.join(idStrings[0], JoinType.LEFT);
            for (int i = 1; i < idStrings.length; i++) {
                if (i < idStrings.length - 1) {
                    path = ((Join<?, ?>) path)
                            .join(idStrings[i], JoinType.LEFT);
                } else {
                    path = path.get(idStrings[i]);
                }
            }
        } else {
            // non-nested or embedded, we can select as usual
            path = AdvancedFilterableSupport.getPropertyPathTyped(root, sortedPropId);
        }

        // Make and return the Order instances.
        if (sortBy.getRight()) {
            return cb.asc(path);
        } else {
            return cb.desc(path);
        }
    }

    private boolean isEmbedded(final String propertyId) {
        return entityClassMetadata.getProperty(propertyId).getPropertyKind() == PropertyKind.EMBEDDED;
    }

    private class LazyEntityProvider implements LazyList.EntityProvider {
        @Override
        public int size() {
            return listSize();
        }

        @Override
        public List findEntities(final int firstRow) {
            return findListEntities(firstRow);
        }
    }
}
