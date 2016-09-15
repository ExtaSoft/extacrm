package ru.extas.server.settings;

import ru.extas.model.settings.SettingsInstance;

import java.util.List;

/**
 * Настройки системы
 *
 * Created by valery on 12.09.16.
 */
public interface UserSettingsService {

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

    /**
     * Загружает основные настройки приложения
     *
     * @return объект настроек
     */
    SettingsInstance loadMainSettings();

    /**
     * Сохраняет общие настройки приложения
     *
     * @param settings объект настроек
     * @return объект настроек
     */
    SettingsInstance saveMainSettings(SettingsInstance settings);

    /**
     * Список иконок
     *
     * @return список путей к доступным иконкам
     */
    List<String> getFaviconPathList();
}
