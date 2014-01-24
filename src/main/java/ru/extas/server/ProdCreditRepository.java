package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.ProdCredit;

import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Кредит"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdCreditRepository extends CrudRepository<ProdCredit, String> {

	List<ProdCredit> findByActive(boolean active);
}
