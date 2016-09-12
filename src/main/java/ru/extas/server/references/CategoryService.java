package ru.extas.server.references;

import java.util.Collection;

/**
 * Поставляет набора категорий
 *
 * @author Valery Orlov
 *         Date: 04.11.2014
 *         Time: 13:46
 */
public interface CategoryService {

    String COMPANY_CAT_DEALER = "Дилер";
    String COMPANY_CAT_DISTRIBUTOR = "Дистрибьютор";
    String COMPANY_CAT_BANK = "Банк";
    String COMPANY_CAT_ASSURANCE = "Страховая компания";
    String COMPANY_CAT_CALLCENTER = "Колл-центр";
    String COMPANY_CAT_CLIENT = "Клиент";


    /**
     * Запрашивает список категорий компании
     *
     * @return коллекция категорий для компании
     */
    Collection<String> loadCompanyCategories();

}
