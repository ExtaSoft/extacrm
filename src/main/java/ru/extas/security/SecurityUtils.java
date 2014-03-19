package ru.extas.security;


import static ru.extas.server.ServiceLocator.lookup;

/**
 * Вспомогательные методы при работе с правами доступа
 *
 * @author Valery Orlov
 *         Date: 05.03.14
 *         Time: 17:48
 */
public class SecurityUtils {

	public static ExtaDomain findDomainByClass(final Class entityClass) {
		return null;
	}

	public static EntitySecurityManager getSecurityManagerByClass(final Class entityClass) {

		final String serviceName = String.format("%sSecurityManager", entityClass.getSimpleName());
		EntitySecurityManager manager= lookup(serviceName, EntitySecurityManager.class);
		return manager;
	}

}
