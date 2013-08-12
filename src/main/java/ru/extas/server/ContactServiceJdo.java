/**
 * 
 */
package ru.extas.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Contact;

/**
 * JDO имплементация службы управления контактами
 * 
 * @author Valery Orlov
 * 
 */
public class ContactServiceJdo implements ContactService {

	private final Logger logger = LoggerFactory.getLogger(ContactServiceJdo.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.ContactService#loadAll()
	 */
	@Override
	public Collection<Contact> loadContacts() {
		logger.debug("Requesting contact list...");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<Contact> contacts = new ArrayList<Contact>();
			Extent<Contact> extent = pm.getExtent(Contact.class, false);
			for (Contact contact : extent) {
				contacts.add(contact);
			}
			extent.closeAll();
			logger.debug("Retrieved {} contacts", contacts.size());

			return contacts;
		} finally {
			pm.close();
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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			logger.info("Prsisting contact with name {}...", contact.getName());
			pm.makePersistent(contact);
		} finally {
			pm.close();
		}
	}

}
