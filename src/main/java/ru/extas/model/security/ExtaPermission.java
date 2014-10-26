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
public class ExtaPermission extends AuditedObject implements Permission {

    @Column(name = "DOMAIN")
    private ExtaDomain domain;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ACCESS_PERMISSION_ACTION")
    private Set<SecureAction> actions = newHashSet();

    @Column(name = "TARGET")
    private SecureTarget target;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private UserProfile user;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private UserGroup group;


    /**
     * Создает право доступа.
     * Разрешает действие над определенными целевыми объектами в заданном разделе системы.
     *
     * @param domain раздел системы
     * @param action разрешенное действие, null чтобы разрешить все
     * @param target целевые объекты, null чтобы разрешить все
     */
    public ExtaPermission(final ExtaDomain domain, final SecureAction action, final SecureTarget target) {
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
    public ExtaPermission(final ExtaDomain domain, final Set<SecureAction> actions, final SecureTarget target) {
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

    /**
     * <p>Constructor for ExtaPermission.</p>
     */
    public ExtaPermission() {
    }

    /** {@inheritDoc} */
    public ExtaPermission createCopy() {
        final ExtaPermission clone = new ExtaPermission();

        clone.setDomain(getDomain());
        clone.setActions(EnumSet.copyOf(actions));
        clone.setTarget(getTarget());

        return clone;
    }

    /** {@inheritDoc} */
    @Override
    public boolean implies(final Permission p) {
        checkNotNull(p);
        if (!(p instanceof ExtaPermission)) {
            return false;
        }

        final ExtaPermission perm = (ExtaPermission) p;

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
     * @return a {@link ru.extas.model.security.ExtaDomain} object.
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
     * @return a {@link ru.extas.model.security.SecureTarget} object.
     */
    public SecureTarget getTarget() {
        return target;
    }

    /**
     * <p>Setter for the field <code>domain</code>.</p>
     *
     * @param domain a {@link ru.extas.model.security.ExtaDomain} object.
     */
    public void setDomain(final ExtaDomain domain) {
        this.domain = domain;
    }

    /**
     * <p>Setter for the field <code>actions</code>.</p>
     *
     * @param actions a {@link java.util.Set} object.
     */
    public void setActions(final Set<SecureAction> actions) {
        this.actions = actions;
    }

    /**
     * <p>Setter for the field <code>target</code>.</p>
     *
     * @param target a {@link ru.extas.model.security.SecureTarget} object.
     */
    public void setTarget(final SecureTarget target) {
        this.target = target;
    }

    /**
     * <p>Getter for the field <code>user</code>.</p>
     *
     * @return a {@link ru.extas.model.security.UserProfile} object.
     */
    public UserProfile getUser() {
        return user;
    }

    /**
     * <p>Setter for the field <code>user</code>.</p>
     *
     * @param user a {@link ru.extas.model.security.UserProfile} object.
     */
    public void setUser(final UserProfile user) {
        this.user = user;
    }

    /**
     * <p>Getter for the field <code>group</code>.</p>
     *
     * @return a {@link ru.extas.model.security.UserGroup} object.
     */
    public UserGroup getGroup() {
        return group;
    }

    /**
     * <p>Setter for the field <code>group</code>.</p>
     *
     * @param group a {@link ru.extas.model.security.UserGroup} object.
     */
    public void setGroup(final UserGroup group) {
        this.group = group;
    }
}

