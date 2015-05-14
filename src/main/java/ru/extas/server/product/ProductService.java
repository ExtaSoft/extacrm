package ru.extas.server.product;

import ru.extas.model.contacts.SalePoint;
import ru.extas.model.sale.Product;

import java.util.List;
import java.util.Map;

/**
 * @author Valery Orlov
 *         Date: 13.05.2015
 *         Time: 13:39
 */
public interface ProductService {

    /**
     * Возвращает продукты доступные на торговой точке.
     *
     * @param salePoint торговая точка для которой ищутся продукты.
     *                  Может быть null, в этом случае возврацаются все активные продукты.
     * @return найденные продукты по типам
     */
    Map<Product.Type, List<Product>> findAvailableProducts(SalePoint salePoint);
}
