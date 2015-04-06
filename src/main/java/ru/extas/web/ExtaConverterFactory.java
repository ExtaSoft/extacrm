package ru.extas.web;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.extas.model.contacts.*;
import ru.extas.model.contacts.Person.Sex;
import ru.extas.model.insurance.A7Form;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.insurance.Policy;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProductInSale;
import ru.extas.model.sale.Sale;
import ru.extas.model.security.*;
import ru.extas.web.commons.converters.*;
import ru.extas.web.contacts.employee.StringToTypeOfEmployment;
import ru.extas.web.contacts.person.*;
import ru.extas.web.insurance.StringToA7FormConverter;
import ru.extas.web.insurance.StringToA7StatusConverter;
import ru.extas.web.insurance.StringToPeriodOfCoverConverter;
import ru.extas.web.insurance.StringToPolicyConverter;
import ru.extas.web.lead.StringToLeadResult;
import ru.extas.web.lead.StringToLeadStatus;
import ru.extas.web.product.String2CreditProgramType;
import ru.extas.web.product.String2ProdInSaleState;
import ru.extas.web.sale.StringToSaleCancelReason;
import ru.extas.web.users.*;

import java.math.BigDecimal;
import java.util.Date;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Определяет кнверторы по умолчанию
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaConverterFactory extends DefaultConverterFactory {
	private static final long serialVersionUID = -1489942684787774107L;

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(final Class<PRESENTATION> presentationType, final Class<MODEL> modelType) {

		// Конверторы дат
		if (presentationType == Date.class && modelType == DateTime.class)
			return (Converter<PRESENTATION, MODEL>) lookup(DateToJodaDTConverter.class);
		if (presentationType == Date.class && modelType == LocalDate.class)
			return (Converter<PRESENTATION, MODEL>) lookup(DateToJodaLDConverter.class);
		if (presentationType == String.class && modelType == DateTime.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToJodaDTConverter.class);
		if (presentationType == String.class && modelType == LocalDate.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToJodaLDConverter.class);
		if (presentationType == String.class && modelType == Date.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToDateTimeConverter.class);

		// Конвертер денег
		if (presentationType == String.class && modelType == BigDecimal.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToMoneyConverter.class);

		// конвертер ролей пользователя
		if (presentationType == String.class && modelType == UserRole.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToUserRoleConverter.class);

		// конвертер ролей доступа к объектам
		if (presentationType == String.class && modelType == AccessRole.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToAccessRoleConverter.class);

		// Конвертер половой принадлежности
		if (presentationType == String.class && modelType == Sex.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToPersonSex.class);

		// Простой конвертор флага
		if (presentationType == String.class && modelType == Boolean.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToBooleanConverter.class);

		// Конвертер полиса БСО
		if (presentationType == String.class && modelType == Policy.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToPolicyConverter.class);

		// Конвертер строк в длинное целое
		if (presentationType == String.class && modelType == Long.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToLongConverter.class);

		// Конвертируем Квитанцию А-7 в строку (номер квитанции)
		if (presentationType == String.class && modelType == A7Form.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToA7FormConverter.class);

		// Конвертер статусов А-7
		if (presentationType == String.class && modelType == A7Form.Status.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToA7StatusConverter.class);

		// Конвертор результата завершения Продажи
		if (presentationType == String.class && modelType == Sale.CancelReason.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToSaleCancelReason.class);

		// Конвертор статуса лида
		if (presentationType == String.class && modelType == Lead.Status.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToLeadStatus.class);

		// Конвертор результата завершения лида
		if (presentationType == String.class && modelType == Lead.Result.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToLeadResult.class);

		// Конвертер периода страхования
		if (presentationType == String.class && modelType == Insurance.PeriodOfCover.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToPeriodOfCoverConverter.class);

		// Конвертер типов кредитных программ
		if (presentationType == String.class && modelType == ProdCredit.ProgramType.class)
			return (Converter<PRESENTATION, MODEL>) lookup(String2CreditProgramType.class);

		// Конвертер разделов системы
		if (presentationType == String.class && modelType == ExtaDomain.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToExtaDomainConverter.class);

		// Конвертер разрешенных действий в рамках правила доступа
		if (presentationType == String.class && modelType == SecureAction.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToSecureActionConverter.class);

		// Конвертер целевых объектов правила доступа
		if (presentationType == String.class && modelType == SecureTarget.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToSecureTargetConverter.class);

		// Конвертер отношение к объекту недвижимости
		if (presentationType == String.class && modelType == RealtyKind.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToRealtyKind.class);

		// Конвертер срока проживания
		if (presentationType == String.class && modelType == PeriodOfResidence.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToPeriodOfResidence.class);

		// Конвертер семейного положения
		if (presentationType == String.class && modelType == MaritalStatus.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToMaritalStatus.class);

		// Конвертер уровня образования
		if (presentationType == String.class && modelType == EducationKind.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToEducationKind.class);

		// Конвертер уровня образования
		if (presentationType == String.class && modelType == TypeOfEmployment.class)
			return (Converter<PRESENTATION, MODEL>) lookup(StringToTypeOfEmployment.class);

		// Конвертер статуса продукта в продаже
		if (presentationType == String.class && modelType == ProductInSale.State.class)
			return (Converter<PRESENTATION, MODEL>) lookup(String2ProdInSaleState.class);

		// Let default factory handle the rest
		return super.findConverter(presentationType, modelType);
	}
}
