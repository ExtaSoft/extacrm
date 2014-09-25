package ru.extas.server.lead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Person;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.sale.SaleRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;

/**
 * JPA имплементация службы управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:55
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LeadRepositoryImpl extends AbstractSecuredRepository<Lead> implements LeadService {

    private final static Logger logger = LoggerFactory.getLogger(LeadRepositoryImpl.class);

    @Inject
    private LeadRepository leadRepository;
    //    @Inject private RuntimeService runtimeService;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private SalePointRepository salePointRepository;
    @Inject
    private SaleRepository saleRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Lead qualify(Lead lead) {
        checkNotNull(lead);
        checkState(lead.getClient() != null, "Невозможно квалифицировать, поскольку не привязан клиент!");
        checkState(lead.getVendor() != null, "Невозможно квалифицировать, поскольку не привязан Мото салон!");
        checkState(lead.getStatus() == Lead.Status.NEW, "Квалифицировать можно только новый лид!");

//        // запуск БП
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("saleCreditProcess");
//        // Привязать процесс к лиду
//        lead.setProcessId(processInstance.getId());

        // Обновляем поля лида
        lead.setContactName(lead.getClient().getName());

        // Статус
        lead.setStatus(Lead.Status.QUALIFIED);
        // Сохранить изменения
        lead = leadRepository.secureSave(lead);

        // Создать продажу на базе лида
        Sale sale = saleRepository.ctreateSaleByLead(lead);

        // Привязать лид к процессу
//        runtimeService.setVariable(processInstance.getId(), "lead", lead);
//        logger.debug("Started \"saleCreditProcess\" business process instance (id = {})", processInstance.getId());

        return lead;
    }

    @Transactional
    @Override
    public void finishLead(Lead lead, Lead.Result result) {
        lead.setResult(result);
        lead.setStatus(Lead.Status.CLOSED);
        secureSave(lead);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JpaRepository<Lead, ?> getEntityRepository() {
        return leadRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectBrands(Lead lead) {
        if (!isNullOrEmpty(lead.getMotorBrand()))
            newHashSet(lead.getMotorBrand());

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectRegions(Lead lead) {
        Set<String> regions = newHashSet();
        if (lead.getClient() != null
                && lead.getClient().getRegAddress() != null
                && !isNullOrEmpty(lead.getClient().getRegAddress().getRegion()))
            regions.add(lead.getClient().getRegAddress().getRegion());
        if (lead.getVendor() != null
                && lead.getVendor().getRegAddress() != null
                && !isNullOrEmpty(lead.getVendor().getRegAddress().getRegion()))
            regions.add(lead.getVendor().getRegAddress().getRegion());
        return regions;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Lead permitAndSave(Lead lead, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (lead != null) {
            lead = super.permitAndSave(lead, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
//            if(lead.getClient() instanceof Person)
            personRepository.permitAndSave(lead.getClient(), userContact, regions, brands);
//            else
//                companyRepository.permitAndSave((Company) lead.getClient(), userContact, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(lead.getVendor(), userContact, regions, brands);
        }
        return lead;
    }
}
