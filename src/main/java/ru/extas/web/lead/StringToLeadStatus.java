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
 * @author Valery Orlov
 */
@Component
public class StringToLeadStatus implements Converter<String, Lead.Status> {

    private static final long serialVersionUID = 7270069509495749676L;

    private final BiMap<Lead.Status, String> map;

    public StringToLeadStatus() {
        map = HashBiMap.create();
        map.put(Lead.Status.NEW, "Новый");
        map.put(Lead.Status.QUALIFIED, "Квалифицирован");
        map.put(Lead.Status.CLOSED, "Закрыт");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Lead.Status> getModelType() {
        return Lead.Status.class;
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
    public Lead.Status convertToModel(final String value, final Class<? extends Lead.Status> targetType, final Locale locale)
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
    public String convertToPresentation(final Lead.Status value, final Class<? extends String> targetType, final Locale locale)
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
    public Lead.Status convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Lead.Status value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
