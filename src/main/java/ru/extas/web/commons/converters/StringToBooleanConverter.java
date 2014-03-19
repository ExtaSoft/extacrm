/**
 *
 */
package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Простая конвертация флага в строку
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToBooleanConverter implements Converter<String, Boolean> {

    private static final long serialVersionUID = -3047437045883938183L;

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    /** {@inheritDoc} */
    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getPresentationType()
     */
    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.lang.Class, java.util.Locale)
     */
    /** {@inheritDoc} */
    @Override
    public Boolean convertToModel(final String value, final Class<? extends Boolean> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null || value.isEmpty())
            return null;
        return value.equals("Да");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final Boolean value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return value ? "Да" : "Нет";
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    /**
     * <p>convertToModel.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.Boolean} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public Boolean convertToModel(final String value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToModel(value, null, locale);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    /**
     * <p>convertToPresentation.</p>
     *
     * @param value a {@link java.lang.Boolean} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public String convertToPresentation(final Boolean value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
