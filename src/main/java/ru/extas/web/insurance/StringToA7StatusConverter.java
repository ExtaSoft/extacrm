/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import ru.extas.model.A7Form;

import java.util.Locale;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 *
 * @author Valery Orlov
 */
public class StringToA7StatusConverter implements Converter<String, A7Form.Status> {

    private static final long serialVersionUID = 568270351867767905L;

    private final BiMap<A7Form.Status, String> map;

    public StringToA7StatusConverter() {
        map = HashBiMap.create();
        map.put(A7Form.Status.NEW, "Новый бланк");
        map.put(A7Form.Status.SPENT, "Использованный бланк");
        map.put(A7Form.Status.LOST, "Потерянный бланк");
        map.put(A7Form.Status.BROKEN, "Испорченный бланк");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<A7Form.Status> getModelType() {
        return A7Form.Status.class;
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
     * java.lang.Class, java.util.Locale)
     */
    @Override
    public A7Form.Status convertToModel(final String value, final Class<? extends A7Form.Status> targetType, final Locale locale)
            throws ConversionException {
        if (value == null || value.isEmpty())
            return null;

        return map.inverse().get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    @Override
    public String convertToPresentation(final A7Form.Status value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws ConversionException {
        if (value == null)
            return null;
        return map.get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    public A7Form.Status convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final A7Form.Status value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
