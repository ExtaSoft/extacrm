package ru.extas.model.common;



import ru.extas.model.security.ExtaDomain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для подсистем пользователя в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class ExtaDomainConverter implements AttributeConverter<ExtaDomain, String> {

    /** {@inheritDoc} */
    @Override
    public String convertToDatabaseColumn(final ExtaDomain attribute) {
        return attribute.getName();
    }

    /** {@inheritDoc} */
    @Override
    public ExtaDomain convertToEntityAttribute(final String dbData) {
        return ExtaDomain.getByName(dbData);
    }
}
