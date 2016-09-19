package ru.extas.model.settings;

import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.UserProfile;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Различные настройки системы относящиеся к пользователю
 *
 * @author Valery Orlov
 *         Date: 01.12.2014
 *         Time: 0:54
 */
@Entity
@Table(name = "USER_SETTINGS", indexes = {@Index(columnList = "USER_ID, NAME")})
public class UserSettings extends IdentifiedObject {

    @OneToOne(fetch = FetchType.LAZY)
    private UserProfile user;

    @Column(length = 50)
    @Size(max = 50)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String settings;

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }
}
