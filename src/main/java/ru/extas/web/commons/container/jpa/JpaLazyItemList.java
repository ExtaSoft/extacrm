package ru.extas.web.commons.container.jpa;

import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.metadata.EntityClassMetadata;
import com.vaadin.addon.jpacontainer.metadata.MetadataFactory;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.Container;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.springframework.transaction.support.TransactionTemplate;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;
import ru.extas.utils.SupplierSer;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.collect.Lists.*;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 10.04.2015
 *         Time: 17:28
 */
public class JpaLazyItemList<TEntityType extends IdentifiedObject> extends AbstractList<JpaEntityItem<TEntityType>> implements Serializable {

    // Vaadin table by default has 15 rows, 2x that to cache up an down
    // With this setting it is maximum of 2 requests that happens. With
    // normal scrolling just 0-1 per user interaction
    public static final int DEFAULT_PAGE_SIZE = 15 + 15 * 2;

    private List<JpaEntityItem> currentPage;
    private List<JpaEntityItem> prevPage;
    private List<JpaEntityItem> nextPage;

    private int pageIndex = -10;
    private final int pageSize = DEFAULT_PAGE_SIZE;

    private final Class<TEntityType> entityClass;
    private final SupplierSer<List<JpaSortBy>> sortBySupplier;
    private final EntityClassMetadata<TEntityType> entityClassMetadata;
    private final SupplierSer<Container.Filter> filterSupplier;
    private final JpaPropertyProvider propertyProvider;

    public JpaLazyItemList(final Class<TEntityType> entityClass, final SupplierSer<List<JpaSortBy>> sortBySupplier, final SupplierSer<Container.Filter> filterSupplier, final JpaPropertyProvider propertyProvider) {
        this.entityClass = entityClass;
        this.sortBySupplier = sortBySupplier;
        this.filterSupplier = filterSupplier;
        this.propertyProvider = propertyProvider;
        this.entityClassMetadata = MetadataFactory.getInstance().getEntityClassMetadata(entityClass);
    }

    @Override
    public JpaEntityItem<TEntityType> get(final int index) {
        final int pageIndexForReqest = index / pageSize;
        final int indexOnPage = index % pageSize;

        // Find page from cache
        List<JpaEntityItem> page = null;
        if (pageIndex == pageIndexForReqest) {
            page = currentPage;
        } else if (pageIndex - 1 == pageIndexForReqest && prevPage != null) {
            page = prevPage;
        } else if (pageIndex + 1 == pageIndexForReqest && nextPage != null) {
            page = nextPage;
        }

        if (page == null) {
            // Page not in cache, change page, move next/prev is feasible
            if (pageIndexForReqest - 1 == pageIndex) {
                // going to next page
                prevPage = currentPage;
                currentPage = nextPage;
                nextPage = null;
            } else if (pageIndexForReqest + 1 == pageIndex) {
                // going to previous page
                nextPage = currentPage;
                currentPage = prevPage;
                prevPage = null;
            } else {
                currentPage = null;
                prevPage = null;
                nextPage = null;
            }
            pageIndex = pageIndexForReqest;
            if (currentPage == null) {
                currentPage = queryEntities(pageIndex * pageSize);
            }
            if (currentPage == null) {
                return null;
            } else {
                page = currentPage;
            }
        }
        final JpaEntityItem<TEntityType> get = page.get(indexOnPage);
        return get;
    }

    private List<JpaEntityItem> queryEntities(final int firstRow) {
        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> query = cb.createTupleQuery();
        final Root<TEntityType> root = query.from(entityClass);

        // Добавляем фильтр контейнера
        final List<Predicate> predicates = createContainerFilterPredicates(cb, query, root);
        if (!predicates.isEmpty()) {
            query.where(CollectionUtil.toArray(Predicate.class, predicates));
        }

        // формируем возвращаемое значение
        final List<String> nestedProps = propertyProvider.getNestedProps();
        final List<Selection<?>> selectionList = newArrayListWithCapacity(1 + nestedProps.size());
        selectionList.add(root);
        for (final String prop : nestedProps) {
            selectionList.add(getPropertyPath(root, prop));
        }
        query.multiselect(selectionList);

        // Добавляем сортировку
        final List<JpaSortBy> sortByList = sortBySupplier.get();
        if (!sortByList.isEmpty()) {
            final List<Order> orders = newArrayListWithCapacity(sortByList.size());
            for (final JpaSortBy sortBy : sortByList)
                orders.add(translateSortBy(sortBy, cb, root));
            query.orderBy(orders);
        }

        final TypedQuery<Tuple> tq = em.createQuery(query);
        tq.setFirstResult(firstRow);
        tq.setMaxResults(pageSize);
        final List<Tuple> resultList = tq.getResultList();
        if (resultList.isEmpty())
            return null;
        else
            return newArrayList(
                    resultList
                            .stream()
                            .map(tuple -> {
                                final Object[] nestedArray = new Object[nestedProps.size()];
                                for (int i = 0; i < nestedProps.size(); i++) {
                                    nestedArray[i] = tuple.get(i + 1);
                                }
                                return new JpaEntityItem<TEntityType>(tuple.get(root), propertyProvider, true, nestedArray);
                            })
                            .iterator());
    }

    private Order translateSortBy(final JpaSortBy sortBy, final CriteriaBuilder cb, final Root<TEntityType> root) {
        final String sortedPropId = sortBy.getProperty();
        final Path<TEntityType> path = getPropertyPath(root, sortedPropId);

        // Make and return the Order instances.
        if (sortBy.isAsceding()) {
            return cb.asc(path);
        } else {
            return cb.desc(path);
        }
    }

    private Path<TEntityType> getPropertyPath(final Root<TEntityType> root, final String propId) {
        Path path;
        if (propertyProvider.isNestedProp(propId)) {
            // First split the id and build a Path.
            final String[] idStrings = propId.split("\\.");
            if (!isEmbedded(idStrings[0])) {
                // пробуем найти join
                //root.getJoins().stream().filter(j -> j.)
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
            } else
                path = AdvancedFilterableSupport.getPropertyPathTyped(root, propId);
        } else {
            // non-nested or embedded, we can select as usual
            path = AdvancedFilterableSupport.getPropertyPathTyped(root, propId);
        }
        return path;
    }

    private boolean isEmbedded(final String propertyId) {
        return entityClassMetadata.getProperty(propertyId).getPropertyKind() == PropertyKind.EMBEDDED;
    }

    private Integer cachedSize;

    @Override
    public int size() {
        if (cachedSize == null) {
            cachedSize = querySize();
        }
        return cachedSize;
    }

    private int querySize() {
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

    protected List<Predicate> createContainerFilterPredicates(final CriteriaBuilder cb,
                                                              final CriteriaQuery<?> query,
                                                              final Root<TEntityType> root) {
        final Container.Filter filter = filterSupplier.get();
        final List<Predicate> predicates = newLinkedList();
        if (filter != null) {
            predicates.add(FilterConverter.convertFilter(filter, cb, root));
        }
        return predicates;
    }

    private transient WeakHashMap<JpaEntityItem<TEntityType>, Integer> indexCache;

    private Map<JpaEntityItem<TEntityType>, Integer> getIndexCache() {
        if (indexCache == null) {
            indexCache = new WeakHashMap<JpaEntityItem<TEntityType>, Integer>();
        }
        return indexCache;
    }

    @Override
    public int indexOf(final Object o) {
        // optimize: check the buffers first
        Integer indexViaCache = getIndexCache().get(o);
        if (indexViaCache != null) {
            return indexViaCache;
        } else { // проверим загруженные записи (смотрим в буфер)
            indexViaCache = getBufferIndex(o);
        }
        if (indexViaCache == null) { // Не нашли в кеше и буфере, значит надо запросить у БД
            indexViaCache = queryIndexOf((JpaEntityItem<TEntityType>) o);
        }
        if (indexViaCache != null) {
            getIndexCache().put((JpaEntityItem<TEntityType>) o, indexViaCache);
            return indexViaCache;
        }
        return -1;
    }

    private Integer queryIndexOf(final JpaEntityItem<TEntityType> item) {
        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery query = cb.createQuery();
        final Root<TEntityType> root = query.from(entityClass);

        // Добавляем фильтр контейнера
        final List<Predicate> predicates = createContainerFilterPredicates(cb, query, root);
        if (!predicates.isEmpty()) {
            query.where(CollectionUtil.toArray(Predicate.class, predicates));
        }

        // Добавляем сортировку
        final List<JpaSortBy> sortByList = sortBySupplier.get();
        if (!sortByList.isEmpty()) {
            final List<Order> orders = newArrayListWithCapacity(sortByList.size());
            for (final JpaSortBy sortBy : sortByList)
                orders.add(translateSortBy(sortBy, cb, root));
            query.orderBy(orders);
        }

        final Path<String> idPath = root.get(IdentifiedObject_.id);
        query.select(idPath).distinct(true);

        final TypedQuery<Long> tq = em.createQuery(query);
        final DatabaseQuery databaseQuery = tq.unwrap(JpaQuery.class).getDatabaseQuery();
        final DatabaseRecord translationRow = new DatabaseRecord();
        final JpaEntityManager jpaEm = em.unwrap(JpaEntityManager.class);
        databaseQuery.prepareCall(jpaEm.getSession(), translationRow);
        final String querySql = databaseQuery.getTranslatedSQLString(jpaEm.getSession(), translationRow);
        final String itemId = item.getBean().getId();
        final Query nativeQuery = em.createNativeQuery(
                MessageFormat.format(
                        "SELECT q.rownum AS indx " +
                                "FROM (SELECT s.{2} AS id, @rownum:=@rownum + 1 AS rownum " +
                                "           FROM (SELECT @rownum:=0) i, ({0}) s) q " +
                                "WHERE q.id = ''{1}''", querySql, itemId, IdentifiedObject_.id.getName()));
//            nativeQuery.unwrap(JpaQuery.class).getDatabaseQuery().setTranslationRow(translationRow);
        final Object result;
        try {
            result = nativeQuery.getSingleResult();
        } catch (final NoResultException e) {
            return -1;
        }

        return result != null ? (int) (double) result : -1;
    }

    private Integer getBufferIndex(final Object o) {
        Integer buffIndex = null;
        if (currentPage != null) {
            final int idx = currentPage.indexOf(o);
            if (idx != -1) {
                buffIndex = pageIndex * pageSize + idx;
            }
        }
        if (buffIndex == null && prevPage != null) {
            final int idx = prevPage.indexOf(o);
            if (idx != -1) {
                buffIndex = (pageIndex - 1) * pageSize + idx;
            }
        }
        if (buffIndex == null && nextPage != null) {
            final int idx = nextPage.indexOf(o);
            if (idx != -1) {
                buffIndex = (pageIndex + 1) * pageSize + idx;
            }
        }
        return buffIndex;
    }

    @Override
    public boolean contains(final Object o) {
        // Although there would be the indexed version, vaadin sometimes calls this
        // First check caches, then fall back to sluggish iterator :-(
        if (getIndexCache().containsKey(o)) {
            return true;
        } else if (currentPage != null && currentPage.contains(o)) {
            return true;
        } else if (prevPage != null && prevPage.contains(o)) {
            return true;
        } else if (nextPage != null && nextPage.contains(o)) {
            return true;
        }
        return super.contains(o);
    }

    @Override
    public Iterator<JpaEntityItem<TEntityType>> iterator() {
        return new Iterator<JpaEntityItem<TEntityType>>() {

            private int index = -1;
            private final int size = size();

            @Override
            public boolean hasNext() {
                return index + 1 < size;
            }

            @Override
            public JpaEntityItem<TEntityType> next() {
                index++;
                return get(index);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    /**
     * Resets buffers used by the LazyList.
     */
    public void reset() {
        currentPage = null;
        prevPage = null;
        nextPage = null;
        pageIndex = -10;
        cachedSize = null;
        if (indexCache != null) {
            indexCache.clear();
        }
    }

    public TEntityType refreshItem(final JpaEntityItem<TEntityType> item) {
        final TEntityType entity = item.getBean();
        final EntityManager em = lookup(EntityManager.class);
        final TEntityType freshEntity = lookup(TransactionTemplate.class).execute(
                status -> {
                    final TEntityType e = em.find(entityClass, entity.getId());
                    if (e != null)
                        em.refresh(e);
                    return e;
                });
        item.refreshBean(freshEntity);
        return freshEntity;
    }
}
