/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.insurance.Policy;
import ru.extas.server.insurance.PolicyRepository;

import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Конвертируем полис БСО в строку
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToPolicyConverter implements Converter<String, Policy> {

    private static final long serialVersionUID = 3362579681121638152L;

    /** {@inheritDoc} */
    @Override
    public Policy convertToModel(final String value, final Class<? extends Policy> targetType, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return lookup(PolicyRepository.class).findByRegNum(value);
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final Policy value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return value.getRegNum();
    }

    /** {@inheritDoc} */
    @Override
    public Class<Policy> getModelType() {
        return Policy.class;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /**
     * <p>convertToModel.</p>
     *
     * @param value  a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link ru.extas.model.insurance.Policy} object.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     */
    public Policy convertToModel(final String value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToModel(value, null, locale);
    }

    /**
     * <p>convertToPresentation.</p>
     *
     * @param value  a {@link ru.extas.model.insurance.Policy} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     */
    public String convertToPresentation(final Policy value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
