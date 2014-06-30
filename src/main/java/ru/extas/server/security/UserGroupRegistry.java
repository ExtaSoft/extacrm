package ru.extas.server.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.security.UserGroup;

/**
 * @author Valery Orlov
 *         Date: 17.06.2014
 *         Time: 16:14
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserGroupRegistry extends JpaRepository<UserGroup, String> {
}
