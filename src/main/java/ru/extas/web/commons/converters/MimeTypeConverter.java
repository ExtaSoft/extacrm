package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.Converter;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * <p>MimeTypeConverter class.</p>
 *
 * @author Valery Orlov
 *         Date: 18.04.2014
 *         Time: 17:39
 * @version $Id: $Id
 * @since 0.4.2
 */
@Component
public class MimeTypeConverter implements Converter<String, String> {
    /** {@inheritDoc} */
    @Override
    public String convertToModel(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
        if (value == null)
            return null;

        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String convertToPresentation(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
        if (value == null)
            return null;

        try {
            return MimeTypes.getDefaultMimeTypes().forName(value).getDescription();
        } catch (final MimeTypeException e) {
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
