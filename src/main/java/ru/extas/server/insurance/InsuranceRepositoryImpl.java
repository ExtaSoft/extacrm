package ru.extas.server.insurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.model.insurance.Insurance;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.security.UserManagementServiceImpl;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
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
    @Inject private LegalEntityRepository legalEntityRepository;
    @Inject private SalePointRepository salePointRepository;


    /** {@inheritDoc} */
    @Transactional
    @Override
    public Insurance saveAndIssue(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        insurance = insuranceRepository.secureSave(insurance);
        policyService.issuePolicy(insurance.getRegNum());
        formService.spendForm(insurance.getA7Num());
        return insurance;
    }

    /** {@inheritDoc} */
    @Override
    public JpaRepository<Insurance, ?> getEntityRepository() {
        return insuranceRepository;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(Insurance insurance) {
        if(!isNullOrEmpty(insurance.getMotorBrand()))
            return newHashSet(insurance.getMotorBrand());
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(Insurance insurance) {
        Set<String> regions = newHashSet();
        if(insurance.getClient() != null
                && insurance.getClient().getRegAddress() != null
                && !isNullOrEmpty(insurance.getClient().getRegAddress().getRegion()))
            regions.add(insurance.getClient().getRegAddress().getRegion());
        if(insurance.getDealer() != null
                && insurance.getDealer().getRegAddress() != null
                && !isNullOrEmpty(insurance.getDealer().getRegAddress().getRegion()))
            regions.add(insurance.getDealer().getRegAddress().getRegion());
        return regions;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Insurance permitAndSave(Insurance insurance, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (insurance != null) {
            insurance = super.permitAndSave(insurance, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
            if(insurance.getClient() instanceof Person)
                personRepository.permitAndSave((Person) insurance.getClient(), userContact, regions, brands);
            else
                legalEntityRepository.permitAndSave((LegalEntity) insurance.getClient(), userContact, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(insurance.getDealer(), userContact, regions, brands);
        }
        return insurance;
    }
}
