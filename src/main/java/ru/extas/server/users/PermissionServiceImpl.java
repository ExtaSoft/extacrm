package ru.extas.server.users;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.Person;
import ru.extas.security.SecuredRepository;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.insurance.InsuranceRepository;
import ru.extas.server.lead.LeadRepository;
import ru.extas.server.sale.SaleRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Реализация Служб для работы с системой распределения прав доступа
 *
 * @author Valery Orlov
 *         Date: 04.04.2014
 *         Time: 0:42
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PermissionServiceImpl implements PermissionService {

    @Inject private PersonRepository personRepository;
    @Inject private CompanyRepository companyRepository;
    @Inject private LegalEntityRepository legalEntityRepository;
    @Inject private SalePointRepository salePointRepository;
    @Inject private LeadRepository leadRepository;
    @Inject private SaleRepository saleRepository;
    @Inject private InsuranceRepository insuranceRepository;
    @Inject private UserManagementService userService;

    @Transactional
    @Override
    public void permitAllOwnObjects() {
        // Физ.лица
        permitObjects(personRepository.findAll(), personRepository);
        // Торговая точка
        permitObjects(salePointRepository.findAll(), salePointRepository);
        // Юр.лица
        permitObjects(legalEntityRepository.findAll(), legalEntityRepository);
        // Компании
        permitObjects(companyRepository.findAll(), companyRepository);
        // Лид
        permitObjects(leadRepository.findAll(), leadRepository);
        // Продажа
        permitObjects(saleRepository.findAll(), saleRepository);
        // Страховка
        permitObjects(insuranceRepository.findAll(), insuranceRepository);
    }

    protected <Entity extends SecuredObject> void permitObjects(List<Entity> entities, SecuredRepository<Entity> repository) {
        for(Entity entity : entities) {
            Person createdBy = userService.findUserContactByLogin(entity.getCreatedBy());
            repository.permitAndSave(entity, createdBy);
            Person modifiedBy = userService.findUserContactByLogin(entity.getModifiedBy());
            repository.permitAndSave(entity, modifiedBy);
        }
    }

}
