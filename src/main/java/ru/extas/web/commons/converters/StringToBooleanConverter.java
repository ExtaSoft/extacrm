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
 * @since 0.3
 */
@Component
public class StringToBooleanConverter implements Converter<String, Boolean> {

    private static final long serialVersionUID = -3047437045883938183L;

    /** {@inheritDoc} */
    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean convertToModel(final String value, final Class<? extends Boolean> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null || value.isEmpty())
            return null;
        return value.equals("Да");
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final Boolean value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return value ? "Да" : "Нет";
    }

    /**
     * <p>convertToModel.</p>
     *
     * @param value  a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.Boolean} object.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     */
    public Boolean convertToModel(final String value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToModel(value, null, locale);
    }

    /**
     * <p>convertToPresentation.</p>
     *
     * @param value  a {@link java.lang.Boolean} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     */
    public String convertToPresentation(final Boolean value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
