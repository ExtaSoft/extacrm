/**
 * 
 */
package ru.extas.server;

import java.util.Collection;

import ru.extas.model.Contact;

/**
 * Интерфейс управления контактами
 * 
 * @author Valery Orlov
 * 
 */
public interface ContactService {

	/**
	 * Возвращает список всех контактов
	 * 
	 * @return список контактов
	 */
	Collection<Contact> loadContacts();

	/**
	 * Сохраняет контакт в базе
	 * 
	 * @param contact
	 *            - сохраняемый контакт
	 */
	void persistContact(Contact contact);
}
