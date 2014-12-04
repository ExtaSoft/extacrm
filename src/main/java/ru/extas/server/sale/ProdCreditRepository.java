package ru.extas.server.sale;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.sale.ProdCredit;

import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Кредит"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdCreditRepository extends JpaRepository<ProdCredit, String> {

	/**
	 * <p>findByActiveOrderByNameAsc.</p>
	 *
	 * @param active a boolean.
	 * @return a {@link java.util.List} object.
	 */
	List<ProdCredit> findByActiveOrderByNameAsc(boolean active);
}
