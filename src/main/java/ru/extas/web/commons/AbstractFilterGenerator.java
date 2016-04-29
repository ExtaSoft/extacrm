package ru.extas.web.commons;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import org.tepi.filtertable.FilterGenerator;

/**
 * Created by valery on 27.04.16.
 */
public abstract class AbstractFilterGenerator implements FilterGenerator {
    @Override
    public Container.Filter generateFilter(final Object propertyId, final Object value) {
        return null;
    }

    @Override
    public void filterRemoved(final Object propertyId) {

    }

    @Override
    public void filterAdded(final Object propertyId, final Class<? extends Container.Filter> filterType, final Object value) {

    }

    @Override
    public Container.Filter filterGeneratorFailed(final Exception reason, final Object propertyId, final Object value) {
        return null;
    }

    @Override
    public abstract Container.Filter generateFilter(Object propertyId, Field<?> originatingField);

    @Override
    public abstract AbstractField<?> getCustomFilterComponent(Object propertyId);
}
