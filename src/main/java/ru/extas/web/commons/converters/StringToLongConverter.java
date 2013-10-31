package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Valery Orlov
 *         Date: 19.08.13
 *         Time: 20:07
 */
@Component
public class StringToLongConverter extends AbstractStringToNumberConverter<Long> {
    @Override
    public Long convertToModel(final String value, final Class<? extends Long> targetType, final Locale locale) throws ConversionException {
        return (Long) convertToNumber(value, targetType, locale);
    }

    @Override
    public Class<Long> getModelType() {
        return Long.class;
    }
}
