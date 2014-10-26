package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.StringToDateConverter;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
* @author Valery Orlov
*         Date: 11.08.2014
*         Time: 15:28
*/
@Component
public class StringToDateTimeConverter extends StringToDateConverter {

    @Inject
    private DateTimeZone clientTimeZone;

    @Inject
    private Locale locale;

    private String pattern;

    public StringToDateTimeConverter(final String pattern) {
        this.pattern = pattern;
    }

    public StringToDateTimeConverter() {
        this("dd.MM.yyyy HH:mm:ss");
    }

    @Override
    protected DateFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = this.locale;
        }

        final DateFormat f = new SimpleDateFormat(pattern, locale);
        f.setTimeZone(clientTimeZone.toTimeZone());
        return f;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
}
