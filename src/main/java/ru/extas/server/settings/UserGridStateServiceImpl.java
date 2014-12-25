package ru.extas.server.settings;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.settings.UserGridState;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Реализация сервиса управления настройками таблиц
 *
 * @author Valery Orlov
 *         Date: 01.12.2014
 *         Time: 1:17
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserGridStateServiceImpl implements UserGridStateService {

    @Inject
    private UserGridStateRegistry stateRegistry;
    @Inject
    private UserManagementService userService;

    @Transactional
    @Override
    public void saveState(final String tableId, final String name, final String state) {
        final UserGridState gridState = new UserGridState();
        gridState.setUser(userService.getCurrentUser());
        gridState.setTableId(tableId);
        gridState.setName(name);
        gridState.setState(state);

        stateRegistry.save(gridState);
    }

    @Transactional
    @Override
    public void deleteState(final String tableId, final String name) {
        final UserGridState gridState = stateRegistry.findByUserAndTableIdAndName(userService.getCurrentUser(), tableId, name);
        if (gridState != null)
            stateRegistry.delete(gridState);
    }

    @Override
    public Set<UserGridState> loadStates(final String tableId) {
        return stateRegistry.findByUserAndTableId(userService.getCurrentUser(), tableId);
    }

    @Override
    public String getDefaultStateName(final String tableId) {
        final UserGridState gridState = stateRegistry.findByUserAndTableIdAndDefaultState(userService.getCurrentUser(), tableId, true);
        if (gridState != null)
            return gridState.getName();

        return null;
    }

    @Transactional
    @Override
    public void setDefaultState(final String tableId, final String name) {
        // Снимаем текущее состояние по умолчанию
        final UserGridState gridState = stateRegistry.findByUserAndTableIdAndDefaultState(userService.getCurrentUser(), tableId, true);
        if (gridState != null) {
            gridState.setDefaultState(false);
            stateRegistry.save(gridState);
        }
        if (!isNullOrEmpty(name)) {
            final UserGridState state = stateRegistry.findByUserAndTableIdAndName(userService.getCurrentUser(), tableId, name);
            if (state != null) {
                state.setDefaultState(true);
                stateRegistry.save(state);
            }
        }
    }
}
