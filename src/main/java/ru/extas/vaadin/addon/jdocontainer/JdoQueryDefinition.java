package ru.extas.vaadin.addon.jdocontainer;

import com.vaadin.data.util.MethodPropertyDescriptor;
import com.vaadin.data.util.VaadinPropertyDescriptor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Defines entity query definition to be used with JPA entity managers.
 *
 * @param <E>
 * @author Valery Orlov
 */
public class JdoQueryDefinition<E> extends LazyQueryDefinition {
    /**
     * Serial version UID for this class.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Class of the persistent entity type.
     */
    private final Class<E> jdoClass;

    /**
     * Constructor for configuring query definition.
     *
     * @param jdoClass     The entity class.
     * @param batchSize    The batch size.
     * @param idPropertyId The ID of the ID property or null if item index is used as ID.
     */
    public JdoQueryDefinition(final Class<E> jdoClass, final int batchSize,
                              final Object idPropertyId) {
        super(false, batchSize, idPropertyId);
        this.jdoClass = jdoClass;
        for (final Entry<String, VaadinPropertyDescriptor<E>> propDesc : getPropertyDescriptors(jdoClass).entrySet()) {
            addProperty(propDesc.getKey(), propDesc.getValue().getPropertyType(), null, false, true);
        }
    }

    /**
     * Gets class of the persistent entity type.
     *
     * @return the entityClass
     */
    public final Class<E> getJdoClass() {
        return jdoClass;
    }

    LinkedHashMap<String, VaadinPropertyDescriptor<E>> getPropertyDescriptors(
            final Class<E> beanClass) {
        final LinkedHashMap<String, VaadinPropertyDescriptor<E>> pdMap = new LinkedHashMap<>();

        // Try to introspect, if it fails, we just have an empty Item
        try {
            final List<PropertyDescriptor> propertyDescriptors = getBeanPropertyDescriptor(beanClass);

            // Add all the bean properties as MethodProperties to this Item
            // later entries on the list overwrite earlier ones
            for (final PropertyDescriptor pd : propertyDescriptors) {
                final Method getMethod = pd.getReadMethod();
                if ((getMethod != null)
                        && getMethod.getDeclaringClass() != Object.class) {
                    final VaadinPropertyDescriptor<E> vaadinPropertyDescriptor = new MethodPropertyDescriptor<>(
                            pd.getName(), pd.getPropertyType(),
                            pd.getReadMethod(), pd.getWriteMethod());
                    pdMap.put(pd.getName(), vaadinPropertyDescriptor);
                }
            }
        } catch (final java.beans.IntrospectionException ignored) {
        }

        return pdMap;
    }

    @SuppressWarnings("unchecked")
    private List<PropertyDescriptor> getBeanPropertyDescriptor(
            final Class<E> beanClass) throws IntrospectionException {
        // Oracle bug 4275879: Introspector does not consider superinterfaces of
        // an interface
        if (beanClass.isInterface()) {
            final List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

            for (final Class<?> cls : beanClass.getInterfaces()) {
                propertyDescriptors.addAll(getBeanPropertyDescriptor((Class<E>) cls));
            }

            final BeanInfo info = Introspector.getBeanInfo(beanClass);
            propertyDescriptors.addAll(Arrays.asList(info
                    .getPropertyDescriptors()));

            return propertyDescriptors;
        } else {
            final BeanInfo info = Introspector.getBeanInfo(beanClass);
            return Arrays.asList(info.getPropertyDescriptors());
        }
    }

}
