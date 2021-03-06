package ru.extas.server.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Employee;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.SecuredObject;
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
 * @version $Id: $Id
 * @since 0.3.0
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

    /** {@inheritDoc} */
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

    /**
     * <p>permitObjects.</p>
     *
     * @param entities a {@link java.util.List} object.
     * @param repository a {@link ru.extas.security.SecuredRepository} object.
     * @param <Entity> a Entity object.
     */
    protected <Entity extends SecuredObject> void permitObjects(final List<Entity> entities, final SecuredRepository<Entity> repository) {
        for(final Entity entity : entities) {
            final Employee createdBy = userService.findUserEmployeeByLogin(entity.getCreatedBy());
            final Employee modifiedBy = userService.findUserEmployeeByLogin(entity.getLastModifiedBy());
            entity.addSecurityUserAccess(createdBy.getId(), AccessRole.OWNER);
            entity.addSecurityUserAccess(modifiedBy.getId(), AccessRole.EDITOR);
            repository.secureSave(entity);
        }
    }

}
