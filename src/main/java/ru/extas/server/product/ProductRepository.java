package ru.extas.server.product;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.sale.Product;

import java.util.List;

/**
 * Интерфейс доступа к базе всех типов продуктов
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProductRepository extends JpaRepository<Product, String> {

	/**
	 * <p>findByActiveOrderByNameAsc.</p>
	 *
	 * @param active a boolean.
	 * @return a {@link java.util.List} object.
	 */
	List<Product> findByActiveOrderByNameAsc(boolean active);
}
