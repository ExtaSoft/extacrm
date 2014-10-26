package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Employee;

import javax.persistence.*;

/**
 * @author Valery Orlov
 *         Date: 08.10.2014
 *         Time: 16:07
 */
@Entity
@Table(name = "SECURITY_RULE_USER")
public class UserObjectAccess extends AuditedObject {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "SECURITY_RULE_ID")
    private ObjectSecurityRule securityRule;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "USER_ID")
    private Employee user;

    private AccessRole role;

    public UserObjectAccess(final ObjectSecurityRule securityRule, final Employee user, final AccessRole role) {
        this.securityRule = securityRule;
        this.user = user;
        this.role = role;
    }

    public UserObjectAccess() {
    }

    public ObjectSecurityRule getSecurityRule() {
        return securityRule;
    }

    public void setSecurityRule(final ObjectSecurityRule securityRule) {
        this.securityRule = securityRule;
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(final Employee user) {
        this.user = user;
    }

    public AccessRole getRole() {
        return role;
    }

    public void setRole(final AccessRole role) {
        this.role = role;
    }
}
