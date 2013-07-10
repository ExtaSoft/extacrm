package ru.extas.web;

import static ru.extas.server.ServiceLocator.lookup;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import ru.extas.model.Contact.Sex;
import ru.extas.model.Policy;
import ru.extas.model.UserRole;
import ru.extas.web.commons.converters.DateToJodaDTConverter;
import ru.extas.web.commons.converters.DateToJodaLDConverter;
import ru.extas.web.commons.converters.StringToBooleanConverter;
import ru.extas.web.commons.converters.StringToJodaDTConverter;
import ru.extas.web.commons.converters.StringToJodaLDConverter;
import ru.extas.web.commons.converters.StringToMoneyConverter;
import ru.extas.web.contacts.StringToContactSex;
import ru.extas.web.insurance.StringToPolicyConverter;
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
			return (Converter<PRESENTATION, MODEL>) lookup(StringToMoneyConverter.class);

		// конвертер ролей пользователя
		if (presentationType == String.class && modelType == UserRole.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToUserRoleConverter.class);

		// Конвертер половой принадлежности
		if (presentationType == String.class && modelType == Sex.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToContactSex.class);

		// Простой конвертор флага
		if (presentationType == String.class && modelType == Boolean.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToBooleanConverter.class);

		// Конвертер полиса БСО
		if (presentationType == String.class && modelType == Policy.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToPolicyConverter.class);

		// Let default factory handle the rest
		return super.findConverter(presentationType, modelType);
	}
}