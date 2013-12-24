package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.UserProfile;

/**
 * @author Valery Orlov
 *         Date: 19.12.13
 *         Time: 12:33
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserRegistry extends CrudRepository<UserProfile, String> {

UserProfile findByLogin(String login);

}
