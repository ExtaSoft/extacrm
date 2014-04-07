package ru.extas.security;

import org.apache.shiro.authz.Permission;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Правао доступа к обхектам CRM
 *
 * @author Valery Orlov
 *         Date: 03.03.14
 *         Time: 18:13
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaPermission implements Permission, Serializable {

    private final ExtaDomain domain;
    private final Set<SecureAction> actions;
    private final SecureTarget target;

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
     * @param domain  раздел системы
     * @param actions разрешенные действия, null чтобы разрешить все
     * @param target  целевые объекты, null чтобы разрешить все
     */
    public ExtaPermission(ExtaDomain domain, Set<SecureAction> actions, SecureTarget target) {
        checkNotNull(domain);
        this.domain = domain;
        this.actions = actions;
        this.target = target;
    }

    /**
     * Создает право доступа.
     * Определяет ЛЮБЫЕ действия над ЛЮБЫМИ целевыми объектами в заданном разделе системы.
     *
     * @param domain раздел системы
     */
    public ExtaPermission(final ExtaDomain domain) {
        this(domain, (SecureAction) null, null);
    }

    /**
     * Создает право доступа.
     * Определяет ЛЮБЫЕ действия над целевыми объектами в заданном разделе системы.
     *
     * @param domain раздел системы
     * @param target целевые объекты
     */
    public ExtaPermission(final ExtaDomain domain, final SecureTarget target) {
        this(domain, (SecureAction) null, target);
    }

    @Override
    public boolean implies(Permission p) {
        checkNotNull(p);
        if (!(p instanceof ExtaPermission)) {
            return false;
        }

        ExtaPermission perm = (ExtaPermission) p;

        if (domain == perm.getDomain()) {
            if (perm.getTarget() == null || target == perm.getTarget()) {
                if (isEmpty(perm.getActions())
                        || actions.contains(SecureAction.ALL)
                        || actions.containsAll(perm.getActions())) {
                    return true;
                }
            }
        }

        return false;
    }

    public ExtaDomain getDomain() {
        return domain;
    }

    public Set<SecureAction> getActions() {
        return actions;
    }

    public SecureTarget getTarget() {
        return target;
    }
}
