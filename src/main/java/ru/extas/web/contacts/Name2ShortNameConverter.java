package ru.extas.web.contacts;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Конвертирует полное имя в короткую форму (без отчества)
 * @author Valery Orlov
 *         Date: 12.11.2014
 *         Time: 1:55
 */
@Component
public class Name2ShortNameConverter implements Converter<String, String> {
    @Override
    public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return NameUtils.getShortName(value);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
