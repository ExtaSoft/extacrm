/**
 *
 */
package ru.extas.model;

/**
 * Перечислитель ролей пользователей
 *
 * @author Valery Orlov
 */
public enum UserRole {

    /**
     * Администратор
     */
    ADMIN("admin"),

    /**
     * Руководитель
     */
    MANAGER("manager"),

    /**
     * Рядовой пользователь
     */
    USER("user");

    /**
     * Возвращает строковое представление роли
     *
     * @return строковое представление роли
     */
    public String getName() {
        return name;
    }

    public static UserRole getRoleByName(String name) {
        for (UserRole role : UserRole.values())
            if (role.getName().equals(name))
                return role;
        throw new IllegalArgumentException("There's no user role for name: " + name);
    }

    private UserRole(String name) {
        this.name = name;
    }

    private final String name;

}
