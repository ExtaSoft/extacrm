package ru.extas.server;

import ru.extas.model.Lead;
import ru.extas.model.Sale;

/**
 * <p>SaleService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:27
 * @version $Id: $Id
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
