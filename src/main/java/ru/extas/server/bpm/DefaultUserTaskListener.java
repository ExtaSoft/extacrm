package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateTask;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;

/**
 * Стандартный обработчик пользовательской задачи
 *
 * @author Valery Orlov
 *         Date: 19.11.13
 *         Time: 13:28
 * @version $Id: $Id
 * @since 0.3
 */
public class DefaultUserTaskListener {

    @Inject
    UserManagementService userManagementService;

    /**
     * <p>createTask.</p>
     *
     * @param task a {@link org.activiti.engine.delegate.DelegateTask} object.
     */
    public void createTask(final DelegateTask task) {
        final String user = userManagementService.getCurrentUserLogin();

        // Значения по умолчанию
        task.setAssignee(user);
        task.setOwner(user);
    }
}
