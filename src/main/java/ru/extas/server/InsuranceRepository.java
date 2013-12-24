package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.Insurance;

/**
 * Интерфейс управления данными об иммущественном страховании
 *
 * @author Valery Orlov
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface InsuranceRepository extends CrudRepository<Insurance, String> {

Iterable<Insurance> findByCreatedBy(String createdBy);
}