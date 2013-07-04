package ru.extas.web.commons;

import java.util.Date;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.vaadin.data.util.converter.Converter;

public class DateToJodaLDConverter implements Converter<Date, LocalDate> {
	private static final long serialVersionUID = 6275343643194858906L;

	@Override
	public LocalDate convertToModel(Date value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		return new LocalDate(value);
	}

	@Override
	public Date convertToPresentation(LocalDate value, Locale locale) throws ConversionException {
		if (value == null)
			return null;
		Date date = value.toDate();
		return date;
	}

	@Override
	public Class<LocalDate> getModelType() {
		return LocalDate.class;
	}

	@Override
	public Class<Date> getPresentationType() {
		return Date.class;
	}
}