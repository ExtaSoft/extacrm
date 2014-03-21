package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Конвертер для отображения BigDecimal процентов в элементах UI
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToPercentConverter implements Converter<String, BigDecimal> {

    private static final long serialVersionUID = -7818477340305539184L;
    private transient DecimalFormat format;
    private final static Logger logger = LoggerFactory.getLogger(StringToPercentConverter.class);

    @Inject
    Locale _locale;

    /**
     * DecimalFormat's percent symbol
     */
    private static final String PERCENT_SYMBOL = "%";

    DecimalFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = _locale;
        }
        if (format == null) {
            format = (DecimalFormat) NumberFormat.getPercentInstance(locale);
            format.setParseBigDecimal(true);
            //format.setRoundingMode(RoundingMode.HALF_UP);
            //format.setMultiplier(1);
            format.setMaximumFractionDigits(2);
            //format.setMinimumFractionDigits(2);
            //format.setParseIntegerOnly(false);
            logger.debug("Percent format pattern {}", format.toPattern());
        }
        return format;
    }

    /** {@inheritDoc} */
    @Override
    public Class<BigDecimal> getModelType() {
        return BigDecimal.class;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal convertToModel(String value, final Class<? extends BigDecimal> targetType, final Locale locale)
            throws ConversionException {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Remove leading and trailing white space
        value = value.trim();

        // Parse and detect errors. If the full string was not used, it is
        // an error.
        BigDecimal parsedValue = (BigDecimal) getFormat(locale).parse(value, new ParsePosition(0));
        if (parsedValue == null) {
            parsedValue = (BigDecimal) getFormat(locale).parse(value + PERCENT_SYMBOL, new ParsePosition(0));
        }
        if (parsedValue == null) {
            throw new ConversionException(MessageFormat.format(
                    "Значение '{0}' не является допустимым числом", value));
        }

        return parsedValue;
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final BigDecimal value, final Class<? extends String> targetType,
                                        final Locale locale)
            throws ConversionException {
        if (value == null) {
            return null;
        }

        return getFormat(locale).format(value);
    }

    /**
     * <p>convertToModel.</p>
     *
     * @param value  a {@link java.lang.String} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.math.BigDecimal} object.
     * @throws ConversionException if any.
     */
    public BigDecimal convertToModel(final String value, final Locale locale)
            throws ConversionException {
        return convertToModel(value, null, locale);
    }

    /**
     * <p>convertToPresentation.</p>
     *
     * @param value  a {@link java.math.BigDecimal} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     * @throws ConversionException if any.
     */
    public String convertToPresentation(final BigDecimal value, final Locale locale)
            throws ConversionException {
        return convertToPresentation(value, null, locale);
    }
}
