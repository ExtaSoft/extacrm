package ru.extas.web.commons.container.jpa;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import ru.extas.model.common.IdentifiedObject;

import java.util.*;

import static com.google.common.base.Throwables.propagate;

/**
 * @author Valery Orlov
 *         Date: 13.04.2015
 *         Time: 12:58
 */
public class JpaEntityItem<TEntityType extends IdentifiedObject> implements Item {
    private final boolean cached;
    private TEntityType bean;
    private List nestedProps;
    private final Map<Object, EntityItemProperty> propertyIdToProperty = new HashMap<Object, EntityItemProperty>();
    private final JpaPropertyProvider<TEntityType> propertyProvider;
    private LinkedList<Item.PropertySetChangeListener> propertySetChangeListeners = null;


    JpaEntityItem(final TEntityType bean, final JpaPropertyProvider<TEntityType> propertyProvider, final boolean cached, final Object... nested) {
        this.bean = bean;
        this.propertyProvider = propertyProvider;
        this.nestedProps = Arrays.asList(nested);
        this.cached = cached;
    }

    JpaEntityItem(final TEntityType bean, final JpaPropertyProvider<TEntityType> propertyProvider, final Object... nested) {
        this(bean, propertyProvider, false, nested);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaEntityItem)) return false;

        final JpaEntityItem<?> that = (JpaEntityItem<?>) o;

        return bean.equals(that.bean);

    }

    @Override
    public int hashCode() {
        return bean.hashCode();
    }

    public boolean isCached() {
        return cached;
    }

    public JpaPropertyProvider<TEntityType> getPropertyProvider() {
        return propertyProvider;
    }

    public TEntityType getBean() {
        return bean;
    }

    @Override
    public Property getItemProperty(final Object id) {
        EntityItemProperty prop = propertyIdToProperty.get(id);
        if (prop == null) {
            if (propertyProvider.isNestedProp(id))
                prop = new NestedProperty(id.toString());
            else
                prop = new DynaProperty(id.toString());
            propertyIdToProperty.put(id, prop);
        }
        return prop;
    }

    @Override
    public Collection<String> getItemPropertyIds() {
        return propertyProvider.getPropertyIds();
    }

    @Override
    public boolean addItemProperty(final Object id, final Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItemProperty(final Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refreshBean(final TEntityType freshEntity) {
        this.bean = freshEntity;
        this.nestedProps = Collections.emptyList();
        for (final EntityItemProperty property : propertyIdToProperty.values()) {
            property.fireValueChangeEvent();
        }
    }

    private abstract class EntityItemProperty implements Property,
            Property.ValueChangeNotifier {
        private List<ValueChangeListener> listeners;

        private class ValueChangeEvent extends EventObject implements
                Property.ValueChangeEvent {

            private static final long serialVersionUID = 4999596001491426923L;

            private ValueChangeEvent(final Property source) {
                super(source);
            }

            @Override
            public Property getProperty() {
                return (Property) getSource();
            }
        }

        /**
         * Notifies all the listeners that the value of the property has
         * changed.
         */
        public void fireValueChangeEvent() {
            if (listeners != null) {
                final Object[] l = listeners.toArray();
                final Property.ValueChangeEvent event = new ValueChangeEvent(
                        this);
                for (int i = 0; i < l.length; i++) {
                    ((Property.ValueChangeListener) l[i]).valueChange(event);
                }
            }
        }

        @Deprecated
        @Override
        public void addListener(final ValueChangeListener listener) {
            assert listener != null : "listener must not be null";
            if (listeners == null) {
                listeners = new LinkedList<ValueChangeListener>();
            }
            listeners.add(listener);
        }

        @Deprecated
        @Override
        public void removeListener(final ValueChangeListener listener) {
            assert listener != null : "listener must not be null";
            if (listeners != null) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    listeners = null;
                }
            }
        }

        @Override
        public void addValueChangeListener(final ValueChangeListener listener) {
            addListener(listener);
        }

        @Override
        public void removeValueChangeListener(final ValueChangeListener listener) {
            removeListener(listener);
        }
    }

    private class NestedProperty extends EntityItemProperty {

        private final String propertyName;

        public NestedProperty(final String property) {
            propertyName = property;
        }

        @Override
        public Object getValue() {
            try {
                final int propIndex = propertyProvider.getNestedIndex(propertyName);
                if (propIndex >= 0 && propIndex < nestedProps.size())
                    return nestedProps.get(propIndex);
                else
                    return PropertyUtils.getNestedProperty(bean, propertyName);
            } catch (final NestedNullException ne) {
                return null;
            } catch (final Throwable e) {
                propagate(e);
            }
            return null;
        }

        @Override
        public void setValue(final Object newValue) throws ReadOnlyException {
            try {
                PropertyUtils.setNestedProperty(bean, propertyName, newValue);
                final int propIndex = propertyProvider.getNestedIndex(propertyName);
                if (propIndex >= 0 && propIndex < nestedProps.size())
                    nestedProps.set(propIndex, newValue);
                fireValueChangeEvent();
            } catch (final Throwable e) {
                propagate(e);
            }
        }

        @Override
        public Class getType() {
            return propertyProvider.getPropType(propertyName);
        }

        @Override
        public boolean isReadOnly() {
            return propertyProvider.isPropReadOnly(bean, propertyName);
        }

        @Override
        public void setReadOnly(final boolean newStatus) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class DynaProperty extends EntityItemProperty {

        private final String propertyName;

        public DynaProperty(final String property) {
            propertyName = property;
        }

        @Override
        public Object getValue() {
            return propertyProvider.getBeanProp(bean, propertyName);
        }

        @Override
        public void setValue(final Object newValue) throws Property.ReadOnlyException {
            propertyProvider.setBeanProp(bean, propertyName, newValue);
            fireValueChangeEvent();
        }

        @Override
        public Class getType() {
            return propertyProvider.getPropType(propertyName);
        }

        @Override
        public boolean isReadOnly() {
            return propertyProvider.isPropReadOnly(bean, propertyName);
        }

        @Override
        public void setReadOnly(final boolean newStatus) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }


}
