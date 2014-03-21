package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.ProdInsurance;

import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Страховка"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdInsuranceRepository extends CrudRepository<ProdInsurance, String> {

	/**
	 * <p>findByActive.</p>
	 *
	 * @param active a boolean.
	 * @return a {@link java.util.List} object.
	 */
	List<ProdInsurance> findByActive(boolean active);

}
