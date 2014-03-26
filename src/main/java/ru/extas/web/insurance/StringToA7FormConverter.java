/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.insurance.A7Form;
import ru.extas.server.insurance.A7FormRepository;

import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Конвертируем Квитанцию А-7 в строку (номер квитанции)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToA7FormConverter implements Converter<String, A7Form> {

    private static final long serialVersionUID = 3362579681121638152L;

    /** {@inheritDoc} */
    @Override
    public A7Form convertToModel(final String value, final Class<? extends A7Form> targetType, final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return lookup(A7FormRepository.class).findByRegNum(value);
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final A7Form value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return value.getRegNum();
    }

    /** {@inheritDoc} */
    @Override
    public Class<A7Form> getModelType() {
        return A7Form.class;
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
     * @return a {@link ru.extas.model.insurance.A7Form} object.
     * @throws ConversionException if any.
     */
    public A7Form convertToModel(final String value, final Locale locale)
            throws ConversionException {
        return convertToModel(value, null, locale);
    }

    /**
     * <p>convertToPresentation.</p>
     *
     * @param value  a {@link ru.extas.model.insurance.A7Form} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws ConversionException if any.
     */
    public String convertToPresentation(final A7Form value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
