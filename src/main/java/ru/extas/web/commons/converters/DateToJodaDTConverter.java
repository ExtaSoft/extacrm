package ru.extas.web.commons.converters;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import com.vaadin.data.util.converter.Converter;

public class DateToJodaDTConverter implements Converter<Date, DateTime> {
	private static final long serialVersionUID = 6275343643194858906L;

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
	@Override
	public DateTime convertToModel(Date value,/*
											 * Class<? extends DateTime>
											 * targetType,
											 */Locale locale) throws ConversionException {
		if (value == null)
			return null;
		return new DateTime(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	@Override
	public Date convertToPresentation(DateTime value, /*
													 * Class<? extends Date>
													 * targetType,
													 */Locale locale) throws ConversionException {
		if (value == null)
			return null;
		Date date = value.toDate();
		return date;
	}
}