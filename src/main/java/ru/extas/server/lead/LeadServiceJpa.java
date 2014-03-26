package ru.extas.server.lead;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.lead.Lead;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * JPA имплементация службы управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:55
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LeadServiceJpa implements LeadService {

private final static Logger logger = LoggerFactory.getLogger(LeadServiceJpa.class);

@Inject
private LeadRepository leadRepository;
@Inject
private RuntimeService runtimeService;

/** {@inheritDoc} */
@Transactional
@Override
public void qualify(Lead obj) {
	checkNotNull(obj);
	checkState(obj.getClient() != null, "Can't qualify lead without client link!");
	checkState(obj.getVendor() != null, "Can't qualify lead without vendor link!");
	checkState(obj.getStatus() == Lead.Status.NEW, "Can't qualify not new lead!");

	// запуск БП
	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("saleCreditProcess");
	// Привязать процесс к лиду
	obj.setProcessId(processInstance.getId());
	// Статус
	obj.setStatus(Lead.Status.QUALIFIED);
	// Сохранить изменения
	leadRepository.save(obj);
	// Привязать лид к процессу
	runtimeService.setVariable(processInstance.getId(), "lead", obj);

	logger.debug("Started \"saleCreditProcess\" business process instance (id = {})", processInstance.getId());
}

}
