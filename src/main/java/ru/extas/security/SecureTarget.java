package ru.extas.security;

/**
 * Целевые объекты системы к которым предоставляется доступ
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
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

	/**
	 * <p>Constructor for SecureTarget.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	SecureTarget(final String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>getByName.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link ru.extas.security.SecureTarget} object.
	 */
	public static SecureTarget getByName(String name) {
		for (SecureTarget item : SecureTarget.values())
			if (item.getName().equals(name))
				return item;
		throw new IllegalArgumentException("There's no enum for name: " + name);
	}

	private final String name;
}
