package ru.extas.vaadin.addon.jdocontainer;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import ru.extas.model.AbstractExtaObject;

import java.util.Locale;

public class SingleSelectConverter<T extends AbstractExtaObject> implements Converter<Object, T> {

    private static final long serialVersionUID = -4442179362842630869L;

    private final AbstractSelect select;

    private final LazyJdoContainer<T> container;

    public SingleSelectConverter(final AbstractSelect select, final LazyJdoContainer<T> container) {
        this.select = select;
        this.container = container;
    }

    T convertToModel(final Object value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value != select.getNullSelectionItemId()) {
            return container.getJdoEntity(value);
        } else {
            return null;
        }
    }

    Object convertToPresentation(final T value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value != null)
            return 0;
        return select.getNullSelectionItemId();
    }

    @Override
    public Class<T> getModelType() {
        return container.getEntityClass();
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object, java.lang.Class, java.util.Locale)
     */
    @Override
    public T convertToModel(final Object value, final Class<? extends T> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToModel(value, locale);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang.Object, java.lang.Class,
     * java.util.Locale)
     */
    @Override
    public Object convertToPresentation(final T value, final Class<?> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, locale);
    }

}
