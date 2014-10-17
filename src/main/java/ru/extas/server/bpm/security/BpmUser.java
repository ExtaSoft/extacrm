package ru.extas.server.bpm.security;

import org.activiti.engine.identity.User;
import ru.extas.model.security.UserProfile;

/**
 * <p>BpmUser class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 19:28
 * @version $Id: $Id
 * @since 0.3
 */
public class BpmUser implements User {

    private final UserProfile userProfile;

    /**
     * <p>Constructor for BpmUser.</p>
     *
     * @param userProfile a {@link ru.extas.model.security.UserProfile} object.
     */
    public BpmUser(final UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return userProfile.getLogin();
    }

    /** {@inheritDoc} */
    @Override
    public void setId(final String id) {
    }

    /** {@inheritDoc} */
    @Override
    public String getFirstName() {
        return userProfile.getEmployee().getName();
    }

    /** {@inheritDoc} */
    @Override
    public void setFirstName(final String firstName) {
    }

    /** {@inheritDoc} */
    @Override
    public void setLastName(final String lastName) {
    }

    /** {@inheritDoc} */
    @Override
    public String getLastName() {
        return userProfile.getEmployee().getName();
    }

    /** {@inheritDoc} */
    @Override
    public void setEmail(final String email) {
    }

    /** {@inheritDoc} */
    @Override
    public String getEmail() {
        return userProfile.getEmployee().getEmail();
    }

    /** {@inheritDoc} */
    @Override
    public String getPassword() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setPassword(final String string) {
    }
}
