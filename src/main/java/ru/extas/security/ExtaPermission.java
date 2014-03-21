package ru.extas.security;

import com.google.common.base.Function;
import org.apache.shiro.authz.permission.DomainPermission;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Правао доступа к обхектам CRM
 *
 * @author Valery Orlov
 *         Date: 03.03.14
 *         Time: 18:13
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaPermission extends DomainPermission {

	/**
	 * Создает право доступа.
	 * Разрешает действие над определенными целевыми объектами в заданном разделе системы.
	 *
	 * @param domain раздел системы
	 * @param action разрешенное действие, null чтобы разрешить все
	 * @param target целевые объекты, null чтобы разрешить все
	 */
	public ExtaPermission(ExtaDomain domain, SecureAction action, SecureTarget target) {
		this(domain, action == null ? null : newHashSet(action), target);
	}

	/**
	 * Создает право доступа.
	 * Разрешает набор действий над определенными целевыми объектами в заданном разделе системы.
	 *
	 * @param domain раздел системы
	 * @param actions разрешенные действия, null чтобы разрешить все
	 * @param target целевые объекты, null чтобы разрешить все
	 */
	public ExtaPermission(ExtaDomain domain, Set<SecureAction> actions, SecureTarget target) {
		checkNotNull(domain);

		// Раздел
		setDomain(domain.getName());

		// Разрешенные действия
		if (actions != null && !actions.isEmpty())
			setActions(newHashSet(transform(actions, new Function<SecureAction, String>() {
				@Override
				public String apply(final SecureAction input) {
					return input.getName();
				}
			})));

		// Целевые объекты
		if (target != null)
			setTargets(newHashSet(target.getName()));
	}

	/**
	 * Создает право доступа.
	 * Определяет ЛЮБЫЕ действия над ЛЮБЫМИ целевыми объектами в заданном разделе системы.
	 *
	 * @param domain раздел системы
	 */
	public ExtaPermission(final ExtaDomain domain) {
		this(domain, (SecureAction)null, null);
	}

	/**
	 * Создает право доступа.
	 * Определяет ЛЮБЫЕ действия над целевыми объектами в заданном разделе системы.
	 *
	 * @param domain раздел системы
	 * @param target целевые объекты
	 */
	public ExtaPermission(final ExtaDomain domain, final SecureTarget target) {
		this(domain, (SecureAction)null, target);
	}
}
