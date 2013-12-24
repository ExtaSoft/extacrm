/**
 *
 */
package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.A7Form;
import ru.extas.model.Contact;

import java.util.List;

/**
 * Управление формами А-7
 *
 * @author Valery Orlov
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface A7FormRepository extends CrudRepository<A7Form, String> {

/**
 * Найти квитанцию по номеру
 *
 * @param regNum Номер квитанции
 *
 * @return Найденная квитанция или null
 */
A7Form findByRegNum(String regNum);

List<A7Form> findByOwnerAndStatus(Contact owner, A7Form.Status status);
}
