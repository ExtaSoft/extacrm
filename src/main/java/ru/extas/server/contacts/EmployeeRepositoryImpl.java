package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.common.Address;
import ru.extas.model.contacts.Employee;
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
public class EmployeeRepositoryImpl extends AbstractSecuredRepository<Employee> implements EmployeeService{

    @Inject
    private EmployeeRepository entityRepository;

    @Inject
    private CompanyRepository companyRepository;

    /** {@inheritDoc} */
    @Override
    public EmployeeRepository getEntityRepository() {
        return entityRepository;
    }

    @Override
    protected Collection<Pair<String, AccessRole>> getObjectUsers(final Employee employee) {
        return newArrayList(getCurUserAccess(employee));
    }

    @Override
    protected Collection<String> getObjectCompanies(final Employee employee) {
        if (employee != null && employee.getCompany() != null)
            return newHashSet(employee.getCompany().getId());
        else
            return null;
    }

    @Override
    protected Collection<String> getObjectSalePoints(final Employee employee) {
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
        final Address address = employee.getAddress();
        if(address != null && !isNullOrEmpty(address.getRegionWithType()))
            return newHashSet(address.getRegionWithType());
        return null;
    }

    /**
     * Определяет является ли переданный сотрудник, сотрудником Экстрим Ассистанс
     *
     * @param employee проверяемый сотрудник
     * @return true если сотрудник Экстрим Ассистанс, иначе false
     */
    @Override
    public boolean isEAEmployee(final Employee employee) {
        return employee != null && employee.getCompany() != null && companyRepository.isExtremeAssistance(employee.getCompany());
    }

    /**
     * Определяет является ли переданный сотрудник, сотрудником банка
     *
     * @param employee проверяемый сотрудник
     * @return true если сотрудник банка, иначе false
     */
    @Override
    public boolean isBankEmployee(final Employee employee) {
        return employee != null && employee.getCompany() != null && companyRepository.isBank(employee.getCompany());
    }

    /**
     * Определяет является ли переданный сотрудник, сотрудником дилера
     *
     * @param employee проверяемый сотрудник
     * @return true если сотрудник дилера, иначе false
     */
    @Override
    public boolean isDealerEmployee(final Employee employee) {
        return employee != null && employee.getCompany() != null && companyRepository.isDealer(employee.getCompany());
    }

    /**
     * Определяет является ли переданный сотрудник, сотрудником колл-центра
     *
     * @param employee проверяемый сотрудник
     * @return true если сотрудник колл-центра, иначе false
     */
    @Override
    public boolean isCallcenterEmployee(final Employee employee) {
        return employee != null && employee.getCompany() != null && companyRepository.isCallcenter(employee.getCompany());
    }
}
