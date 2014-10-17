package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация методов управления сотрудниками
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 12:39
 * @version $Id: $Id
 * @since 0.3.0
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class EmployeeRepositoryImpl extends AbstractSecuredRepository<Employee> {

    @Inject
    private EmployeeRepository entityRepository;

    /** {@inheritDoc} */
    @Override
    public EmployeeRepository getEntityRepository() {
        return entityRepository;
    }

    @Override
    protected Collection<Pair<Employee, AccessRole>> getObjectUsers(final Employee employee) {
        return newArrayList(getCurUserAccess(employee));
    }

    @Override
    protected Collection<Company> getObjectCompanies(final Employee employee) {
        return null;
    }

    @Override
    protected Collection<SalePoint> getObjectSalePoints(final Employee employee) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(final Employee employee) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(final Employee employee) {
        if(employee.getRegAddress() != null && !isNullOrEmpty(employee.getRegAddress().getRegion()))
            return newHashSet(employee.getRegAddress().getRegion());
        return null;
    }
}
