package ru.extas.server;

import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;

/**
 * <p>SaleService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:27
 * @version $Id: $Id
 * @since 0.3
 */
public interface SaleService {
/**
 * Создает продажу на основе лида
 *
 * @param lead лид для которого создается продажа
 * @return созданная продажа
 */
Sale ctreateSaleByLead(Lead lead);
}
