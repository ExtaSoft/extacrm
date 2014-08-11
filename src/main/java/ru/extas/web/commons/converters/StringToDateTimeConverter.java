package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.StringToDateConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
* @author Valery Orlov
*         Date: 11.08.2014
*         Time: 15:28
*/
public class StringToDateTimeConverter extends StringToDateConverter {

    private final String pattern;

    public StringToDateTimeConverter(String pattern) {
        this.pattern = pattern;
    }

    public StringToDateTimeConverter() {
        this("dd.MM.yyyy HH:mm:ss");
    }

    @Override
    protected DateFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = lookup(Locale.class);
        }

        DateFormat f = new SimpleDateFormat(pattern, locale);
        return f;
    }
}
