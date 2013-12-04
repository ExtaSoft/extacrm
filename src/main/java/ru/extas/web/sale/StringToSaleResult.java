/**
 *
 */
package ru.extas.web.sale;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.Sale;

import java.util.Locale;

/**
 * Конвертируем результат завершения продажи в текстовое представление
 *
 * @author Valery Orlov
 */
@Component
public class StringToSaleResult implements Converter<String, Sale.Result> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<Sale.Result, String> map;

    public StringToSaleResult() {
        map = HashBiMap.create();
        map.put(Sale.Result.SUCCESSFUL, "Успешное выполнение");
        map.put(Sale.Result.CLIENT_REJECTED, "Отказ клиента");
        map.put(Sale.Result.VENDOR_REJECTED, "Отказ контрагента");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Sale.Result> getModelType() {
        return Sale.Result.class;
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
    public Sale.Result convertToModel(final String value, final Class<? extends Sale.Result> targetType, final Locale locale)
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
    public String convertToPresentation(final Sale.Result value, final Class<? extends String> targetType, final Locale locale)
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
    public Sale.Result convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Sale.Result value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
