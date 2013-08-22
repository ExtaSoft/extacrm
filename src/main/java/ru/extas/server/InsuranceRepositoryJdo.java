package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 */
public class InsuranceRepositoryJdo implements InsuranceRepository {

    private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJdo.class);

    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#getAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<Insurance> loadAll() {
        logger.debug("Requesting insurances list...");
        unitOfWork.get().begin();
        Query q = pm.get().newQuery(Insurance.class);
        Map<String, Object> parameters = newHashMap();
        try {
            Subject subject = SecurityUtils.getSubject();
            // пользователю доступны только собственные записи
            if (subject.hasRole(UserRole.USER.getName())) {
                q.setFilter("createdBy == createdByPrm");
                q.declareParameters("String createdByPrm");
                parameters.put("createdByPrm", subject.getPrincipal());
            }
            Collection<Insurance> insurances = (Collection<Insurance>) q.executeWithMap(parameters);
            logger.info("Retrieved {} insuranses", insurances.size());

            return insurances;
        } finally {
            q.closeAll();
            unitOfWork.get().begin();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#create(ru.extas.model.Insurance)
     */
    @Override
    public void persist(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        unitOfWork.get().begin();
        try {
            pm.get().makePersistent(insurance);

            // TODO: Запускать транзакцию
            final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
            policyRepository.issuePolicy(insurance.getRegNum());
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#deleteById(java.lang.Long)
     */
    @Override
    public void deleteById(String id) {
        logger.debug("Deleting insurance with id: {}", id);
        unitOfWork.get().begin();
        try {
            pm.get().deletePersistent(pm.get().getObjectById(Insurance.class, id));
        } finally {
            unitOfWork.get().end();
        }
    }

}
