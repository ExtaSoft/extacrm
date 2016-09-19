package ru.extas.server.settings;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.settings.UserSettings;

/**
 * Доступ к настройкам
 *
 * Created by valery on 15.09.16.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserSettingsRegistry extends JpaRepository<UserSettings, String> {

    UserSettings findByUserIsNullAndName(String name);

}
