package ru.extas.server.settings;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.security.ExtaPermission;
import ru.extas.model.security.UserProfile;
import ru.extas.model.settings.UserGridState;

import java.util.Set;

/**
 * @author Valery Orlov
 *         Date: 01.12.2014
 *         Time: 1:09
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface UserGridStateRegistry extends JpaRepository<UserGridState, String> {

    UserGridState findByUserAndTableIdAndName(UserProfile user, String tableId, String name);

    Set<UserGridState> findByUserAndTableId(UserProfile user, String tableId);

    UserGridState findByUserAndTableIdAndDefaultState(UserProfile user, String tableId, boolean defaultState);
}
