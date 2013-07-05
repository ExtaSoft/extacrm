package ru.extas.web;

import static ru.extas.server.ServiceLocator.lookup;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import ru.extas.model.Contact.Sex;
import ru.extas.model.UserRole;
import ru.extas.web.commons.DateToJodaDTConverter;
import ru.extas.web.commons.DateToJodaLDConverter;
import ru.extas.web.commons.StringBigdecimalConverter;
import ru.extas.web.commons.StringToBooleanConverter;
import ru.extas.web.commons.StringToJodaDTConverter;
import ru.extas.web.commons.StringToJodaLDConverter;
import ru.extas.web.contacts.StringToContactSex;
import ru.extas.web.users.StringToUserRoleConverter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

/**
 * Определяет кнверторы по умолчанию
 * 
 * @author Valery Orlov
 * 
 */
public class ExtaConverterFactory extends DefaultConverterFactory {
	private static final long serialVersionUID = -1489942684787774107L;

	@SuppressWarnings("unchecked")
	@Override
	protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {

		// Конверторы дат
		if (presentationType == Date.class && modelType == DateTime.class)
			return (Converter<PRESENTATION, MODEL>) lookup(DateToJodaDTConverter.class);
		if (presentationType == Date.class && modelType == LocalDate.class)
			return (Converter<PRESENTATION, MODEL>) lookup(DateToJodaLDConverter.class);
		if (presentationType == String.class && modelType == DateTime.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToJodaDTConverter.class);
		if (presentationType == String.class && modelType == LocalDate.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToJodaLDConverter.class);

		// Конвертер денег
		if (presentationType == String.class && modelType == BigDecimal.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringBigdecimalConverter.class);

		// конвертер ролей пользователя
		if (presentationType == String.class && modelType == UserRole.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToUserRoleConverter.class);

		// Конвертер половой принадлежности
		if (presentationType == String.class && modelType == Sex.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToContactSex.class);

		if (presentationType == String.class && modelType == Boolean.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToBooleanConverter.class);
		// Let default factory handle the rest
		return super.findConverter(presentationType, modelType);
	}
}