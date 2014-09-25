package ru.extas.server.sale;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;

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
public interface SaleRepository extends JpaRepository<Sale, String>, SaleService {

    // Найти продажу по лиду
    Sale findByLead(Lead lead);

}
