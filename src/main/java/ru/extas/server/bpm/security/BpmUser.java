package ru.extas.server.bpm.security;

import org.activiti.engine.identity.User;
import ru.extas.model.UserProfile;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 19:28
 */
public class BpmUser implements User {

    private final UserProfile userProfile;

    public BpmUser(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String getId() {
        return userProfile.getLogin();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public String getFirstName() {
        return userProfile.getContact().getName();
    }

    @Override
    public void setFirstName(String firstName) {
    }

    @Override
    public void setLastName(String lastName) {
    }

    @Override
    public String getLastName() {
        return userProfile.getContact().getName();
    }

    @Override
    public void setEmail(String email) {
    }

    @Override
    public String getEmail() {
        return userProfile.getContact().getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String string) {
    }
}
