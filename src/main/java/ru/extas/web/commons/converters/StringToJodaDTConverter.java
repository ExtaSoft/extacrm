package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Обеспечивает преобразование между строкой и <code>org.joda.time.DateTime</code>
 *
 * @author Valery Orlov
 */
@Component
public class StringToJodaDTConverter implements Converter<String, DateTime> {
    private static final long serialVersionUID = 6275343643194858906L;

    transient private DateTimeFormatter formatter;
    private final String pattern;

    private DateTimeFormatter getFormatter() {
        if (formatter == null)
            formatter = DateTimeFormat.forPattern(pattern);
        return formatter;
    }

    /**
     * Преобразование между строкой и <code>org.joda.time.DateTime</code> по
     * шаблону "dd.MM.yyyy hh:mm:ss"
     */
    public StringToJodaDTConverter() {
        this("dd.MM.yyyy HH:mm:ss");
    }

    /**
     * Преобразоваие между строкой и <code>org.joda.time.DateTime</code> по
     * заданному шаблону
     *
     * @param pattern шаблон преобразования
     */
    public StringToJodaDTConverter(final String pattern) {
        this.pattern = pattern;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    @Override
    public Class<DateTime> getModelType() {
        return DateTime.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
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
    public DateTime convertToModel(final String value, final Class<? extends DateTime> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null || value.isEmpty())
            return null;
        if (locale == null)
            locale = Locale.getDefault();
        return getFormatter().withLocale(locale).parseDateTime(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    @Override
    public String convertToPresentation(final DateTime value, final Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;

        if (locale == null)
            locale = Locale.getDefault();

        return getFormatter().withLocale(locale).print(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
     * java.util.Locale)
     */
    public DateTime convertToModel(final String value, final Locale locale)
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
    public String convertToPresentation(final DateTime value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }
}