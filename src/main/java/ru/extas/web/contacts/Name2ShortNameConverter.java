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
    public String convertToModel(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
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
