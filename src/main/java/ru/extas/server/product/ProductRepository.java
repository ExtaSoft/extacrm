package ru.extas.server.product;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.product.Product;

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
public interface ProductRepository extends JpaRepository<Product, String>, ProductService {

    /**
     * <p>findByActiveOrderByNameAsc.</p>
     *
     * @param active a boolean.
     * @return a {@link java.util.List} object.
     */
    List<Product> findByActiveOrderByNameAsc(boolean active);

    /**
     * Ищет продукты доступные для заданной торговой точки
     *
     * @param salePoint торговая точка
     * @return список найденных продуктов
     */
    @Query("SELECT p FROM SalePoint s JOIN s.legalEntities ls JOIN ls.credProducts p WHERE s = :salePoint")
    List<Product> findBySalePoint(@Param("salePoint") SalePoint salePoint);
}
