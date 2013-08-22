/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.converter.Converter;
import ru.extas.model.A7Form;
import ru.extas.server.A7FormService;

import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Конвертируем Квитанцию А-7 в строку (номер квитанции)
 *
 * @author Valery Orlov
 */
public class StringToA7FormConverter implements Converter<String, A7Form> {

    private static final long serialVersionUID = 3362579681121638152L;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    @Override
    public A7Form convertToModel(final String value, final Class<? extends A7Form> targetType, final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return lookup(A7FormService.class).findByNum(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    @Override
    public String convertToPresentation(final A7Form value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return value.getRegNum();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<A7Form> getModelType() {
        return A7Form.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getPresentationType()
     */
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
    public A7Form convertToModel(final String value, final Locale locale)
            throws ConversionException {
        return convertToModel(value, null, locale);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    public String convertToPresentation(final A7Form value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
