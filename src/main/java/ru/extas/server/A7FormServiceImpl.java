/**
 *
 */
package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.A7Form;
import ru.extas.model.A7Form.Status;
import ru.extas.model.Contact;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Valery Orlov
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class A7FormServiceImpl implements A7FormService {

private final static Logger logger = LoggerFactory.getLogger(A7FormServiceImpl.class);

@Inject
private A7FormRepository formRepository;
@Inject
private UserManagementService userService;

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.A7FormRepository#spendForm(java.lang.String)
 */
@Transactional
@Override
public void spendForm(final String formNum) {
	final A7Form form = formRepository.findByRegNum(formNum);
	if (form != null && form.getStatus() != Status.SPENT) {
		form.setStatus(Status.SPENT);
		formRepository.save(form);
	}
}

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.A7FormRepository#changeOwner(java.util.List)
 */
@Transactional
@Override
public void changeOwner(final List<String> formNums, final Contact owner) {
	for (String num : formNums) {
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

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.A7FormRepository#changeStatus(java.util.List, ru.extas.model.A7Form.Status)
 */
@Transactional
@Override
public void changeStatus(final A7Form form, final Status newStatus) {
	if (form != null) {
		form.setStatus(newStatus);
		formRepository.save(form);
	}
}

@Transactional
@Override
public List<A7Form> loadAvailable() {
	logger.debug("Requesting available A-7 forms...");

	// Поиск контакта пользователя
	final Contact owner = userService.getCurrentUserContact();

	Status status = Status.NEW;
	final List<A7Form> forms = formRepository.findByOwnerAndStatus(owner, status);

	logger.debug("Retrieved {} available A-7 forms", forms.size());
	return forms;
}
}
