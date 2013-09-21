package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 */
public class InsuranceRepositoryJpa implements InsuranceRepository {

    private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJpa.class);
    @Inject
    private Provider<EntityManager> em;
    @Inject
    private PolicyRegistry policyRepository;
    @Inject
    private A7FormService formService;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#getAll()
     */
    @Transactional
    @Override
    public Collection<Insurance> loadAll() {
        logger.debug("Requesting insurances list...");
        Query q;
        Subject subject = SecurityUtils.getSubject();
        // пользователю доступны только собственные записи
        if (subject.hasRole(UserRole.USER.getName())) {
            q = em.get().createQuery("SELECT i FROM Insurance i WHERE i.createdBy = :createdByPrm");
            q.setParameter("createdByPrm", subject.getPrincipal());
        } else
            q = em.get().createQuery("SELECT i FROM Insurance i");

        Collection<Insurance> insurances = (Collection<Insurance>) q.getResultList();
        logger.info("Retrieved {} insurances", insurances.size());

        return insurances;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#create(ru.extas.model.Insurance)
     */
    @Transactional
    @Override
    public void persist(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        if (insurance.getId() == null) {
            em.get().persist(insurance);
        } else {
            em.get().merge(insurance);
        }
        policyRepository.issuePolicy(insurance.getRegNum());
        formService.spendForm(insurance.getA7Num());
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.InsuranceRepository#deleteById(java.lang.Long)
     */
    @Transactional
    @Override
    public void deleteById(String id) {
        logger.debug("Deleting insurance with id: {}", id);
        em.get().remove(em.get().find(Insurance.class, id));
    }

}
