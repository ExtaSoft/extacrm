package ru.extas.model.security;


import ru.extas.model.security.SecureTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для целевых обхектов в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class SecureTargetConverter implements AttributeConverter<SecureTarget, String> {

    /** {@inheritDoc} */
    @Override
    public String convertToDatabaseColumn(final SecureTarget attribute) {
        return attribute.getName();
    }

    /** {@inheritDoc} */
    @Override
    public SecureTarget convertToEntityAttribute(final String dbData) {
        return SecureTarget.getByName(dbData);
    }
}
