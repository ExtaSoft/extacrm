/**
 * 
 */
package ru.extas.web.users;

import java.util.Locale;

import ru.extas.model.UserRole;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 * 
 * @author Valery Orlov
 * 
 */
public class StringToUserRoleConverter implements Converter<String, UserRole> {

	private static final long serialVersionUID = 568270351867767905L;

	private final BiMap<UserRole, String> map;

	public StringToUserRoleConverter() {
		map = HashBiMap.create();
		map.put(UserRole.USER, "Пользователь");
		map.put(UserRole.MANAGER, "Руководитель");
		map.put(UserRole.ADMIN, "Администратор");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<UserRole> getModelType() {
		return UserRole.class;
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
	@Override
	public UserRole convertToModel(final String value, final Class<? extends UserRole> targetType, final Locale locale)
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
	@Override
	public String convertToPresentation(final UserRole value, final Class<? extends String> targetType,
			final Locale locale)
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
	public UserRole convertToModel(final String value, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToModel(value, null, locale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.util.Locale)
	 */
	public String convertToPresentation(final UserRole value, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return convertToPresentation(value, null, locale);
	}

}
