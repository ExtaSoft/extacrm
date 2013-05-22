package ru.extas.model;

import java.util.HashSet;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class UserRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(UserRealm.class);

	private final SecurityManagerService mySecurityManagerService;

	@Inject
	public UserRealm(SecurityManagerService mySecurityManagerService) {
		// This is the thing that knows how to find user creds and roles
		this.mySecurityManagerService = mySecurityManagerService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

		String username = (String) principalCollection.getPrimaryPrincipal();

		// Find the thing that stores your user's roles.
		UserData principal = mySecurityManagerService.findMyPrincipalByUsername(username);
		if (principal == null) {
			logger.info("Principal not found for authorizing user with username: {}", username);
			return null;
		} else {
			logger.info("Authoriziong user {} with roles: {}", username, principal.getRoles());
			SimpleAuthorizationInfo result = new SimpleAuthorizationInfo(principal.getRoles());
			return result;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
		if (usernamePasswordToken.getUsername() == null || usernamePasswordToken.getUsername().isEmpty()) {
			throw new AuthenticationException("Authentication failed");
		}

		// Find the thing that stores your user's credentials. This may be the
		// same or different than
		// the thing that stores the roles.
		UserData principal = mySecurityManagerService.findMyPrincipalByUsername(usernamePasswordToken.getUsername());
		if (principal == null) {
			logger.info("Principal not found for user with username: {}", usernamePasswordToken.getUsername());
			return null;
		}

		logger.info("Principal found for authenticating user with username: {}", usernamePasswordToken.getUsername());

		return new SimpleAccount(principal.getLogin(), principal.getPassword(), getName(), principal.getRoles(), new HashSet<Permission>());
	}
}