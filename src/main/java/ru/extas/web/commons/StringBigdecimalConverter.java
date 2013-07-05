package ru.extas.web.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * Конвертер для отображения BigDecimal элементах UI
 * 
 * @author Valery Orlov
 * 
 */
public class StringBigdecimalConverter implements Converter<String, BigDecimal> {

	private static final long serialVersionUID = -7818477340305539184L;
	private transient DecimalFormat format;

	protected DecimalFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		// format.applyPattern("#,##0.00");
		format.setParseBigDecimal(true);
		format.setRoundingMode(RoundingMode.HALF_UP);
		return format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public BigDecimal convertToModel(String value, Locale locale) throws ConversionException {
		if (value == null || value.isEmpty()) {
			return null;
		}

		// Remove leading and trailing white space
		value = value.trim();

		// Parse and detect errors. If the full string was not used, it is
		// an error.
		ParsePosition parsePosition = new ParsePosition(0);
		BigDecimal parsedValue = (BigDecimal) getFormat(locale).parse(value, parsePosition);
		if (parsePosition.getIndex() != value.length()) {
			throw new ConversionException("Could not convert '" + value + "' to " + getModelType().getName());
		}

		if (parsedValue == null) {
			// Convert "" to null
			return null;
		}
		return parsedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.util.Locale)
	 */
	@Override
	public String convertToPresentation(BigDecimal value, Locale locale) throws ConversionException {
		if (value == null) {
			return null;
		}

		return getFormat(locale).format(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<BigDecimal> getModelType() {
		return BigDecimal.class;
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
}
