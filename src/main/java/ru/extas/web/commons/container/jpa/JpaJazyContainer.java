package ru.extas.web.commons.container.jpa;

import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.addon.jpacontainer.filter.util.FilterConverter;
import com.vaadin.addon.jpacontainer.metadata.EntityClassMetadata;
import com.vaadin.addon.jpacontainer.metadata.MetadataFactory;
import com.vaadin.addon.jpacontainer.metadata.PropertyKind;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.support.TransactionTemplate;
import org.vaadin.viritin.LazyList;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.*;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 10.04.2015
 *         Time: 11:12
 */
public class JpaJazyContainer<TEntityType extends IdentifiedObject>
        extends
        AbstractContainer
        implements
        Container.Filterable,
        Container.Indexed,
        Container.Sortable,
        Container.ItemSetChangeNotifier {

    private static final int CONTAINER_PAGE_SIZE = LazyList.DEFAULT_PAGE_SIZE;
    private final Class<TEntityType> entityClass;
    private final JpaLazyItemList<TEntityType> entityList;
    private final AdvancedFilterableSupport filterSupport;
    private final List<String> nestedProps = newArrayList();
    private List<Pair<String, Boolean>> sortByList = newLinkedList();
    private final EntityClassMetadata<TEntityType> entityClassMetadata;
    private List<TEntityType> backingList;
    private Resolver resolver = new DefaultResolver();
    private transient WrapDynaClass dynaClass;


    public JpaJazyContainer(final Class<TEntityType> type) {
        this.backingList = new ArrayList<TEntityType>();
        this.dynaClass = WrapDynaClass.createDynaClass(type);
        this.entityClass = type;
        this.entityClassMetadata = MetadataFactory.getInstance().getEntityClassMetadata(entityClass);
        entityList = new JpaLazyItemList<TEntityType>();
        setCollection(entityList);
        this.filterSupport = new AdvancedFilterableSupport();
        this.filterSupport.addListener(e -> refresh());

        updateFilterablePropertyIds();
    }

    protected void updateFilterablePropertyIds() {
        this.filterSupport.setFilterablePropertyIds(getContainerPropertyIds());
    }

    public void addNestedContainerProperty(final String nestedProp) {
        if (resolver.hasNested(nestedProp))
            nestedProps.add(nestedProp);
        updateFilterablePropertyIds();
        fireContainerPropertySetChange();
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
            return cb.codesc(path);
        }
    }

    private boolean isEmbedded(final String propertyId) {
        return entityClassMetadata.getProperty(propertyId).getPropertyKind() == PropertyKind.EMBEDDED;
    }

    public final void setCollection(final Collection<TEntityType> backingList1) {
        if (backingList1 instanceof List) {
            this.backingList = (List<TEntityType>) backingList1;
        } else {
            this.backingList = new ArrayList<TEntityType>(backingList1);
        }
        fireItemSetChange();
    }

    protected List<TEntityType> getBackingList() {
        return backingList;
    }

    private WrapDynaClass getDynaClass() {
        if (dynaClass == null && !backingList.isEmpty()) {
            dynaClass = WrapDynaClass.createDynaClass(backingList.get(0).
                    getClass());
        }
        return dynaClass;
    }

    @Override
    public int indexOfId(final Object itemId) {
        return getBackingList().indexOf(itemId);
    }

    public int indexOf(final TEntityType bean) {
        return indexOfId(bean);
    }

    @Override
    public TEntityType getIdByIndex(final int index) {
        return getBackingList().get(index);
    }

    @Override
    public List<TEntityType> getItemIds(final int startIndex, final int numberOfItems) {
        // Whooo!? Vaadin calls this method with numberOfItems == -1
        if (numberOfItems < 0) {
            throw new IllegalArgumentException();
        }

        return getBackingList().subList(startIndex, startIndex + numberOfItems);
    }

    @Override
    public Object addItemAt(final int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item addItemAt(final int index, final Object newItemId) throws UnsupportedOperationException {
        backingList.add(index, (TEntityType) newItemId);
        fireItemSetChange();
        return getItem(newItemId);
    }

    @Override
    public TEntityType nextItemId(final Object itemId) {
        final int i = getBackingList().indexOf(itemId) + 1;
        if (getBackingList().size() == i) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public TEntityType prevItemId(final Object itemId) {
        final int i = getBackingList().indexOf(itemId) - 1;
        if (i < 0) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public TEntityType firstItemId() {
        return (getBackingList().isEmpty()) ? null : getBackingList().get(0);
    }

    @Override
    public TEntityType lastItemId() {
        return getBackingList().isEmpty() ? null : getBackingList().get(getBackingList().size() - 1);
    }

    @Override
    public boolean isFirstId(final Object itemId) {
        return itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(final Object itemId) {
        return itemId.equals(lastItemId());
    }

    @Override
    public Object addItemAfter(final Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item addItemAfter(final Object previousItemId, final Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item getItem(final Object itemId) {
        if (itemId == null) {
            return null;
        }
        return new DynaBeanItem<TEntityType>((TEntityType) itemId);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        final ArrayList<String> properties = new ArrayList<String>();
        if (getDynaClass() != null) {
            for (final DynaProperty db : getDynaClass().getDynaProperties()) {
                if (db.getType() != null) {
                    properties.add(db.getName());
                } else {
                    // type may be null in some cases
                    Logger.getLogger(JpaJazyContainer.class.getName()).log(
                            Level.FINE, "Type not detected for property {0}",
                            db.getName());
                }
            }
            properties.remove("class");
            properties.addAll(nestedProps);
        }
        return properties;
    }

    @Override
    public Collection<?> getItemIds() {
        return getBackingList();
    }

    @Override
    public Property getContainerProperty(final Object itemId, final Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(final Object propertyId) {
        final String propName = propertyId.toString();
        final DynaProperty dynaProperty = getDynaClass().getDynaProperty(propName);
        final Class<?> type;
        if (dynaProperty != null)
            type = dynaProperty.getType();
        else if (nestedProps.contains(propName))
            type = getNestedPropType(propName);
        else
            throw new IllegalArgumentException("Wrong propertyId");

        if (type.isPrimitive()) {
            // Vaadin can't handle primitive types in _all_ places, so use
            // wrappers instead. FieldGroup works, but e.g. Table in _editable_
            // mode fails for some reason
            return ClassUtils.primitiveToWrapper(type);
        }
        return type;
    }

    private Class<?> getNestedPropType(String nestedProp) {
        WrapDynaClass propClass = getDynaClass();
        String prop = null;
        Class<?> propType = Object.class;
        while (resolver.hasNested(nestedProp)) {
            prop = resolver.next(nestedProp);

            propType = propClass.getDynaProperty(prop).getType();
            propClass = WrapDynaClass.createDynaClass(propType);

            nestedProp = resolver.remove(nestedProp);
        }
        return propType;
    }

    @Override
    public int size() {
        return getBackingList().size();
    }

    @Override
    public boolean containsId(final Object itemId) {
        return getBackingList().contains((TEntityType) itemId);
    }

    @Override
    public Item addItem(final Object itemId) throws UnsupportedOperationException {
        backingList.add((TEntityType) itemId);
        fireItemSetChange();
        return getItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItem(final Object itemId) throws UnsupportedOperationException {
        final boolean remove = backingList.remove((TEntityType) itemId);
        if (remove) {
            fireItemSetChange();
        }
        return remove;
    }

    @Override
    public boolean addContainerProperty(final Object propertyId,
                                        final Class<?> type, final Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeContainerProperty(final Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        backingList.clear();
        fireItemSetChange();
        return true;
    }

    public JpaJazyContainer<TEntityType> addAll(final Collection<TEntityType> beans) {
        backingList.addAll(beans);
        fireItemSetChange();
        return this;
    }

    public void addItemSetChangeListener(
            final ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    public void removeItemSetChangeListener(
            final ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    public void addListener(final ItemSetChangeListener listener) {
        super.addListener(listener);
    }

    public void removeListener(final ItemSetChangeListener listener) {
        super.removeListener(listener);
    }


    public class DynaBeanItem<T> implements Item {

        private final Map<Object, Property> propertyIdToProperty = new HashMap<Object, Property>();

        private class NestedProperty implements Property {

            private final String propertyName;

            public NestedProperty(final String property) {
                propertyName = property;
            }

            @Override
            public Object getValue() {
                try {
                    return PropertyUtils.getNestedProperty(bean, propertyName);
                } catch (final Throwable e) {
                    propagate(e);
                }
                return null;
            }

            @Override
            public void setValue(final Object newValue) throws ReadOnlyException {
                try {
                    PropertyUtils.setNestedProperty(bean, propertyName, newValue);
                } catch (final Throwable e) {
                    propagate(e);
                }
            }

            @Override
            public Class getType() {
                return JpaJazyContainer.this.getType(propertyName);
            }

            @Override
            public boolean isReadOnly() {
                try {
                    return PropertyUtils.getPropertyDescriptor(bean, propertyName).getWriteMethod() == null;
                } catch (final Throwable e) {
                    propagate(e);
                }
                return true;
            }

            @Override
            public void setReadOnly(final boolean newStatus) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        private class DynaProperty implements Property {

            private final String propertyName;

            public DynaProperty(final String property) {
                propertyName = property;
            }

            @Override
            public Object getValue() {
                return getDynaBean().get(propertyName);
            }

            @Override
            public void setValue(final Object newValue) throws ReadOnlyException {
                getDynaBean().set(propertyName, newValue);
            }

            @Override
            public Class getType() {
                return JpaJazyContainer.this.getType(propertyName);
            }

            @Override
            public boolean isReadOnly() {
                final PropertyDescriptor descriptor = getDynaClass().getPropertyDescriptor(propertyName);
                if (descriptor != null)
                    return descriptor.getWriteMethod() == null;
                else
                    throw new IllegalStateException("Wrong property name");
            }

            @Override
            public void setReadOnly(final boolean newStatus) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        }

        private T bean;

        private transient DynaBean db;

        public DynaBeanItem(final T bean) {
            this.bean = bean;
        }

        public T getBean() {
            return bean;
        }

        private DynaBean getDynaBean() {
            if (db == null) {
                db = new WrapDynaBean(bean);
            }
            return db;
        }

        @Override
        public Property getItemProperty(final Object id) {
            Property prop = propertyIdToProperty.get(id);
            if (prop == null) {
                if (nestedProps.contains(id))
                    prop = new NestedProperty(id.toString());
                else
                    prop = new DynaProperty(id.toString());
                propertyIdToProperty.put(id, prop);
            }
            return prop;
        }

        @Override
        public Collection<String> getItemPropertyIds() {
            return JpaJazyContainer.this.getContainerPropertyIds();
        }

        @Override
        public boolean addItemProperty(final Object id, final Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean removeItemProperty(final Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}

