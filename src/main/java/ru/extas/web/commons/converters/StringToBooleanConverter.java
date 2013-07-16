/**
 * 
 */
package ru.extas.web.commons.converters;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * Простая конвертация флага в строку
 * 
 * @author Valery Orlov
 * 
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {

	private static final long serialVersionUID = -3047437045883938183L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<Boolean> getModelType() {
		return Boolean.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getPresentationType()
	 */
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
	public Boolean convertToModel(String value, Class<? extends Boolean> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null || value.isEmpty())
			return null;
		return value.equals("Да");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	public String convertToPresentation(Boolean value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return value ? "Да" : "Нет";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public Boolean convertToModel(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
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
	public String convertToPresentation(Boolean value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToPresentation(value, null, locale);
	}

}
