package ru.extas.web.commons.container.jpa;

import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.addon.jpacontainer.util.CollectionUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.support.TransactionTemplate;
import org.vaadin.viritin.LazyList;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.utils.SupplierSer;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newLinkedList;
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
    private final JpaLazyItemList<TEntityType> entityItemList;

    private final JpaPropertyProvider propertyProvider;
    private final AdvancedFilterableSupport filterSupport;
    private List<Pair<String, Boolean>> sortByList = newLinkedList();

    public JpaJazyContainer(final Class<TEntityType> type) {
        this.entityClass = type;
        this.propertyProvider = new JpaPropertyProvider(entityClass);
        entityItemList = createJpaEntityItemList(this.entityClass, () -> sortByList,
                () -> getAppliedFiltersAsConjunction(), this.propertyProvider);
        this.filterSupport = new AdvancedFilterableSupport();
        this.filterSupport.addListener(e -> refresh());

        updateFilterablePropertyIds();
    }

    protected JpaLazyItemList<TEntityType> createJpaEntityItemList(Class<TEntityType> typeClass, SupplierSer<List<Pair<String, Boolean>>> sortBySupplier, SupplierSer<Filter> filterSupplierSer, JpaPropertyProvider jpaPropertyProvider) {
        return new JpaLazyItemList<TEntityType>(typeClass,
                sortBySupplier, filterSupplierSer, jpaPropertyProvider);
    }

    public JpaLazyItemList<TEntityType> getEntityItemList() {
        return entityItemList;
    }

    protected void updateFilterablePropertyIds() {
        this.filterSupport.setFilterablePropertyIds(getContainerPropertyIds());
    }

    public void addNestedContainerProperty(final String nestedProp) {
        propertyProvider.addNestedContainerProperty(nestedProp);
        updateFilterablePropertyIds();
        fireContainerPropertySetChange();
    }

    public void refresh() {
        entityItemList.reset();
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


    @Override
    public int indexOfId(final Object itemId) {
        return entityItemList.indexOf(itemId);
    }

    public int indexOf(final TEntityType bean) {
        return indexOfId(bean);
    }

    @Override
    public Object getIdByIndex(final int index) {
        return entityItemList.get(index).getBean();
    }

    @Override
    public List getItemIds(final int startIndex, final int numberOfItems) {
        // Whooo!? Vaadin calls this method with numberOfItems == -1
        if (numberOfItems < 0) {
            throw new IllegalArgumentException();
        }

        return entityItemList.subList(startIndex, startIndex + numberOfItems);
    }

    @Override
    public Object addItemAt(final int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item addItemAt(final int index, final Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object nextItemId(final Object itemId) {
        final int i = entityItemList.indexOf(itemId) + 1;
        if (entityItemList.size() == i) {
            return null;
        }
        return entityItemList.get(i);
    }

    @Override
    public Object prevItemId(final Object itemId) {
        final int i = entityItemList.indexOf(itemId) - 1;
        if (i < 0) {
            return null;
        }
        return entityItemList.get(i);
    }

    @Override
    public Object firstItemId() {
        return (entityItemList.isEmpty()) ? null : entityItemList.get(0);
    }

    @Override
    public Object lastItemId() {
        return entityItemList.isEmpty() ? null : entityItemList.get(entityItemList.size() - 1);
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
        return ((JpaEntityItem<TEntityType>) itemId);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        return propertyProvider.getPropertyIds();
    }

    @Override
    public Collection<?> getItemIds() {
        return entityItemList;
    }

    @Override
    public Property getContainerProperty(final Object itemId, final Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(final Object propertyId) {
        return propertyProvider.getPropType((String) propertyId);
    }

    @Override
    public int size() {
        return entityItemList.size();
    }

    @Override
    public boolean containsId(final Object itemId) {
        return entityItemList.contains((JpaEntityItem<TEntityType>) itemId);
    }

    @Override
    public Item addItem(final Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItem(final Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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


}

