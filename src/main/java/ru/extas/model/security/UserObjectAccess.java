package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;

/**
 * @author Valery Orlov
 *         Date: 08.10.2014
 *         Time: 16:07
 */
@Entity
@Table(name = "SECURITY_RULE_USER")
public class UserObjectAccess extends AuditedObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "SECURITY_RULE_ID")
    private ObjectSecurityRule securityRule;

    @Column(name = "USER_ID", length = ID_SIZE)
    private String userId;

    private AccessRole role;

    public UserObjectAccess(final ObjectSecurityRule securityRule, final String userId, final AccessRole role) {
        this.securityRule = securityRule;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public AccessRole getRole() {
        return role;
    }

    public void setRole(final AccessRole role) {
        this.role = role;
    }
}
