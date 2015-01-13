package ru.extas.server.settings;

import ru.extas.model.settings.UserGridState;

import java.util.Set;

/**
 * Интерфейс для управления настройками состояния таблиц
 *
 * @author Valery Orlov
 *         Date: 01.12.2014
 *         Time: 1:12
 */
public interface UserGridStateService {

    /**
     * Сохраняет состояние таблицы для текущего пользователя
     *
     * @param tableId идентификатор таблицы
     * @param name имя профиля (состояния)
     * @param state состояние таблици в виде JSON
     */
    void saveState(String tableId, String name, String state);

    /**
     * Удаляет состояние таблициы текущего пользователя
     *
     * @param tableId идентификатор таблицы
     * @param name имя профиля (состояния)
     */
    void deleteState(String tableId, String name);

    /**
     * Загружает состояния таблицы для текущего пользователя
     *
     * @param tableId таблица для которой загружаются состояния
     * @return набор состояний
     */
    Set<UserGridState> loadStates(String tableId);

    /**
     * Возвращает имя состояния таблицы по умолчанию
     *
     * @param tableId идентификатор таблицы
     * @return имя состояния
     */
    String getDefaultStateName(String tableId);

    /**
     * Устанавливает состояние по умолчанию для таблицы.
     * Если состояние {name} равно Null, то ни одно из состояний не будет назначено.
     *
     * @param tableId идентификатор таблицы
     * @param name имя состояния или null
     */
    void setDefaultState(String tableId, String name);
}
