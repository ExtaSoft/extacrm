package ru.extas.web.commons.converters;

import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

public class DateToJodaLDConverter implements Converter<Date, LocalDate> {
	private static final long serialVersionUID = 6275343643194858906L;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.lang.Class, java.util.Locale)
	 */
	public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return new LocalDate(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		Date date = value.toDate();
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public LocalDate convertToModel(Date value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToModel(value, null, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.util.Locale)
	 */
	@Override
	public Date convertToPresentation(LocalDate value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToPresentation(value, null, locale);
	}
}