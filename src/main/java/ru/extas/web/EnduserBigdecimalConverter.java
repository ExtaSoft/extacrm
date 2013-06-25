package ru.extas.web;

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
public class EnduserBigdecimalConverter implements Converter<String, BigDecimal> {

	private static final long serialVersionUID = -7818477340305539184L;

	protected DecimalFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		DecimalFormat dc = (DecimalFormat) NumberFormat.getInstance(locale);
		dc.applyPattern("#,##0.00");
		dc.setParseBigDecimal(true);
		dc.setRoundingMode(RoundingMode.HALF_UP);
		return dc;
	}

	@Override
	public BigDecimal convertToModel(String value, Locale locale) throws ConversionException {
		if (value == null) {
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

	@Override
	public String convertToPresentation(BigDecimal value, Locale locale) throws ConversionException {
		if (value == null) {
			return null;
		}

		return getFormat(locale).format(value);
	}

	@Override
	public Class<BigDecimal> getModelType() {
		return BigDecimal.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
