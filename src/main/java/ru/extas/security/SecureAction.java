package ru.extas.security;

/**
 * Действие разрешаемое пользователю
 */
public enum SecureAction {
	/**
	 * Чтение/просмотр
	 */
	VIEW("view"),
	/**
	 * Редактирование
	 */
	EDIT("edit"),
	/**
	 * Ввод
	 */
	INSERT("insert"),
	/**
	 * Удаление
	 */
	DELETE("delete"),
	/**
	 * Полный доступ
	 */
	ALL("*");


	SecureAction(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static SecureAction getByName(String name) {
		for (SecureAction item : SecureAction.values())
			if (item.getName().equals(name))
				return item;
		throw new IllegalArgumentException("There's no enum for name: " + name);
	}

	private final String name;
}
