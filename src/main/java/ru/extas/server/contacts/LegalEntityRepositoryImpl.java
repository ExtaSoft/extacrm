package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация сервисов управления данными юр.лиц
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 14:30
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LegalEntityRepositoryImpl extends AbstractSecuredRepository<LegalEntity> {

    @Inject
    private LegalEntityRepository legalEntityRepository;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private CompanyRepository companyRepository;

    @Override
    public JpaRepository<LegalEntity, ?> getEntityRepository() {
        return legalEntityRepository;
    }

    @Override
    protected Collection<String> getObjectBrands(LegalEntity legalEntity) {
        Set<String> regions = newHashSet();
        if(legalEntity.getActualAddress() != null)
            regions.add(legalEntity.getActualAddress().getRegion());
        return regions;
    }

    @Override
    protected Collection<String> getObjectRegions(LegalEntity legalEntity) {
        return null;
    }

    @Transactional
    @Override
    public void permitObject(LegalEntity legalEntity, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (legalEntity != null) {
            super.permitObject(legalEntity, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты юр.лица:
            // Компания
            companyRepository.permitAndSave(legalEntity.getCompany(), userContact, regions, brands);
            // Директор
            personRepository.permitAndSave(legalEntity.getDirector(), userContact, regions, brands);
        }
    }
}
