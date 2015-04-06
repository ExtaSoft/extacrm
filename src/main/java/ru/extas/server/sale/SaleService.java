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
    Sale createSaleByLead(Lead lead);

    /**
     * Завершает продажу
     *
     * @param sale продажа
     */
    void finishSale(Sale sale);

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
     * Завершает продажи
     *
     * @param sales   продажи
     */
    void finishSales(Set<Sale> sales);

    /**
     * Отменяет продажу с указанной причиной
     *
     * @param sale продажа
     * @param reason причина отмены
     */
    void cancelSale(Sale sale, Sale.CancelReason reason);

    /**
     * Отменяет набор продаж с указанной причиной
     * @param sales продажи
     * @param reason причина отмены
     */
    void cancelSales(Set<Sale> sales, Sale.CancelReason reason);
}
