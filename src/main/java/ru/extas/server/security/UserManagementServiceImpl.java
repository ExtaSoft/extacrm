package ru.extas.server.security;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.model.security.*;

import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Имплементация сервиса управления пользователями и правами доступа
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@SuppressWarnings("unchecked")
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserManagementServiceImpl implements UserManagementService {

    private static final String SUPERUSER_LOGIN = "admin";
    private final static Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Inject
    private UserRegistry userRegistry;

    /** {@inheritDoc} */
    @Override
    public UserProfile findUserByLogin(String login) {
        logger.debug("Finding user by login: {}...", login);
        if (login.equals(SUPERUSER_LOGIN)) {
            logger.debug("Returning superuser profile...");
            return getSuperuser();
        }

        UserProfile result = userRegistry.findByLogin(login);
        if (result != null) {
            logger.debug("Found user with login name {}", login);
            return result;
        } else {
            logger.debug("User with login name {} doesn't found", login);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Person findUserContactByLogin(final String login) {
        final UserProfile currentUser = findUserByLogin(login);
        return currentUser != null ? currentUser.getContact() : null;
    }

    /** {@inheritDoc} */
    @Override
    public UserProfile getSuperuser() {
        UserProfile user = new UserProfile();
        user.setLogin(SUPERUSER_LOGIN);
        Person contact = getSuperuserContact();
        user.setContact(contact);
        user.setLogin(SUPERUSER_LOGIN);
        String is_dev_env = System.getProperty("IS_DEV_ENV");
        if (is_dev_env != null && is_dev_env.equalsIgnoreCase("true")) {
            user.setPassword("y+ajXewM2qsaZBocksvfYKIlMzQBPW9SXORl4npgLWc=");
            user.setPasswordSalt("YM8hMeHtHyPOa3eY+JmSVg==");
        } else {
            user.setPassword("wfrySeRJLIX11u4OdZqXzDky8pL0mk/Q8VDt8QwWCU8=");
            user.setPasswordSalt("Dgr6Vo1PB7h4FWEEKCVXuQ==");
        }
        user.setRole(UserRole.ADMIN);
        return user;
    }

    /**
     * <p>getSuperuserContact.</p>
     *
     * @return a {@link ru.extas.model.contacts.Person} object.
     */
    protected Person getSuperuserContact() {
        Person contact = findUserContactByLogin("orlov@extremeassist.ru");
        if(contact == null) {
            contact = new Person();
            contact.setName("Global Superuser");
        }
        return contact;
    }

    /** {@inheritDoc} */
    @Override
    public UserProfile getCurrentUser() {
        return findUserByLogin(getCurrentUserLogin());
    }

    /** {@inheritDoc} */
    @Override
    public String getCurrentUserLogin() {
        Subject subject = SecurityUtils.getSubject();
        // TODO: Кинуть исключение если нет авторизованного пользователя
        return (String) subject.getPrincipal();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Получить контакт текущего пользователя
     */
    @Override
    public Person getCurrentUserContact() {
        final UserProfile currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getContact() : null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isUserAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCurUserHasRole(UserRole role) {
        Subject subject = SecurityUtils.getSubject();
        return subject.hasRole(role.getName());
    }

    /** {@inheritDoc} */
    @Override
    public void authenticate(String login, String password) {
        Subject subject = SecurityUtils.getSubject();
        final UsernamePasswordToken token = new UsernamePasswordToken(login, password);
        subject.login(token);

        // Логин в Activiti
        Authentication.setAuthenticatedUserId(login);
    }

    /** {@inheritDoc} */
    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        // Activiti logout
        Authentication.setAuthenticatedUserId(null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPermittedOneOf(final Set<ExtaDomain> domains) {
        checkNotNull(domains);

        Subject subject = SecurityUtils.getSubject();
        for (ExtaDomain domain : domains)
            if (subject.isPermitted(new ExtaPermission(domain)))
                return true;

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPermittedTarget(final ExtaDomain domain, final SecureTarget target) {
        checkNotNull(domain);
        checkNotNull(target);

        Subject subject = SecurityUtils.getSubject();

        return subject.isPermitted(new ExtaPermission(domain, target));
    }

}
