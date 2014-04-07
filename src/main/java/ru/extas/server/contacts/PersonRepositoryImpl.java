package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.security.AbstractSecuredRepository;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация методов управления физ.лицами
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 12:39
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PersonRepositoryImpl extends AbstractSecuredRepository<Person> {

    @Inject
    private PersonRepository entityRepository;

    @Override
    public PersonRepository getEntityRepository() {
        return entityRepository;
    }

    @Override
    protected Collection<String> getObjectBrands(Person person) {
        return null;
    }

    @Override
    protected Collection<String> getObjectRegions(Person person) {
        if(person.getActualAddress() != null && !isNullOrEmpty(person.getActualAddress().getRegion()))
            return newHashSet(person.getActualAddress().getRegion());
        return null;
    }
}
