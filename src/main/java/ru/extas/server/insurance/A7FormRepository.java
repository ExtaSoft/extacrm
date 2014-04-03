/**
 *
 */
package ru.extas.server.insurance;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.Contact;
import ru.extas.model.insurance.A7Form;

import java.util.List;

/**
 * Управление формами А-7
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface A7FormRepository extends JpaRepository<A7Form, String>, A7FormService {

    /**
     * Найти квитанцию по номеру
     *
     * @param regNum Номер квитанции
     * @return Найденная квитанция или null
     */
    A7Form findByRegNum(String regNum);

    /**
     * <p>findByOwnerAndStatus.</p>
     *
     * @param owner  a {@link ru.extas.model.contacts.Contact} object.
     * @param status a {@link ru.extas.model.insurance.A7Form.Status} object.
     * @return a {@link java.util.List} object.
     */
    List<A7Form> findByOwnerAndStatus(Contact owner, A7Form.Status status);
}
