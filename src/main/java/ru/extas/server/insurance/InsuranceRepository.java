package ru.extas.server.insurance;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.insurance.Insurance;

/**
 * Интерфейс управления данными об иммущественном страховании
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface InsuranceRepository extends CrudRepository<Insurance, String> {

/**
 * <p>findByCreatedBy.</p>
 *
 * @param createdBy a {@link java.lang.String} object.
 * @return a {@link java.lang.Iterable} object.
 */
Iterable<Insurance> findByCreatedBy(String createdBy);
}
