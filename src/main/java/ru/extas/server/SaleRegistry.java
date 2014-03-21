package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.Sale;

/**
 * Интерфейс управления продажами
 *
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:31
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface SaleRegistry extends CrudRepository<Sale, String> {

}
