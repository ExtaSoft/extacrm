/**
 * 
 */
package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Locale;

import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;

import com.vaadin.data.util.converter.Converter;

/**
 * Конвертируем полис БСО в строку
 * 
 * @author Valery Orlov
 * 
 */
public class StringToPolicyConverter implements Converter<String, Policy> {

	private static final long serialVersionUID = 3362579681121638152L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.util.Locale)
	 */
	@Override
	public Policy convertToModel(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return lookup(PolicyRegistry.class).findByNum(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.util.Locale)
	 */
	@Override
	public String convertToPresentation(Policy value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null)
			return null;
		return value.getRegNum();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<Policy> getModelType() {
		return Policy.class;
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

}
