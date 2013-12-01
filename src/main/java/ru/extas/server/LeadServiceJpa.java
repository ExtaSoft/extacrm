package ru.extas.server;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Lead;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * JPA имплементация службы управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:55
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LeadServiceJpa implements LeadService {

    private final static Logger logger = LoggerFactory.getLogger(LeadServiceJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private RuntimeService runtimeService;

    @Transactional
    @Override
    public void persist(Lead obj) {
        checkNotNull(obj);
        logger.debug("Persisting lead");
        if (obj.getId() == null)
            em.persist(obj);
        else
            em.merge(obj);
    }

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
        persist(obj);
        // Привязать лид к процессу
        runtimeService.setVariable(processInstance.getId(), "lead", obj);

        logger.debug("Started \"saleCreditProcess\" business process instance (id = {})", processInstance.getId());
    }

}
