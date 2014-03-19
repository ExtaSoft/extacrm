package ru.extas.web.commons.converters;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * <p>StringToLongConverter class.</p>
 *
 * @author Valery Orlov
 *         Date: 19.08.13
 *         Time: 20:07
 * @version $Id: $Id
 */
@Component
public class StringToLongConverter extends AbstractStringToNumberConverter<Long> {
    /** {@inheritDoc} */
    @Override
    public Long convertToModel(final String value, final Class<? extends Long> targetType, final Locale locale) throws ConversionException {
        return (Long) convertToNumber(value, targetType, locale);
    }

    /** {@inheritDoc} */
    @Override
    public Class<Long> getModelType() {
        return Long.class;
    }
}
