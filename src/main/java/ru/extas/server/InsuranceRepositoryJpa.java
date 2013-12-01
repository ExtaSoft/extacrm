package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class InsuranceRepositoryJpa implements InsuranceRepository {

    private final static Logger logger = LoggerFactory.getLogger(UserManagementServiceJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PolicyRegistry policyRepository;
    @Inject
    private A7FormService formService;
    @Inject
    private UserManagementService userService;

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
        // пользователю доступны только собственные записи
        if (userService.isCurUserHasRole(UserRole.USER)) {
            q = em.createQuery("SELECT i FROM Insurance i WHERE i.createdBy = :createdByPrm");
            q.setParameter("createdByPrm", userService.getCurrentUserLogin());
        } else
            q = em.createQuery("SELECT i FROM Insurance i");

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
            em.persist(insurance);
        } else {
            em.merge(insurance);
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
        em.remove(em.find(Insurance.class, id));
    }

}
