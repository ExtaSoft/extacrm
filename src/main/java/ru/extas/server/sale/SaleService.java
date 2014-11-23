package ru.extas.server.sale;

import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.security.SecuredRepository;

import java.util.Set;

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
     * Возобновляет продажу и перемещает ее в открытые
     * @param sale - возобновляемая продажа
     */
    void reopenSale(Sale sale);

    /**
     * Возобновляет продажи и перемещает их в открытые
     *
     * @param sales возобновляемые продажи
     */
    void reopenSales(Set<Sale> sales);

    /**
     * Завершает продажи с указанным результатом
     *
     * @param sales   продажи
     * @param result результат завершения
     */
    void finishSales(Set<Sale> sales, Sale.Result result);
}
