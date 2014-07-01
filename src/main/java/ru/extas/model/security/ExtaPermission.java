package ru.extas.model.security;

import org.apache.shiro.authz.Permission;
import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Права доступа к обхектам CRM
 *
 * @author Valery Orlov
 *         Date: 03.03.14
 *         Time: 18:13
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "ACCESS_PERMISSION")
public class ExtaPermission extends AuditedObject implements Permission, Cloneable {

    @Column(name = "DOMAIN")
    private ExtaDomain domain;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ACCESS_PERMISSION_ACTION")
    private Set<SecureAction> actions = newHashSet();

    @Column(name = "TARGET")
    private SecureTarget target;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private UserProfile user;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private UserGroup group;


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

    public ExtaPermission() {
    }

    @Override
    public ExtaPermission clone() {
        ExtaPermission clone = new ExtaPermission();

        clone.setDomain(getDomain());
        clone.setActions(EnumSet.copyOf(actions));
        clone.setTarget(getTarget());

        return clone;
    }

    /** {@inheritDoc} */
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

    /**
     * <p>Getter for the field <code>domain</code>.</p>
     *
     * @return a {@link ExtaDomain} object.
     */
    public ExtaDomain getDomain() {
        return domain;
    }

    /**
     * <p>Getter for the field <code>actions</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<SecureAction> getActions() {
        return actions;
    }

    /**
     * <p>Getter for the field <code>target</code>.</p>
     *
     * @return a {@link SecureTarget} object.
     */
    public SecureTarget getTarget() {
        return target;
    }

    public void setDomain(ExtaDomain domain) {
        this.domain = domain;
    }

    public void setActions(Set<SecureAction> actions) {
        this.actions = actions;
    }

    public void setTarget(SecureTarget target) {
        this.target = target;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }
}

