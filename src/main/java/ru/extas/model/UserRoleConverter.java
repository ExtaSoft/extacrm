package ru.extas.model;


import javax.persistence.AttributeConverter;

/**
 * Конвертер для ролей пользователей в БД
 * <p/>
 * Date: 09.09.13
 * Time: 14:37
 *
 * @author Valery Orlov
 */
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
