package ru.extas.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.PMF;
import ru.extas.model.UserProfile;

/**
 * Имплементация сервиса управления пользователями и правами доступа
 * 
 * @author Valery Orlov
 * 
 */
public class UserManagementServiceJdo implements UserManagementService {

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
			List<UserProfile> users = new ArrayList<UserProfile>();
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

}
