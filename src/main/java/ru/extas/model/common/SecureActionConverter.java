package ru.extas.model.common;


import ru.extas.model.security.SecureAction;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для допустимых действий в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class SecureActionConverter implements AttributeConverter<SecureAction, String> {

    /** {@inheritDoc} */
    @Override
    public String convertToDatabaseColumn(final SecureAction attribute) {
        return attribute.getName();
    }

    /** {@inheritDoc} */
    @Override
    public SecureAction convertToEntityAttribute(final String dbData) {
        return SecureAction.getByName(dbData);
    }
}
