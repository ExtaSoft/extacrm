package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.UserProfile;

/**
 * <p>UserRegistry interface.</p>
 *
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:33
 * @version $Id: $Id
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserRegistry extends CrudRepository<UserProfile, String> {

/**
 * <p>findByLogin.</p>
 *
 * @param login a {@link java.lang.String} object.
 * @return a {@link ru.extas.model.UserProfile} object.
 */
UserProfile findByLogin(String login);

}
