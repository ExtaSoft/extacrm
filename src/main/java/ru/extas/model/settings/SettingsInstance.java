package ru.extas.model.settings;

/**
 * Объект для хранения настроек
 * <p>
 * Created by valery on 15.09.16.
 */
public class SettingsInstance {

    // Заголовок приложения
    private String appTitle;

    // Путь к иконке приложения
    private String iconPath;

    // Показывать ли раздел идентификации в ТТ
    private boolean showSalePointIds;

    // Режим отладки
    private boolean devServer;

    public SettingsInstance() {
    }

    public SettingsInstance(final String appTitle, final String iconPath, final boolean isShowSalePointIds, final boolean isDevServer) {
        this.appTitle = appTitle;
        this.iconPath = iconPath;
        this.showSalePointIds = isShowSalePointIds;
        this.devServer = isDevServer;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(final String appTitle) {
        this.appTitle = appTitle;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(final String iconPath) {
        this.iconPath = iconPath;
    }

    public boolean isShowSalePointIds() {
        return showSalePointIds;
    }

    public void setShowSalePointIds(final boolean showSalePointIds) {
        this.showSalePointIds = showSalePointIds;
    }

    public boolean isDevServer() {
        return devServer;
    }

    public void setDevServer(final boolean devServer) {
        this.devServer = devServer;
    }
}
