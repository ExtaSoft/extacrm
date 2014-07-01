package ru.extas.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.security.UserProfile;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.PermissionRegistry;
import ru.extas.server.security.UserManagementService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поставщик пользователей из базы данных
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * Constant <code>HASH_ITERATIONS=1024</code>
     */
    public static final int HASH_ITERATIONS = 1024;

    private final static Logger logger = LoggerFactory.getLogger(UserRealm.class);

    /**
     * <p>Constructor for UserRealm.</p>
     */
    public UserRealm() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        this.setCredentialsMatcher(credentialsMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();

        // Find the thing that stores your user's roles.
        UserProfile userProfile = lookup(UserManagementService.class).findUserByLogin(username);
        if (userProfile == null) {
            logger.info("Principal not found for authorizing user with username: {}", username);
            return null;
        } else {
            final UserRole role = userProfile.getRole();
            logger.info("Authoriziong user {} with role: {}", username, role);
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.addRole(role.getName());
            grantPermissions(userProfile, authorizationInfo);
            return authorizationInfo;
        }
    }

    private void grantPermissions(final UserProfile userProfile, final SimpleAuthorizationInfo authorizationInfo) {
        grantDefaultPermissions(userProfile, authorizationInfo);
    }

    /**
     * Дает пользователя привилегии по умолчанию в соответствии с его ролью
     *  @param role              роль пользователя
     * @param authorizationInfo данные авторизации для заполнения привилегиями
     */
    private void grantDefaultPermissions(final UserProfile userProfile, final SimpleAuthorizationInfo authorizationInfo) {
        List<Permission> permissions = newArrayList();
        switch (userProfile.getRole()) {
            case USER: {
                PermissionRegistry permissionRegistry = lookup(PermissionRegistry.class);
                permissions.addAll(permissionRegistry.loadUserPermission(userProfile));
            }
            break;
            case ADMIN:
                permissions = DefaultRolePermissoin.createAdminPermissions();
                break;
            default:
                permissions = newArrayList();
        }
        authorizationInfo.addObjectPermissions(permissions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // DEBUG: Print Security configuration
        logger.debug("!!! CredentialsMatcher: {}", this.getCredentialsMatcher().toString());
        logger.debug("!!! HashAlgorithmName: {}", ((HashedCredentialsMatcher) getCredentialsMatcher()).getHashAlgorithmName());
        logger.debug("!!! HashIterations: {}", ((HashedCredentialsMatcher) getCredentialsMatcher()).getHashIterations());
        logger.debug("!!! StoredCredentialsHexEncoded: {}", ((HashedCredentialsMatcher) getCredentialsMatcher()).isStoredCredentialsHexEncoded());

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        if (usernamePasswordToken.getUsername() == null || usernamePasswordToken.getUsername().isEmpty()) {
            throw new UnknownAccountException("Authentication failed");
        }

        // Find the thing that stores your user's credentials. This may be the
        // same or different than
        // the thing that stores the roles.
        UserProfile principal = lookup(UserManagementService.class).findUserByLogin(usernamePasswordToken.getUsername());
        if (principal == null) {
            logger.info("Principal not found for user with username: {}", usernamePasswordToken.getUsername());
            return null;
        } else if (principal.isBlocked()) {
            logger.info("Access for user with username {} is forbidden", usernamePasswordToken.getUsername());
            throw new LockedAccountException();
        }

        logger.info("Principal found for authenticating user with username: {}", usernamePasswordToken.getUsername());

        ByteSource hashedCredentials = new SimpleByteSource(Base64.decode(principal.getPassword()));
        ByteSource credentialsSalt = new SimpleByteSource(Base64.decode(principal.getPasswordSalt()));
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                principal.getLogin(),
                hashedCredentials,
                credentialsSalt,
                getName());
        return authenticationInfo;
    }

    /**
     * Шифрует пароль в профиле пользователя. Предпологаетя, что пароль
     * находится в <code>user.getPassword()</code> в открытом виде.
     *
     * @param user профиль пользователя
     */
    public static void securePassword(UserProfile user) {
        // We'll use a Random Number Generator to generate salts. This is
        // much more secure than using a username as a salt or nothaving a
        // salt at all. Shiro makes this easy.
        // Note that a normal app would reference an attribute rather than
        // create a new RNG every time:
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        ByteSource salt = rng.nextBytes();

        String plainTextPassword = user.getPassword();
        // Now hash the plain-text password with the random salt and
        // multiple
        // iterations and then Base64-encode the value (requires less space
        // than Hex):
        String hashedPasswordBase64 = new Sha256Hash(plainTextPassword, salt, HASH_ITERATIONS).toBase64();

        // saveAndChangeOwner the salt with the new account. The HashedCredentialsMatcher
        // will need it later when handling login attempts:
        user.setPasswordSalt(salt.toBase64());
        user.setPassword(hashedPasswordBase64);
    }
}
