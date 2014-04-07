/**
 *
 */
package ru.extas.security;

/**
 * Перечислитель ролей пользователей
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
    USER("user"),

    /**
	 * Дилер
	 */
	DEALER("dealer"),

    /**
     * Дилер реководитель
     */
	DEALER_MANAGER("dealer-manager"),

	/**
	 *  Дистрибьютор
	 */
	DISTRIBUTOR("distributor");


    /**
     * Возвращает строковое представление роли
     *
     * @return строковое представление роли
     */
    public String getName() {
        return name;
    }

    /**
     * <p>getRoleByName.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link ru.extas.security.UserRole} object.
     */
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
