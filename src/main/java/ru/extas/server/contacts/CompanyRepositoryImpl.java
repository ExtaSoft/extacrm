package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
 * Реализация прощедут обработки данных компании
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:54
 * @version $Id: $Id
 * @since 0.3.0
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class CompanyRepositoryImpl extends AbstractSecuredRepository<Company> {

    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private LegalEntityRepository legEntRepository;
    @Inject
    private SalePointRepository salePointRepository;

    /** {@inheritDoc} */
    @Override
    public JpaRepository<Company, ?> getEntityRepository() {
        return companyRepository;
    }

    @Override
    protected Collection<Pair<Employee, AccessRole>> getObjectUsers(final Company company) {
        return newArrayList(getCurUserAccess(company));
    }

    @Override
    protected Collection<Company> getObjectCompanies(final Company company) {
        return null;
    }

    @Override
    protected Collection<SalePoint> getObjectSalePoints(final Company company) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(final Company company) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(final Company company) {
        if(!isNullOrEmpty(company.getRegion()))
            return newHashSet(company.getRegion());
        return null;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Company permitAndSave(Company company,
                                 final Collection<Pair<Employee, AccessRole>> users,
                                 final Collection<SalePoint> salePoints,
                                 final Collection<Company> companies,
                                 final Collection<String> regions,
                                 final Collection<String> brands) {
        if (company != null) {
            company = super.permitAndSave(company, users, salePoints, companies, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты компании:
            // Собственник(и) Компании
            final Collection<Pair<Employee, AccessRole>> readers = reassigneRole(users, AccessRole.READER);
            employeeRepository.permitAndSave(company.getOwners(), readers, salePoints, companies, regions, brands);
            // Сотрудники компании
            employeeRepository.permitAndSave(company.getEmployees(), readers, salePoints, companies, regions, brands);
            // Юридические лица компании
            legEntRepository.permitAndSave(company.getLegalEntities(), readers, salePoints, companies, regions, brands);
            // Торговые точки компании
            salePointRepository.permitAndSave(company.getSalePoints(), readers, salePoints, companies, regions, brands);
        }
        return company;
    }
}
