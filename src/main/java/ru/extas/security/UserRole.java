/**
 *
 */
package ru.extas.security;

/**
 * Перечислитель ролей пользователей
 *
 * @author Valery Orlov
 */
public enum UserRole {

	/**
	 * Рядовой пользователь
	 */
	USER("user"),

	/**
	 * Руководитель
	 */
	MANAGER("manager"),

	/**
     * Администратор
     */
    ADMIN("admin"),

	/**
	 * Дилер
	 */
	DEALER("dealer"),

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
