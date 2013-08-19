/**
 *
 */
package ru.extas.server;

import ru.extas.model.Policy;

import java.util.List;

/**
 * Интерфейс к страховым полисам БСО
 *
 * @author Valery Orlov
 */
public interface PolicyRegistry {

    /**
     * Возвращает список всех зарегестрированных полисов
     *
     * @param startIndex      начальный ндекс выборки
     * @param count           размер выборки
     * @param sortPropertyIds сортируемые свойства
     * @param sortStates      направление сортировки
     * @return
     */
    List<Policy> loadAll(int startIndex, int count, Object[] sortPropertyIds, boolean[] sortStates);

    /**
     * Запрашивает количество полисов в базе
     *
     * @return количество полисов в базе
     */
    int queryPoliciesCount();

    /**
     * Возвращает список всех доступных полисов
     *
     * @return список полисов
     */
    List<Policy> loadAvailable();

    /**
     * Сохраняет полис
     *
     * @param policy что сохраняем
     */
    void persist(Policy policy);

    /**
     * Забронировать полис
     *
     * @param policy бронируемый полис
     */
    void bookPolicy(Policy policy);

    /**
     * Реализовать полис
     *
     * @param policy реализуемый полис
     */
    void issuePolicy(Policy policy);

    /**
     * Найти полис по номеру
     *
     * @param regNum номер
     * @return найденный полис или null
     */
    Policy findByNum(String regNum);

    /**
     * Забронировать полис
     *
     * @param regNum номер резервируемого полиса
     */
    void bookPolicy(String regNum);

    /**
     * Реализовать полис
     *
     * @param regNum номер реализуемого полиса
     */
    void issuePolicy(String regNum);
}
