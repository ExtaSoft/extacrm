package ru.extas.server.settings;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * Created by valery on 12.09.16.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class CrmSettingsImpl implements CrmSettings {

    @Override
    public String getFaviconPath() {
        final boolean isAAEnv = isAAEnv();
        String iconName;
        if (isAAEnv)
            iconName = "favicon-aa-64.ico";
        else
            iconName = "favicon-ea-48.ico";

        return "./VAADIN/themes/exta-valo/" + iconName;
    }

    protected boolean isAAEnv() {
        final String is_aa_env = System.getProperty("IS_AA_ENV");
        return is_aa_env != null && is_aa_env.equalsIgnoreCase("true");
    }

    @Override
    public boolean isDevServer() {
        final String is_dev_env = System.getProperty("IS_DEV_ENV");
        return is_dev_env != null && is_dev_env.equalsIgnoreCase("true");
    }

    @Override
    public String getAppTitle() {
        if (isAAEnv())
            return "Аренда Авто CRM";
        else
            return "Экстрим Асистанс CRM";
    }
}
