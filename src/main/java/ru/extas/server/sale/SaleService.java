package ru.extas.server.sale;

import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.security.SecuredRepository;

/**
 * <p>SaleService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:27
 * @version $Id: $Id
 * @since 0.3
 */
public interface SaleService extends SecuredRepository<Sale> {
    /**
     * Создает продажу на основе лида
     *
     * @param lead лид для которого создается продажа
     * @return созданная продажа
     */
    Sale ctreateSaleByLead(Lead lead);

    /**
     * Завершает продажу с указанным результатом
     *
     * @param sale продажа
     * @param result результат завершения
     */
    void finishSale(Sale sale, Sale.Result result);

    /**
     *  Возобновляет продаду и перемещает ее в открытые
     * @param sale - возобновляемая продажа
     */
    void reopenSale(Sale sale);
}
