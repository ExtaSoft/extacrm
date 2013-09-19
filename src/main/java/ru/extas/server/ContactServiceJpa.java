/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Contact;

import javax.persistence.EntityManager;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JPA имплементация службы управления контактами
 *
 * @author Valery Orlov
 */
public class ContactServiceJpa implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceJpa.class);
    @Inject
    private Provider<EntityManager> em;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.ContactService#loadAll()
     */
    @Transactional
    @Override
    public Collection<Contact> loadContacts() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.ContactService#persistContact(ru.extas.model.Contact)
     */
    @Transactional
    @Override
    public void persistContact(Contact contact) {
        checkNotNull(contact, "Can't persist NULL-value contact!!!");
        checkNotNull(contact.getName(), "Can't persist contact with null name!!!");
        logger.debug("Persisting contact with name {}...", contact.getName());
        if (contact.getKey() == null)
            em.get().persist(contact);
        else
            em.get().merge(contact);
    }

}
