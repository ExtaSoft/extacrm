package ru.extas.web;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.extas.model.*;
import ru.extas.model.Person.Sex;
import ru.extas.web.commons.converters.*;
import ru.extas.web.contacts.StringToPersonPosition;
import ru.extas.web.contacts.StringToPersonSex;
import ru.extas.web.insurance.StringToA7FormConverter;
import ru.extas.web.insurance.StringToA7StatusConverter;
import ru.extas.web.insurance.StringToPolicyConverter;
import ru.extas.web.lead.StringToLeadStatus;
import ru.extas.web.sale.StringToSaleType;
import ru.extas.web.users.StringToUserRoleConverter;

import java.math.BigDecimal;
import java.util.Date;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Определяет кнверторы по умолчанию
 *
 * @author Valery Orlov
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

        // Конвертер должностей
        if (presentationType == String.class && modelType == Person.Position.class)
            return (Converter<PRESENTATION, MODEL>) lookup(StringToPersonPosition.class);

        // Конвертер статусов А-7
        if (presentationType == String.class && modelType == A7Form.Status.class)
            return (Converter<PRESENTATION, MODEL>) lookup(StringToA7StatusConverter.class);

        // Котвертер типа продажи
        if (presentationType == String.class && modelType == Sale.Type.class)
            return (Converter<PRESENTATION, MODEL>) lookup(StringToSaleType.class);

        // Конвертор статуса лида
        if (presentationType == String.class && modelType == Lead.Status.class)
            return (Converter<PRESENTATION, MODEL>) lookup(StringToLeadStatus.class);

        // Let default factory handle the rest
        return super.findConverter(presentationType, modelType);
    }
}