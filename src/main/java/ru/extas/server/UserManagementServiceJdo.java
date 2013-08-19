package ru.extas.server;

import com.google.appengine.api.utils.SystemProperty;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery(UserProfile.class);
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
        }
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
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            logger.info("Prsisting user with login name {}...", user.getLogin());
            pm.makePersistent(user);
        } finally {
            pm.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.UserManagementService#loadUsers()
     */
    @Override
    public List<UserProfile> loadUsers() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            logger.info("Requesting user list...");
            List<UserProfile> users = new ArrayList<>();
            Extent<UserProfile> extent = pm.getExtent(UserProfile.class, false);
            for (UserProfile user : extent) {
                users.add(user);
            }
            logger.info("Retrieved {} users", users.size());
            extent.closeAll();

            return users;
        } finally {
            pm.close();
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
        user.setName("Global Superuser");
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

}
