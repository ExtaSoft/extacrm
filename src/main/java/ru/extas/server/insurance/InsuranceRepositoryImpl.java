package ru.extas.server.insurance;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.security.UserManagementServiceImpl;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class InsuranceRepositoryImpl extends AbstractSecuredRepository<Insurance> implements InsuranceService {

    private final static Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Inject
    private InsuranceRepository insuranceRepository;
    @Inject
    private PolicyRepository policyService;
    @Inject
    private A7FormRepository formService;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private LegalEntityRepository legalEntityRepository;
    @Inject
    private SalePointRepository salePointRepository;
    @Inject
    private CompanyRepository companyRepository;


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Insurance saveAndIssue(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        insurance = insuranceRepository.secureSave(insurance);
        policyService.issuePolicy(insurance.getRegNum());
        formService.spendForm(insurance.getA7Num());
        return insurance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JpaRepository<Insurance, ?> getEntityRepository() {
        return insuranceRepository;
    }

    @Override
    protected Collection<Pair<Person, AccessRole>> getObjectUsers(Insurance insurance) {
        return newArrayList(getCurUserAccess(insurance));
    }

    @Override
    protected Collection<Company> getObjectCompanies(Insurance insurance) {
        if (insurance.getDealer() != null)
            return newArrayList(insurance.getDealer().getCompany());
        return null;
    }

    @Override
    protected Collection<SalePoint> getObjectSalePoints(Insurance insurance) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectBrands(Insurance insurance) {
        if (!isNullOrEmpty(insurance.getMotorBrand()))
            return newHashSet(insurance.getMotorBrand());
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectRegions(Insurance insurance) {
        Set<String> regions = newHashSet();
        if (insurance.getDealer() != null
                && insurance.getDealer().getRegAddress() != null
                && !isNullOrEmpty(insurance.getDealer().getRegAddress().getRegion()))
            regions.add(insurance.getDealer().getRegAddress().getRegion());
        return regions;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Insurance permitAndSave(Insurance insurance,
                                   Collection<Pair<Person, AccessRole>> users,
                                   Collection<SalePoint> salePoints,
                                   Collection<Company> companies,
                                   Collection<String> regions,
                                   Collection<String> brands) {
        if (insurance != null) {
            insurance = super.permitAndSave(insurance, users, salePoints, companies, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты страховки:
            // Клиент
            final Collection<Pair<Person, AccessRole>> readers = reassigneRole(users, AccessRole.READER);
            if (insurance.getClient() instanceof Person)
                personRepository.permitAndSave((Person) insurance.getClient(), readers, salePoints, companies, regions, brands);
            else
                legalEntityRepository.permitAndSave((LegalEntity) insurance.getClient(), readers, salePoints, companies, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(insurance.getDealer(), readers, salePoints, companies, regions, brands);
            // Компания продавца
            if (insurance.getDealer() != null)
                companyRepository.permitAndSave(insurance.getDealer().getCompany(), readers, salePoints, companies, regions, brands);
        }
        return insurance;
    }
}
