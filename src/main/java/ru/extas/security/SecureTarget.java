package ru.extas.security;

/**
 * Целевые объекты системы к которым предоставляется доступ
 */
public enum SecureTarget {

	/**
	 * Собственные объекты - видны только объекты принадлежащие пользователю.
	 */
	OWNONLY("ownonly"),
	/**
	 * Объекты относящиеся к той же торговой точке что и пользователь
	 */
	SALE_POINT("sale-point"),
	/**
	 * Корпоративные объекты - видны все "собственные" объекты пользователей принадлежащих
	 */
	CORPORATE("corporate"),
	/**
	 * Все объекты. Никаких ограничений - видимо только администраторам.
	 */
	ALL("*");

	SecureTarget(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static SecureTarget getByName(String name) {
		for (SecureTarget item : SecureTarget.values())
			if (item.getName().equals(name))
				return item;
		throw new IllegalArgumentException("There's no enum for name: " + name);
	}

	private final String name;
}
