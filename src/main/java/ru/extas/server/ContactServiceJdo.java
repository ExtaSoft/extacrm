/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Contact;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JDO имплементация службы управления контактами
 *
 * @author Valery Orlov
 */
public class ContactServiceJdo implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceJdo.class);

    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.ContactService#loadAll()
     */
    @Override
    public Collection<Contact> loadContacts() {
        logger.debug("Requesting contact list...");
        unitOfWork.get().begin();
        try {
            List<Contact> contacts = new ArrayList<>();
            Extent<Contact> extent = pm.get().getExtent(Contact.class, false);
            for (Contact contact : extent) {
                contacts.add(contact);
            }
            extent.closeAll();
            logger.debug("Retrieved {} contacts", contacts.size());

            return contacts;
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.ContactService#persistContact(ru.extas.model.Contact)
     */
    @Override
    public void persistContact(Contact contact) {
        checkNotNull(contact, "Can't persist NULL-value contact!!!");
        checkNotNull(contact.getName(), "Can't persist contact with null name!!!");
        unitOfWork.get().begin();
        try {
            logger.debug("Persisting contact with name {}...", contact.getName());
            pm.get().makePersistent(contact);
        } finally {
            unitOfWork.get().end();
        }
    }

    @Override
    public void updateMissingType() {
        logger.debug("Requesting contact list...");
        unitOfWork.get().begin();
        try {
            final PersistenceManager persistenceManager = pm.get();
            Extent<Contact> extent = persistenceManager.getExtent(Contact.class, false);
            for (Contact contact : extent) {
                if (contact.getType() == null) {
                    contact.setType(Contact.Type.PERSON);
                    persistenceManager.makePersistent(contact);
                }
            }
            extent.closeAll();

        } finally {
            unitOfWork.get().end();
        }
    }

}
