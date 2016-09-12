package ru.extas.server.settings;

/**
 * Настройки системы
 *
 * Created by valery on 12.09.16.
 */
public interface CrmSettings {

    /**
     * Возвращает путь файлу иконки
     *
     * @return путь к иконке
     */
    String getFaviconPath();

    /**
     * Возвращает истину если работа на тестовом сервере
     *
     * @return true если работаем на dev сервере, иначе false
     */
    boolean isDevServer();

    /**
     * Возвращает заголовок приложения
     *
     * @return название приложения
     */
    String getAppTitle();

    /**
     * Показывать ли раздел "Идентификация" в карточке торговой точки
     *
     * @return true - если нужно показывать
     */
    boolean isShowSalePointIds();
}
