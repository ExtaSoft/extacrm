package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Valery Orlov
 *         Date: 18.04.2014
 *         Time: 17:39
 */
@Component
public class MimeTypeConverter implements Converter<String, String> {
    @Override
    public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null)
            return null;

        return value;
    }

    @Override
    public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null)
            return null;

        try {
            return MimeTypes.getDefaultMimeTypes().forName(value).getDescription();
        } catch (MimeTypeException e) {
        }
        return value;
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
