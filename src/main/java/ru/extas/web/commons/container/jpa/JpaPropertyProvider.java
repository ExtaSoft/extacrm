package ru.extas.web.commons.container.jpa;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.lang3.ClassUtils;
import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 13.04.2015
 *         Time: 16:51
 */
public class JpaPropertyProvider<TEntityType extends IdentifiedObject> implements Serializable {

    private final Class<TEntityType> entityClass;
    private final List<String> nestedProps = newArrayList();
    private final WrapDynaClass dynaClass;
    private transient Resolver resolver = new DefaultResolver();

    public JpaPropertyProvider(Class<TEntityType> entityClass) {
        this.entityClass = entityClass;
        dynaClass = WrapDynaClass.createDynaClass(entityClass);
    }

    public List<String> getNestedProps() {
        return nestedProps;
    }

    public int getNestedIndex(String propertyName) {
        return nestedProps.indexOf(propertyName);
    }

    public Class getPropType(String propertyName) {
        final String propName = propertyName.toString();
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

    public Object getBeanProp(TEntityType bean, String propertyName) {
        try {
            return PropertyUtils.getProperty(bean, propertyName);
        } catch (final Throwable e) {
            propagate(e);
        }
        return null;
    }

    public void setBeanProp(TEntityType bean, String propertyName, Object newValue) {
        try {
            PropertyUtils.setNestedProperty(bean, propertyName, newValue);
        } catch (final Throwable e) {
            propagate(e);
        }
    }

    public boolean isPropReadOnly(TEntityType bean, String propertyName){
        try {
            return PropertyUtils.getPropertyDescriptor(bean, propertyName).getWriteMethod() == null;
        } catch (final Throwable e) {
            propagate(e);
        }
        return true;

    }

    public boolean isNestedProp(Object id) {
        return nestedProps.contains(id);
    }

    public Collection<String> getPropertyIds() {
        final ArrayList<String> properties = new ArrayList<String>();
        if (getDynaClass() != null) {
            for (final DynaProperty db : getDynaClass().getDynaProperties()) {
                if (db.getType() != null) {
                    properties.add(db.getName());
                } else {
                    // type may be null in some cases
                    Logger.getLogger(JpaPropertyProvider.class.getName()).log(
                            Level.FINE, "Type not detected for property {0}",
                            db.getName());
                }
            }
            properties.remove("class");
            properties.addAll(nestedProps);
        }
        return properties;

    }

    public void addNestedContainerProperty(String nestedProp) {
        if (resolver.hasNested(nestedProp))
            nestedProps.add(nestedProp);
    }

    public WrapDynaClass getDynaClass() {
        return dynaClass;
    }
}
