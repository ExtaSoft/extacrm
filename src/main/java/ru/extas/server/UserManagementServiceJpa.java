package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Contact;
import ru.extas.model.Person;
import ru.extas.model.UserProfile;
import ru.extas.model.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Имплементация сервиса управления пользователями и правами доступа
 *
 * @author Valery Orlov
 */
@SuppressWarnings("unchecked")
public class UserManagementServiceJpa implements UserManagementService {

    private static final String SUPERUSER_LOGIN = "admin";
    private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJpa.class);
    @Inject
    private Provider<EntityManager> em;

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.UserManagementService#findUserByLogin(java.lang.String)
     */
    @Transactional
    @Override
    public UserProfile findUserByLogin(String login) {
        logger.debug("Finding user by login: {}...", login);
        if (login.equals(SUPERUSER_LOGIN)) {
            logger.debug("Returning superuser profile...");
            return getSuperuser();
        }

        Query q = em.get().createQuery("SELECT u FROM UserProfile u WHERE u.login = :loginPrm");
        q.setParameter("loginPrm", login);
        List<UserProfile> results = (List<UserProfile>) q.getResultList();
        if (!results.isEmpty()) {
            logger.debug("Found user with login name {}", login);
            return results.get(0);
        } else {
            logger.debug("User with login name {} doesn't found", login);
            return null;
        }
    }

    /**
     * Найти контакт пользователя по логину
     *
     * @param login логин
     * @return найденный контакт пользователя или null
     */
    @Transactional
    @Override
    public Contact findUserContactByLogin(final String login) {
        final UserProfile currentUser = findUserByLogin(login);
        return currentUser != null ? currentUser.getContact() : null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.UserManagementService#persistUser(ru.extas.model.UserProfile
     * )
     */
    @Transactional
    @Override
    public void persistUser(UserProfile user) {
        logger.debug("Persisting user with login name {}...", user.getLogin());
        if (user.getId() == null)
            em.get().persist(user);
        else
            em.get().merge(user);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#getSuperuser()
     */
    @Transactional
    @Override
    public UserProfile getSuperuser() {
        UserProfile user = new UserProfile();
        user.setLogin(SUPERUSER_LOGIN);
        Person contact = new Person();
        contact.setName("Global Superuser");
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

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#getCurrentUser()
     */
    @Transactional
    @Override
    public UserProfile getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return findUserByLogin((String) subject.getPrincipal());
    }

    /**
     * Получить контакт текущего пользователя
     *
     * @return контакт текущего пользователя
     */
    @Transactional
    @Override
    public Contact getCurrentUserContact() {
        final UserProfile currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getContact() : null;
    }

}
