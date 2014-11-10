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

    public static final String COMPANY_CAT_DEALER = "Дилер";
    public static final String COMPANY_CAT_DISTRIBUTOR = "Дистрибьютор";
    public static final String COMPANY_CAT_BANK = "Банк";
    public static final String COMPANY_CAT_ASSURANCE = "Страховая компания";


    /**
     * Запрашивает список категорий компании
     *
     * @return коллекция категорий для компании
     */
    Collection<String> loadCompanyCategories();

}
