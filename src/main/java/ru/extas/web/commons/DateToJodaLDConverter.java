package ru.extas.web.commons;

import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

public class DateToJodaLDConverter implements Converter<Date, LocalDate> {
	private static final long serialVersionUID = 6275343643194858906L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public LocalDate convertToModel(Date value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		return new LocalDate(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.util.Locale)
	 */
	@Override
	public Date convertToPresentation(LocalDate value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		Date date = value.toDate();
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<LocalDate> getModelType() {
		return LocalDate.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public Class<Date> getPresentationType() {
		return Date.class;
	}
}