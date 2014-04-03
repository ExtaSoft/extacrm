package ru.extas.server.insurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.insurance.Insurance;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.users.UserManagementServiceImpl;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

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

    @Inject private InsuranceRepository insuranceRepository;
    @Inject private PolicyRepository policyService;
    @Inject private A7FormRepository formService;
    @Inject private PersonRepository personRepository;
    @Inject private CompanyRepository companyRepository;
    @Inject private SalePointRepository salePointRepository;


    /** {@inheritDoc} */
    @Transactional
    @Override
    public void saveAndIssue(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        insuranceRepository.secureSave(insurance);
        policyService.issuePolicy(insurance.getRegNum());
        formService.spendForm(insurance.getA7Num());
    }

    @Override
    public JpaRepository<Insurance, ?> getEntityRepository() {
        return insuranceRepository;
    }

    @Override
    protected Collection<String> getObjectBrands(Insurance insurance) {
        return newHashSet(insurance.getMotorBrand());
    }

    @Override
    protected Collection<String> getObjectRegions(Insurance insurance) {
        Set<String> regions = newHashSet();
        if(insurance.getClient() != null && insurance.getClient().getActualAddress() != null)
            regions.add(insurance.getClient().getActualAddress().getRegion());
        if(insurance.getDealer() != null && insurance.getDealer().getActualAddress() != null)
            regions.add(insurance.getDealer().getActualAddress().getRegion());
        return regions;
    }

    @Transactional
    @Override
    public void permitObject(Insurance insurance, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (insurance != null) {
            super.permitObject(insurance, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
            if(insurance.getClient() instanceof Person)
                personRepository.permitAndSave((Person) insurance.getClient(), userContact, regions, brands);
            else
                companyRepository.permitAndSave((Company) insurance.getClient(), userContact, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(insurance.getDealer(), userContact, regions, brands);
        }
    }
}
