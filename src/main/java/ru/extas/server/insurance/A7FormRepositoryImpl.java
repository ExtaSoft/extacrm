/**
 *
 */
package ru.extas.server.insurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Employee;
import ru.extas.model.insurance.A7Form;
import ru.extas.model.insurance.A7Form.Status;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;
import java.util.List;

/**
 * <p>A7FormServiceImpl class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class A7FormRepositoryImpl implements A7FormService {

    private final static Logger logger = LoggerFactory.getLogger(A7FormRepositoryImpl.class);

    @Inject
    private A7FormRepository formRepository;
    @Inject
    private UserManagementService userService;

    /** {@inheritDoc} */
    @Transactional
    @Override
    public void spendForm(final String formNum) {
        final A7Form form = formRepository.findByRegNum(formNum);
        if (form != null && form.getStatus() != Status.SPENT) {
            form.setStatus(Status.SPENT);
            formRepository.save(form);
        }
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public void changeOwner(final List<String> formNums, final Employee owner) {
        for (final String num : formNums) {
            A7Form form = formRepository.findByRegNum(num);
            if (form != null) {
                form.setOwner(owner);
                formRepository.save(form);
            } else {
                // НовыЙ бланки
                form = new A7Form(num, owner);
                formRepository.save(form);
            }
        }
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public void changeStatus(final A7Form form, final Status newStatus) {
        if (form != null) {
            form.setStatus(newStatus);
            formRepository.save(form);
        }
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public List<A7Form> loadAvailable() {
        logger.debug("Requesting available A-7 forms...");

        // Поиск контакта пользователя
        final Employee owner = userService.getCurrentUserEmployee();

        final Status status = Status.NEW;
        final List<A7Form> forms = formRepository.findByOwnerAndStatus(owner, status);

        logger.debug("Retrieved {} available A-7 forms", forms.size());
        return forms;
    }
}
