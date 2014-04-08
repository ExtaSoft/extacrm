package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация методов управления данными торговой точки
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 14:48
 * @version $Id: $Id
 * @since 0.3.0
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class SalePointRepositoryImpl extends AbstractSecuredRepository<SalePoint> {

    @Inject private SalePointRepository salePointRepository;
    @Inject private LegalEntityRepository legalEntityRepository;
    @Inject private PersonRepository personRepository;

    /** {@inheritDoc} */
    @Override
    public JpaRepository<SalePoint, ?> getEntityRepository() {
        return salePointRepository;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(SalePoint salePoint) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(SalePoint salePoint) {
        if(salePoint.getActualAddress() != null && !isNullOrEmpty(salePoint.getActualAddress().getRegion()))
            return newHashSet(salePoint.getActualAddress().getRegion());
        return null;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public SalePoint permitAndSave(SalePoint salePoint, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (salePoint != null) {
            salePoint = super.permitAndSave(salePoint, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты торговой точки:
            // Юр. лица работающие на торговой точке
            legalEntityRepository.permitAndSave(salePoint.getLegalEntities(), userContact, regions, brands);
            // Сотрудники торговой точки
            personRepository.permitAndSave(salePoint.getEmployees(), userContact, regions, brands);
        }
        return salePoint;
    }
}
