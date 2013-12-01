/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.Insurance;

import java.util.Locale;

/**
 * Конвертируем полис БСО в строку
 *
 * @author Valery Orlov
 */
@Component
public class StringToPeriodOfCoverConverter implements Converter<String, Insurance.PeriodOfCover> {

    private static final long serialVersionUID = 3362579681121638152L;

    private final BiMap<Insurance.PeriodOfCover, String> map;

    public StringToPeriodOfCoverConverter() {
        map = HashBiMap.create();
        map.put(Insurance.PeriodOfCover.YEAR, "Год");
        map.put(Insurance.PeriodOfCover.HALF_A_YEAR, "Полгода");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    @Override
    public Insurance.PeriodOfCover convertToModel(final String value, final Class<? extends Insurance.PeriodOfCover> targetType, final Locale locale)
            throws ConversionException {
        if (value == null || value.isEmpty())
            return Insurance.PeriodOfCover.YEAR;

        return map.inverse().get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.util.Locale)
     */
    @Override
    public String convertToPresentation(final Insurance.PeriodOfCover value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws ConversionException {
        if (value == null)
            return map.get(Insurance.PeriodOfCover.YEAR);
        return map.get(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<Insurance.PeriodOfCover> getModelType() {
        return Insurance.PeriodOfCover.class;
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
    public Insurance.PeriodOfCover convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final Insurance.PeriodOfCover value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
