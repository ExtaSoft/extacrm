package ru.extas.model.common;


import ru.extas.model.security.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для ролей пользователей в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    /** {@inheritDoc} */
    @Override
    public String convertToDatabaseColumn(final UserRole attribute) {
        return attribute.getName();
    }

    /** {@inheritDoc} */
    @Override
    public UserRole convertToEntityAttribute(final String dbData) {
        return UserRole.getRoleByName(dbData);
    }
}
