/**
 *
 */
package ru.extas.server.insurance;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extas.model.insurance.Policy;

import java.util.List;

/**
 * Интерфейс к страховым полисам БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface PolicyRepository extends JpaRepository<Policy, String>, PolicyService {

    /**
     * Найти полис по номеру
     *
     * @param regNum номер
     * @return найденный полис или null
     */
    Policy findByRegNum(String regNum);

    /**
     * <p>findAvailableAtTime.</p>
     *
     * @param dateTime a {@link org.joda.time.DateTime} object.
     * @return a {@link java.util.List} object.
     */
    @Query("SELECT p FROM Policy p " +
            "WHERE p.issueDate IS NULL " +
            "AND (p.bookTime IS NULL OR p.bookTime < ?1) " +
            "ORDER BY p.bookTime, p.regNum")
    List<Policy> findAvailableAtTime(DateTime dateTime);
}
