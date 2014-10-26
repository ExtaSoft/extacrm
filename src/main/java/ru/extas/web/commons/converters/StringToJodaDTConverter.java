package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

/**
 * Обеспечивает преобразование между строкой и <code>org.joda.time.DateTime</code>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToJodaDTConverter implements Converter<String, DateTime> {
    private static final long serialVersionUID = 6275343643194858906L;

    @Inject @Named("clientTimeZone")
    private DateTimeZone clientTimeZone;

    @Inject
    private Locale locale;

    transient private DateTimeFormatter formatter;
    private String pattern;

    private DateTimeFormatter getFormatter() {
        if (formatter == null)
            formatter = DateTimeFormat.forPattern(pattern).withZone(clientTimeZone);
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    /** {@inheritDoc} */
    @Override
    public Class<DateTime> getModelType() {
        return DateTime.class;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public DateTime convertToModel(final String value, final Class<? extends DateTime> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null || value.isEmpty())
            return null;
        if (locale == null)
            locale = this.locale;
        return getFormatter().withLocale(locale).parseDateTime(value);
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final DateTime value, final Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;

        if (locale == null)
            locale = this.locale;

        return getFormatter().withLocale(locale).print(value);
    }

    /**
     * <p>convertToModel.</p>
     *
     * @param value  a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link org.joda.time.DateTime} object.
     * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
     */
    public DateTime convertToModel(final String value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToModel(value, null, locale);
    }

     /**
      * <p>convertToPresentation.</p>
      *
      * @param value  a {@link org.joda.time.DateTime} object.
      * @param locale a {@link java.util.Locale} object.
      * @return a {@link java.lang.String} object.
      * @throws com.vaadin.data.util.converter.Converter.ConversionException if any.
      */
    public String convertToPresentation(final DateTime value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }
}
