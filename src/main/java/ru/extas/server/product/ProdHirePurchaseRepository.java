package ru.extas.server.product;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.product.ProdHirePurchase;

import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Аренда с выкупом"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdHirePurchaseRepository extends JpaRepository<ProdHirePurchase, String> {

	/**
	 * <p>findByActiveOrderByNameAsc.</p>
	 *
	 * @param active a boolean.
	 * @return a {@link List} object.
	 */
	List<ProdHirePurchase> findByActiveOrderByNameAsc(boolean active);

}
