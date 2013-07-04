package ru.extas.web.commons;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import com.vaadin.data.util.converter.Converter;

public class DateToJodaDTConverter implements Converter<Date, DateTime> {
	private static final long serialVersionUID = 6275343643194858906L;

	@Override
	public DateTime convertToModel(Date value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		return new DateTime(value);
	}

	@Override
	public Date convertToPresentation(DateTime value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		Date date = value.toDate();
		return date;
	}

	@Override
	public Class<DateTime> getModelType() {
		return DateTime.class;
	}

	@Override
	public Class<Date> getPresentationType() {
		return Date.class;
	}
}