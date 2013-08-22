package ru.extas.server;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Contact;
import ru.extas.model.UserProfile;
import ru.extas.model.UserRole;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Имплементация сервиса управления пользователями и правами доступа
 *
 * @author Valery Orlov
 */
public class UserManagementServiceJdo implements UserManagementService {

    private static final String SUPERUSER_LOGIN = "admin";
    private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJdo.class);

    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.UserManagementService#findUserByLogin(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public UserProfile findUserByLogin(String login) {
        logger.info("Finding user by login: {}...", login);
        if (login.equals(SUPERUSER_LOGIN)) {
            logger.info("Returning superuser profile...");
            return getSuperuser();
        }
        unitOfWork.get().begin();
        Query q = pm.get().newQuery(UserProfile.class);
        q.setFilter("login == loginPrm");
        q.declareParameters("String loginPrm");

        try {
            List<UserProfile> results = (List<UserProfile>) q.execute(login);
            if (!results.isEmpty()) {
                logger.info("Found user with login name {}", login);
                return results.get(0);
            } else {
                logger.info("User with login name {} doesn't found", login);
                return null;
            }
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

    /**
     * Найти контакт пользователя по логину
     *
     * @param login логин
     * @return найденный контакт пользователя или null
     */
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
    @Override
    public void persistUser(UserProfile user) {
        unitOfWork.get().begin();
        try {
            logger.info("Prsisting user with login name {}...", user.getLogin());
            pm.get().makePersistent(user);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#loadUsers()
     */
    @Override
    public List<UserProfile> loadUsers() {
        unitOfWork.get().begin();
        try {
            logger.info("Requesting user list...");
            List<UserProfile> users = new ArrayList<>();
            Extent<UserProfile> extent = pm.get().getExtent(UserProfile.class, false);
            for (UserProfile user : extent) {
                users.add(user);
            }
            logger.info("Retrieved {} users", users.size());
            extent.closeAll();

            return users;
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#getSuperuser()
     */
    @Override
    public UserProfile getSuperuser() {
        UserProfile user = new UserProfile();
        user.setLogin(SUPERUSER_LOGIN);
        Contact contact = new Contact();
        contact.setName("Global Superuser");
        user.setContact(contact);
        user.setLogin(SUPERUSER_LOGIN);
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            // The app is running on App Engine...
            user.setPassword("wfrySeRJLIX11u4OdZqXzDky8pL0mk/Q8VDt8QwWCU8=");
            user.setPasswordSalt("Dgr6Vo1PB7h4FWEEKCVXuQ==");
        } else {
            user.setPassword("y+ajXewM2qsaZBocksvfYKIlMzQBPW9SXORl4npgLWc=");
            user.setPasswordSalt("YM8hMeHtHyPOa3eY+JmSVg==");
        }
        user.setRole(UserRole.ADMIN);
        return user;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#getCurrentUser()
     */
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
    @Override
    public Contact getCurrentUserContact() {
        final UserProfile currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getContact() : null;
    }

}
