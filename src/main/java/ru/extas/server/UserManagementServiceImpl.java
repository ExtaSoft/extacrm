package ru.extas.server;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Person;
import ru.extas.model.UserProfile;
import ru.extas.model.UserRole;

import javax.inject.Inject;

/**
 * Имплементация сервиса управления пользователями и правами доступа
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@SuppressWarnings("unchecked")
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserManagementServiceImpl implements UserManagementService {

private static final String SUPERUSER_LOGIN = "admin";
private final static Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

@Inject
private UserRegistry userRegistry;

/*
 * (non-Javadoc)
 *
 * @see
 * ru.extas.server.UserManagementService#findUserByLogin(java.lang.String)
 */
/** {@inheritDoc} */
@Transactional
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

/**
 * {@inheritDoc}
 *
 * Найти контакт пользователя по логину
 */
@Transactional
@Override
public Person findUserContactByLogin(final String login) {
	final UserProfile currentUser = findUserByLogin(login);
	return currentUser != null ? currentUser.getContact() : null;
}

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.UserManagementService#getSuperuser()
 */
/** {@inheritDoc} */
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
/** {@inheritDoc} */
@Transactional
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
 *
 * Получить контакт текущего пользователя
 */
@Transactional
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
}
