/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;

import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Конвертируем полис БСО в строку
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToPolicyConverter implements Converter<String, Policy> {

    private static final long serialVersionUID = 3362579681121638152L;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    /** {@inheritDoc} */
    @Override
    public Policy convertToModel(final String value, final Class<? extends Policy> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
	    return lookup(PolicyRegistry.class).findByRegNum(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final Policy value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return value.getRegNum();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    /** {@inheritDoc} */
    @Override
    public Class<Policy> getModelType() {
        return Policy.class;
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
     * java.util.Locale)
     */
    /**
     * <p>convertToModel.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link ru.extas.model.Policy} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public Policy convertToModel(final String value, final Locale locale)
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
     * @param value a {@link ru.extas.model.Policy} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public String convertToPresentation(final Policy value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
