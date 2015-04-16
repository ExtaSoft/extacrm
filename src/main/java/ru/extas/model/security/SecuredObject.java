package ru.extas.model.security;

import org.apache.commons.lang3.tuple.Pair;
import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Базовый класс для всех сущностей.
 * Имплементирует ведение журналов безопасности.
 *
 * @author Valery Orlov
 *         Date: 22.03.2014
 *         Time: 17:40
 * @version $Id: $Id
 * @since 0.3.0
 */
@MappedSuperclass
public class SecuredObject extends AuditedObject {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "SECURITY_RULE_ID")
    private ObjectSecurityRule securityRule;

    public ObjectSecurityRule getSecurityRule() {
        if (securityRule == null)
            setSecurityRule(new ObjectSecurityRule());
        return securityRule;
    }

    public void setSecurityRule(final ObjectSecurityRule securityRule) {
        this.securityRule = securityRule;
    }

    public void addSecurityUserAccess(final String userId, final AccessRole role) {
        checkNotNull(userId);
        checkNotNull(role);
        final ObjectSecurityRule rule = getSecurityRule();
        final Map<String, UserObjectAccess> users = rule.getUsers();
        if (users.containsKey(userId)) {
            final UserObjectAccess access = users.get(userId);
            if (access.getRole() == null || access.getRole().ordinal() < role.ordinal())
                access.setRole(role);
        } else
            users.put(userId, new UserObjectAccess(rule, userId, role));
    }

    public void addSecurityRegions(final Collection<String> regions) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(regions))
            rule.getRegions().addAll(regions);
    }

    public void addSecurityBrands(final Collection<String> brands) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(brands))
            rule.getBrands().addAll(brands);
    }

    public void addSecurityUserAccess(final Collection<Pair<String, AccessRole>> users) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(users))
            users.forEach(p -> addSecurityUserAccess(p.getLeft(), p.getRight()));
    }

    public void addSecuritySalePoints(final Collection<String> salePointIds) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(salePointIds))
            rule.getSalePointIds().addAll(salePointIds);
    }

    public void addSecurityCompanies(final Collection<String> companyIds) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(companyIds))
            rule.getCompanyIds().addAll(companyIds);

    }
}
