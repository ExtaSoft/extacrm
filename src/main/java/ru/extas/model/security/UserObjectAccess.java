package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Person;

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
    private Person user;

    private AccessRole role;

    public UserObjectAccess(ObjectSecurityRule securityRule, Person user, AccessRole role) {
        this.securityRule = securityRule;
        this.user = user;
        this.role = role;
    }

    public UserObjectAccess() {
    }

    public ObjectSecurityRule getSecurityRule() {
        return securityRule;
    }

    public void setSecurityRule(ObjectSecurityRule securityRule) {
        this.securityRule = securityRule;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public AccessRole getRole() {
        return role;
    }

    public void setRole(AccessRole role) {
        this.role = role;
    }
}
