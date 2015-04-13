package ru.extas.web.commons.container.jpa;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
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
    private final TEntityType bean;
    private final List nestedProps;
    private final Map<Object, Property> propertyIdToProperty = new HashMap<Object, Property>();
    private JpaPropertyProvider<TEntityType> propertyProvider;


    public JpaEntityItem(TEntityType bean, Object... nested) {
        this.bean = bean;
        this.nestedProps = Arrays.asList(nested);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JpaEntityItem)) return false;

        JpaEntityItem<?> that = (JpaEntityItem<?>) o;

        return bean.equals(that.bean);

    }

    @Override
    public int hashCode() {
        return bean.hashCode();
    }

    public JpaPropertyProvider<TEntityType> getPropertyProvider() {
        return propertyProvider;
    }

    public void setPropertyProvider(JpaPropertyProvider<TEntityType> propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    public TEntityType getBean() {
        return bean;
    }

    @Override
    public Property getItemProperty(final Object id) {
        Property prop = propertyIdToProperty.get(id);
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

    private class NestedProperty implements Property {

        private final String propertyName;

        public NestedProperty(final String property) {
            propertyName = property;
        }

        @Override
        public Object getValue() {
            try {
                int propIndex = propertyProvider.getNestedIndex(propertyName);
                if (propIndex >= 0 && propIndex < nestedProps.size())
                    return nestedProps.get(propIndex);
                else
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
                int propIndex = propertyProvider.getNestedIndex(propertyName);
                if (propIndex >= 0 && propIndex < nestedProps.size())
                    nestedProps.set(propIndex, newValue);
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

    private class DynaProperty implements Property {

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
