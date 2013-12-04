/**
 *
 */
package ru.extas.web.lead;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.Lead;

import java.util.Locale;

/**
 * Конвертируем результат завершения лида в текстовое представление
 *
 * @author Valery Orlov
 */
@Component
public class StringToLeadResult implements Converter<String, Lead.Result> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<Lead.Result, String> map;

    public StringToLeadResult() {
        map = HashBiMap.create();
        map.put(Lead.Result.SUCCESSFUL, "Успешное выполнение");
        map.put(Lead.Result.CLIENT_REJECTED, "Отказ клиента");
        map.put(Lead.Result.VENDOR_REJECTED, "Отказ контрагента");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Lead.Result> getModelType() {
        return Lead.Result.class;
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
    public Lead.Result convertToModel(final String value, final Class<? extends Lead.Result> targetType, final Locale locale)
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
    public String convertToPresentation(final Lead.Result value, final Class<? extends String> targetType, final Locale locale)
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
    public Lead.Result convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Lead.Result value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
