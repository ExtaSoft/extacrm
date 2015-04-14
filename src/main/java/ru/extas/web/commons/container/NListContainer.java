package ru.extas.web.commons.container;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang3.ClassUtils;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Интерпретация <code>org.vaadin.viritin.ListContainer</code>
 * с поддержкой свойств вложенных объектов
 *
 * @author Valery Orlov
 *         Date: 09.04.2015
 *         Time: 9:41
 */
public class NListContainer<T> extends AbstractContainer implements
        Container.Indexed, Container.Sortable, Container.ItemSetChangeNotifier {

    private List<T> backingList;
    private final List<String> nestedProps = newArrayList();
    private Resolver resolver = new DefaultResolver();

    public NListContainer(final Collection<T> backingList) {
        setCollection(backingList);
    }

    public NListContainer(final Class<T> type, final Collection<T> backingList) {
        dynaClass = WrapDynaClass.createDynaClass(type);
        setCollection(backingList);
    }

    public final void setCollection(final Collection<T> backingList1) {
        if (backingList1 instanceof List) {
            this.backingList = (List<T>) backingList1;
        } else {
            this.backingList = new ArrayList<T>(backingList1);
        }
        fireItemSetChange();
    }

    public NListContainer(final Class<T> type) {
        backingList = new ArrayList<T>();
        dynaClass = WrapDynaClass.createDynaClass(type);
    }

    protected List<T> getBackingList() {
        return backingList;
    }

    private transient WrapDynaClass dynaClass;

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

    public int indexOf(final T bean) {
        return indexOfId(bean);
    }

    @Override
    public T getIdByIndex(final int index) {
        return getBackingList().get(index);
    }

    @Override
    public List<T> getItemIds(final int startIndex, final int numberOfItems) {
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
        backingList.add(index, (T) newItemId);
        fireItemSetChange();
        return getItem(newItemId);
    }

    @Override
    public T nextItemId(final Object itemId) {
        final int i = getBackingList().indexOf(itemId) + 1;
        if (getBackingList().size() == i) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public T prevItemId(final Object itemId) {
        final int i = getBackingList().indexOf(itemId) - 1;
        if (i < 0) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public T firstItemId() {
        return (getBackingList().isEmpty()) ? null : getBackingList().get(0);
    }

    @Override
    public T lastItemId() {
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
        return new DynaBeanItem<T>((T) itemId);
    }

    public void addNestedContainerProperty(final String nestedProp) {
        if (resolver.hasNested(nestedProp))
            nestedProps.add(nestedProp);
        fireContainerPropertySetChange();
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
                    Logger.getLogger(NListContainer.class.getName()).log(
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
        return getBackingList().contains((T) itemId);
    }

    @Override
    public Item addItem(final Object itemId) throws UnsupportedOperationException {
        backingList.add((T) itemId);
        fireItemSetChange();
        return getItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItem(final Object itemId) throws UnsupportedOperationException {
        final boolean remove = backingList.remove((T) itemId);
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

    public NListContainer addAll(final Collection<T> beans) {
        backingList.addAll(beans);
        fireItemSetChange();
        return this;
    }

    @Override
    public void sort(final Object[] propertyId, final boolean[] ascending) {
        // Grid in 7.4 may call this method with empty sorting instructions...
        if (propertyId.length > 0) {
            final Comparator<T> comparator = new PropertyComparator(propertyId,
                    ascending);

            Collections.sort(backingList, comparator);
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (backingList instanceof SortableLazyList) {
            // Assume SortableLazyList can sort by any Comparable property
        } else if (backingList instanceof LazyList) {
            // When using LazyList, don't support sorting by default
            // as the sorting should most probably be done at backend call level
            return Collections.emptySet();
        }
        final ArrayList<String> properties = new ArrayList<String>();
        for (final Object a : getContainerPropertyIds()) {
            final DynaProperty db = getDynaClass().getDynaProperty(a.toString());
            if (db != null && db.getType() != null && (db.getType().
                    isPrimitive() || Comparable.class.isAssignableFrom(
                    db.getType()))) {
                properties.add(db.getName());
            }
        }
        return properties;
    }

    public void addItemSetChangeListener(
            final Container.ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    public void removeItemSetChangeListener(
            final Container.ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    public void addListener(final Container.ItemSetChangeListener listener) {
        super.addListener(listener);
    }

    public void removeListener(final Container.ItemSetChangeListener listener) {
        super.removeListener(listener);
    }

    /**
     * Override point. Allows user to use custom comparators based on properties.
     *
     * @param property the property whose comparator is requested
     * @return Comparator that will compare two objects based on a property
     */
    protected Comparator<T> getUnderlyingComparator(final Object property) {
        return new NullComparator();
    }

    private class PropertyComparator implements Comparator<T> {

        private final Object[] propertyId;
        private final boolean[] ascending;

        private PropertyComparator(final Object[] propertyId, final boolean[] ascending) {
            this.propertyId = propertyId;
            this.ascending = ascending;
        }

        @Override
        public int compare(final T o1, final T o2) {
            for (int i = 0; i < propertyId.length; i++) {
                final String currentProperty = propertyId[i].toString();
                Comparator<T> currentComparator =
                        new BeanComparator<T>(currentProperty, getUnderlyingComparator(currentProperty));

                if (!ascending[i]) {
                    currentComparator = new ReverseComparator(currentComparator);
                }

                final int compare = currentComparator.compare(o1, o2);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
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
                return NListContainer.this.getType(propertyName);
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
            public void setValue(final Object newValue) throws Property.ReadOnlyException {
                getDynaBean().set(propertyName, newValue);
            }

            @Override
            public Class getType() {
                return NListContainer.this.getType(propertyName);
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
            return NListContainer.this.getContainerPropertyIds();
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
