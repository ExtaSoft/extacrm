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
 * @author Valery Orlov
 */
@Component
public class StringToSaleType implements Converter<String, Sale.Type> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<Sale.Type, String> map;

    public StringToSaleType() {
        map = HashBiMap.create();
        map.put(Sale.Type.CREDIT, "Продажа кредита");
        map.put(Sale.Type.INSURANCE, "Продажа страховки");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Sale.Type> getModelType() {
        return Sale.Type.class;
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
    public Sale.Type convertToModel(final String value, final Class<? extends Sale.Type> targetType, final Locale locale)
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
    public String convertToPresentation(final Sale.Type value, final Class<? extends String> targetType, final Locale locale)
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
    public Sale.Type convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Sale.Type value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
