package ru.extas.security;

import org.apache.shiro.authz.Permission;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.ExtaPermission;
import ru.extas.model.security.SecureAction;
import ru.extas.model.security.SecureTarget;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <p>DefaultRolePermissoin class.</p>
 *
 * @author Valery Orlov
 *         Date: 04.03.14
 *         Time: 0:22
 * @version $Id: $Id
 * @since 0.3
 */
public class DefaultRolePermissoin {
	/**
	 * <p>createAdminPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createAdminPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
        permissions.addAll(EnumSet.allOf(ExtaDomain.class).stream().map(domain -> new ExtaPermission(domain, SecureAction.ALL, SecureTarget.ALL)).collect(java.util.stream.Collectors.toList()));
		return permissions;
	}

}
