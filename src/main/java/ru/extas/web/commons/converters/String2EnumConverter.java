package ru.extas.web.commons.converters;

import com.google.common.collect.BiMap;
import com.vaadin.data.util.converter.Converter;

import java.util.Locale;

/**
 * Базовый класс для создания конвертера пересисление - > описание
 *
 * @author Valery Orlov
 *         Date: 08.02.14
 *         Time: 10:35
 */
public abstract class String2EnumConverter<TEnum> implements Converter<String, TEnum> {
	protected final BiMap<TEnum, String> enum2StringMap;
	private final Class<TEnum> enumClass;

	protected abstract BiMap<TEnum, String> createEnum2StringMap();

	protected String2EnumConverter(Class<TEnum> enumClass) {
		this.enumClass = enumClass;
		enum2StringMap = createEnum2StringMap();
	}

	@Override
	public TEnum convertToModel(final String value, final Class<? extends TEnum> targetType, final Locale locale) throws ConversionException {
		if (value == null || value.isEmpty())
			return null;
		return enum2StringMap.inverse().get(value);
	}

	@Override
	public String convertToPresentation(final TEnum value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
		if (value == null)
			return null;
		return enum2StringMap.get(value);
	}

	@Override
	public Class<TEnum> getModelType() {
		return enumClass;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
