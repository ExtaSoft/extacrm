/**
 *
 */
package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Contact;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JPA имплементация службы управления контактами
 *
 * @author Valery Orlov
 */
@Repository
public class ContactServiceJpa implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceJpa.class);
    @PersistenceContext
    private EntityManager em;

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
        if (contact.getId() == null)
            em.persist(contact);
        else
            em.merge(contact);
    }

    @Transactional
    @Override
    public void updateMissingType() {
    }

}
