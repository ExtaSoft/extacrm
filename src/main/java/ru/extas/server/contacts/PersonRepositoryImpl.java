package ru.extas.server.contacts;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.common.Address;
import ru.extas.model.contacts.Person;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация методов управления физ.лицами
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 12:39
 * @version $Id: $Id
 * @since 0.3.0
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PersonRepositoryImpl extends AbstractSecuredRepository<Person> {

    @Inject
    private PersonRepository entityRepository;

    /** {@inheritDoc} */
    @Override
    public PersonRepository getEntityRepository() {
        return entityRepository;
    }

    @Override
    protected Collection<Pair<String, AccessRole>> getObjectUsers(final Person person) {
        return newArrayList(getCurUserAccess(person));
    }

    @Override
    protected Collection<String> getObjectCompanies(final Person person) {
        return null;
    }

    @Override
    protected Collection<String> getObjectSalePoints(final Person person) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(final Person person) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(final Person person) {
        final Address address = person.getRegisterAddress();
        if(address != null && !isNullOrEmpty(address.getRegionWithType()))
            return newHashSet(address.getRegionWithType());
        return null;
    }
}
