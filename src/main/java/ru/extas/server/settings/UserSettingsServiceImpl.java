package ru.extas.server.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.settings.SettingsInstance;
import ru.extas.model.settings.UserSettings;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by valery on 12.09.16.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserSettingsServiceImpl implements UserSettingsService {

    private static final String EA_APP_ICON = "./VAADIN/themes/exta-valo/favicon-ea-48.ico";
    private static final String AA_APP_ICON = "./VAADIN/themes/exta-valo/favicon-aa-64.ico";
    static final String MAIN_SETTINGS = "MAIN_SETTINGS";

    @Inject
    private UserSettingsRegistry settingsRegistry;

    @Override
    public String getFaviconPath() {
        return loadMainSettings().getIconPath();
    }

    @Override
    public boolean isDevServer() {
        return loadMainSettings().isDevServer();
//        final String is_dev_env = System.getProperty("IS_DEV_ENV");
//        return is_dev_env != null && is_dev_env.equalsIgnoreCase("true");
    }

    @Override
    public String getAppTitle() {
        return loadMainSettings().getAppTitle();
    }

    @Override
    public boolean isShowSalePointIds() {
        return loadMainSettings().isShowSalePointIds();
    }

    @Cacheable(MAIN_SETTINGS)
    @Override
    public SettingsInstance loadMainSettings() {
        final UserSettings data = settingsRegistry.findByUserIsNullAndName(MAIN_SETTINGS);
        SettingsInstance settings = null;
        if (data != null) {
            final ObjectMapper mapper = new ObjectMapper();
            try {
                settings = mapper.readValue(data.getSettings(), SettingsInstance.class);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        } else
            settings = defaultMainSettings();

        return settings;
    }

    private SettingsInstance defaultMainSettings() {
        return new SettingsInstance("Экстрим Ассистанс CRM", EA_APP_ICON, true, false);
    }

    @CacheEvict(MAIN_SETTINGS)
    @Override
    public SettingsInstance saveMainSettings(SettingsInstance settings) {
        UserSettings data = settingsRegistry.findByUserIsNullAndName(MAIN_SETTINGS);
        if (data == null) {
            data = new UserSettings();
            data.setName(MAIN_SETTINGS);
        }
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter settingsJson = new StringWriter();
        try {
            mapper.writeValue(settingsJson, settings);
        } catch (final IOException e) {
            Throwables.propagate(e);
        }
        data.setSettings(settingsJson.toString());
        settingsRegistry.save(data);

        return settings;
    }

    @Override
    public List<String> getFaviconPathList() {
        return newArrayList(EA_APP_ICON, AA_APP_ICON);
    }
}
