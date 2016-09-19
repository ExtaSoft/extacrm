package ru.extas.server.contacts;

import ru.extas.model.contacts.Company;
import ru.extas.security.SecuredRepository;

/**
 * Сервис обработки данных компании
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 13:53
 * @version $Id: $Id
 * @since 0.3.0
 */
public interface CompanyService extends SecuredRepository<Company> {

    String EXTREME_ASSISTANCE_ID = "598EB4D6-BC0A-4A2F-B445-52BEF27AC873";

    /**
     * Определяет является ли переданная компания Экстрим Ассистанс
     * @param company проверяемая компания
     * @return true если компания Экстрим Ассистанс
     */
    boolean isExtremeAssistance(Company company);

    /**
     * Определяет является ли переданная компания банком
     *
     * @param company проверяемая компания
     * @return true если компания является банком
     */
    boolean isBank(Company company);

    /**
     * Определяет является ли переданная компания дилером
     *
     * @param company проверяемая компания
     * @return true если компания является дилером
     */
    boolean isDealer(Company company);

    /**
     * Определяет является ли переданная компания дистрибьютором
     *
     * @param company проверяемая компания
     * @return true если компания является дистрибьютором
     */
    boolean isDistributor(Company company);

    /**
     * Определяет является ли переданная компания дистрибьютором или дилером
     *
     * @param company проверяемая компания
     * @return true если компания является дистрибьютором или дилером
     */
    boolean isDealerOrDistributor(Company company);

    /**
     * Определяет является ли переданная компания колл-центром
     *
     * @param company проверяемая компания
     * @return true если компания является колл-центром
     */
    boolean isCallcenter(Company company);

}
