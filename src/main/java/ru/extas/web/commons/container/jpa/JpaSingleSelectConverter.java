package ru.extas.web.commons.container.jpa;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import ru.extas.model.common.IdentifiedObject;

import java.util.Locale;

public class JpaSingleSelectConverter<TEntityType extends IdentifiedObject> implements Converter<Object, TEntityType> {

    private final AbstractSelect select;

    public JpaSingleSelectConverter(final AbstractSelect select) {
        this.select = select;
    }

    @SuppressWarnings("unchecked")
    private JpaJazyContainer<TEntityType> getContainer() {
        return (JpaJazyContainer<TEntityType>) select.getContainerDataSource();
    }

    @Override
    public TEntityType convertToModel(final Object value, final Class<? extends TEntityType> targetType,
            final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value != select.getNullSelectionItemId()) {
            final JpaEntityItem<TEntityType> item = (JpaEntityItem<TEntityType>) value;
            return item.getBean();
        } else {
            return null;
        }
    }

    @Override
    public Object convertToPresentation(final TEntityType value,
            final Class<? extends Object> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value != null) {
            return getContainer().getItemByEntity(value);
        }
        return select.getNullSelectionItemId();
    }

    @Override
    public Class<TEntityType> getModelType() {
        return getContainer().getEntityClass();
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }

}
