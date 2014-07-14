package ru.extas.server.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.security.ExtaPermission;
import ru.extas.model.security.UserProfile;

import java.util.List;

/**
 * <p>PermissionRegistry interface.</p>
 *
 * @author Valery Orlov
 *         Date: 17.06.2014
 *         Time: 16:14
 * @version $Id: $Id
 * @since 0.5.0
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface PermissionRegistry extends JpaRepository<ExtaPermission, String> {

    /**
     * <p>loadUserPermission.</p>
     *
     * @param user a {@link ru.extas.model.security.UserProfile} object.
     * @return a {@link java.util.List} object.
     */
    @Query("SELECT p FROM ExtaPermission p WHERE p.user = :user OR p.group IN (SELECT g FROM UserProfile u, u.groupList g WHERE u = :user)")
    List<ExtaPermission> loadUserPermission(@Param("user") UserProfile user);
}
