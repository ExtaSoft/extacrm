/**
 * 
 */
package ru.extas.web.contacts;

import java.util.Locale;

import ru.extas.model.Contact.Sex;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;

/**
 * @author Valery Orlov
 * 
 */
public class StringToContactSex implements Converter<String, Sex> {

	private static final long serialVersionUID = 7270069509495749676L;

	private final BiMap<Sex, String> map;

	public StringToContactSex() {
		map = HashBiMap.create();
		map.put(Sex.MALE, "Мужской");
		map.put(Sex.FEMALE, "Женский");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<Sex> getModelType() {
		return Sex.class;
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
	public Sex convertToModel(String value, Class<? extends Sex> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null || value.isEmpty())
			return null;
		return map.inverse().get(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	public String convertToPresentation(Sex value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return map.get(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public Sex convertToModel(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
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
	public String convertToPresentation(Sex value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToPresentation(value, null, locale);
	}

}
