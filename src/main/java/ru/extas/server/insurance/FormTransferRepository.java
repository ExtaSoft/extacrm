/**
 *
 */
package ru.extas.server.insurance;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extas.model.insurance.FormTransfer;

import java.util.List;

/**
 * Управление актами приема передачи БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface FormTransferRepository extends JpaRepository<FormTransfer, String>, FormTransferService {

    /**
     * Найти акты приема передачи по номеру бланка
     *
     * @param formNum номер бланка
     * @return список найденных актов или null
     */
    @Query("SELECT o FROM FormTransfer o WHERE ?1 IN (o.formNums)")
    List<FormTransfer> findByFormNum(String formNum);
}
