package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;
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
    public Company permitAndSave(Company company, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (company != null) {
            company = super.permitAndSave(company, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты компании:
            // Собственник(и) Компании
            personRepository.permitAndSave(company.getOwners(), userContact, regions, brands);
            // Сотрудники компании
            personRepository.permitAndSave(company.getEmployees(), userContact, regions, brands);
            // Юридические лица компании
            legEntRepository.permitAndSave(company.getLegalEntities(), userContact, regions, brands);
            // Торговые точки компании
            salePointRepository.permitAndSave(company.getSalePoints(), userContact, regions, brands);
        }
        return company;
    }
}
