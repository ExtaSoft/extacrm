package ru.extas.security;


import static ru.extas.server.ServiceLocator.lookup;

/**
 * Вспомогательные методы при работе с правами доступа
 *
 * @author Valery Orlov
 *         Date: 05.03.14
 *         Time: 17:48
 * @version $Id: $Id
 * @since 0.3
 */
public class SecurityUtils {

	/**
	 * <p>findDomainByClass.</p>
	 *
	 * @param entityClass a {@link java.lang.Class} object.
	 * @return a {@link ru.extas.security.ExtaDomain} object.
	 */
	public static ExtaDomain findDomainByClass(final Class entityClass) {
		return null;
	}

	/**
	 * <p>getSecurityManagerByClass.</p>
	 *
	 * @param entityClass a {@link java.lang.Class} object.
	 * @return a {@link ru.extas.security.EntitySecurityManager} object.
	 */
	public static EntitySecurityManager getSecurityManagerByClass(final Class entityClass) {

		final String serviceName = String.format("%sSecurityManager", entityClass.getSimpleName());
		EntitySecurityManager manager= lookup(serviceName, EntitySecurityManager.class);
		return manager;
	}

}
