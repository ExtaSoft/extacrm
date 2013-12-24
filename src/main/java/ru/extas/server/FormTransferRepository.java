/**
 *
 */
package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.FormTransfer;

import java.util.List;

/**
 * Управление актами приема передачи БСО
 *
 * @author Valery Orlov
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface FormTransferRepository extends CrudRepository<FormTransfer, String> {

/**
 * Найти акты приема передачи по номеру бланка
 *
 * @param formNum номер бланка
 *
 * @return список найденных актов или null
 */
@Query("SELECT o FROM FormTransfer o WHERE ?1 IN (o.formNums)")
List<FormTransfer> findByFormNum(String formNum);
}
