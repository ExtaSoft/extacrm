package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
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
    private PersonRepository personRepository;
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
    protected Collection<Pair<Person, AccessRole>> getObjectUsers(Company company) {
        return newArrayList(getCurUserAccess(company));
    }

    @Override
    protected Collection<Company> getObjectCompanies(Company company) {
        return null;
    }

    @Override
    protected Collection<SalePoint> getObjectSalePoints(Company company) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(Company company) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(Company company) {
        if(company.getRegAddress() != null && !isNullOrEmpty(company.getRegAddress().getRegion()))
            return newHashSet(company.getRegAddress().getRegion());
        return null;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Company permitAndSave(Company company,
                                 Collection<Pair<Person, AccessRole>> users,
                                 Collection<SalePoint> salePoints,
                                 Collection<Company> companies,
                                 Collection<String> regions,
                                 Collection<String> brands) {
        if (company != null) {
            company = super.permitAndSave(company, users, salePoints, companies, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты компании:
            // Собственник(и) Компании
            final Collection<Pair<Person, AccessRole>> readers = reassigneRole(users, AccessRole.READER);
            personRepository.permitAndSave(company.getOwners(), readers, salePoints, companies, regions, brands);
            // Сотрудники компании
            personRepository.permitAndSave(company.getEmployees(), readers, salePoints, companies, regions, brands);
            // Юридические лица компании
            legEntRepository.permitAndSave(company.getLegalEntities(), readers, salePoints, companies, regions, brands);
            // Торговые точки компании
            salePointRepository.permitAndSave(company.getSalePoints(), readers, salePoints, companies, regions, brands);
        }
        return company;
    }
}
