package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация сервисов управления данными юр.лиц
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 14:30
 * @version $Id: $Id
 * @since 0.3.0
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LegalEntityRepositoryImpl extends AbstractSecuredRepository<LegalEntity> {

    @Inject private LegalEntityRepository legalEntityRepository;
    @Inject private PersonRepository personRepository;
    @Inject private CompanyRepository companyRepository;

    /** {@inheritDoc} */
    @Override
    public JpaRepository<LegalEntity, ?> getEntityRepository() {
        return legalEntityRepository;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(final LegalEntity legalEntity) {
        final Set<String> regions = newHashSet();
        if(legalEntity.getRegAddress() != null && !isNullOrEmpty(legalEntity.getRegAddress().getRegion()))
            regions.add(legalEntity.getRegAddress().getRegion());
        return regions;
    }

    @Override
    protected Collection<Pair<String, AccessRole>> getObjectUsers(final LegalEntity legalEntity) {
        final ArrayList<Pair<String, AccessRole>> users = newArrayList();

        // Текущий пользователь как Владелец или Редактор
        users.add(getCurUserAccess(legalEntity));

        return users;
    }

    @Override
    protected Collection<String> getObjectCompanies(final LegalEntity legalEntity) {
        if (legalEntity != null && legalEntity.getCompany() != null)
            return newHashSet(legalEntity.getCompany().getId());
        else
            return null;
    }

    @Override
    protected Collection<String> getObjectSalePoints(final LegalEntity legalEntity) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(final LegalEntity legalEntity) {
        return legalEntity.getMotorBrands();
    }

}
