package ru.extas.server;

import ru.extas.model.Lead;
import ru.extas.model.Sale;

/**
 * Интерфейс управления продажами
 *
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:31
 */
public interface SaleService {

    public void persist(Sale obj);


    /**
     * Создает продажу на основе лида
     *
     * @param lead лид для которого создается продажа
     * @return созданная продажа
     */
    Sale ctreateSaleByLead(Lead lead);
}
