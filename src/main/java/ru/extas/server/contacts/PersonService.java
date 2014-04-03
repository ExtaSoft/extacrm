package ru.extas.server.contacts;

import ru.extas.model.contacts.Person;
import ru.extas.security.SecuredRepository;

/**
 * Определяет дополнительные методы работы с физ.лицами
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 11:57
 */
public interface PersonService extends SecuredRepository<Person> {

}
