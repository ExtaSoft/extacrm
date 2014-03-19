package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Обеспечивает преобразование между строкой и <code>org.joda.time.LocalDate</code>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToJodaLDConverter implements Converter<String, LocalDate> {
    private static final long serialVersionUID = 6275343643194858906L;

    transient private DateTimeFormatter formatter;
    private final String pattern;

    private DateTimeFormatter getFormatter() {
        if (formatter == null)
            formatter = DateTimeFormat.forPattern(pattern);
        return formatter;
    }

    /**
     * Преобразование между строкой и <code>org.joda.time.LocalDate</code> по
     * шаблону "dd.MM.yyyy hh:mm:ss"
     */
    public StringToJodaLDConverter() {
        this("dd.MM.yyyy");
    }

    /**
     * Преобразоваие между строкой и <code>org.joda.time.LocalDate</code> по
     * заданному шаблону
     *
     * @param pattern шаблон преобразования
     */
    public StringToJodaLDConverter(final String pattern) {
        this.pattern = pattern;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getModelType()
     */
    /** {@inheritDoc} */
    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.data.util.converter.Converter#getPresentationType()
     */
    /** {@inheritDoc} */
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
    /** {@inheritDoc} */
    @Override
    public LocalDate convertToModel(final String value, final Class<? extends LocalDate> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null || value.isEmpty())
            return null;
        if (locale == null)
            locale = Locale.getDefault();
        return getFormatter().withLocale(locale).parseLocalDate(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final LocalDate value, final Class<? extends String> targetType, Locale locale)
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
    /**
     * <p>convertToModel.</p>
     *
     * @param value a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link org.joda.time.LocalDate} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public LocalDate convertToModel(final String value, final Locale locale)
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
    /**
     * <p>convertToPresentation.</p>
     *
     * @param value a {@link org.joda.time.LocalDate} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws com.vaadin.data.util.converter.Converter$ConversionException if any.
     */
    public String convertToPresentation(final LocalDate value, final Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return convertToPresentation(value, null, locale);
    }

}
