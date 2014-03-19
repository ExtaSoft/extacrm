/**
 *
 */
package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.Contact;

/**
 * Интерфейс управления контактами
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ContactRepository extends CrudRepository<Contact, String> {

}
