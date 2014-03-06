package ru.extas.model.converter;


import ru.extas.security.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Конвертер для ролей пользователей в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 */
@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(final UserRole attribute) {
        return attribute.getName();
    }

    @Override
    public UserRole convertToEntityAttribute(final String dbData) {
        return UserRole.getRoleByName(dbData);
    }
}
