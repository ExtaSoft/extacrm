package ru.extas.model.security;

import org.apache.commons.lang3.tuple.Pair;
import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;

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

    public void addSecurityUserAccess(final Employee user, final AccessRole role) {
        checkNotNull(user);
        checkNotNull(role);
        final ObjectSecurityRule rule = getSecurityRule();
        final Map<Employee, UserObjectAccess> users = rule.getUsers();
        if (users.containsKey(user)) {
            final UserObjectAccess access = users.get(user);
            if (access.getRole() == null || access.getRole().ordinal() < role.ordinal())
                access.setRole(role);
        } else
            users.put(user, new UserObjectAccess(rule, user, role));
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

    public void addSecurityUserAccess(final Collection<Pair<Employee, AccessRole>> users) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(users))
            users.forEach(p -> addSecurityUserAccess(p.getLeft(), p.getRight()));
    }

    public void addSecuritySalePoints(final Collection<SalePoint> salePoints) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(salePoints))
            rule.getSalePoints().addAll(salePoints);
    }

    public void addSecurityCompanies(final Collection<Company> companies) {
        final ObjectSecurityRule rule = getSecurityRule();
        if (!isEmpty(companies))
            rule.getCompanies().addAll(companies);

    }
}
