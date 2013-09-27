/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Policy;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
public class PolicyRegistryJpa implements PolicyRegistry {

    private final Logger logger = LoggerFactory.getLogger(PolicyRegistryJpa.class);
    @Inject
    private Provider<EntityManager> em;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#loadAvailable()
     */
    @Transactional
    @Override
    public List<Policy> loadAvailable() {
        logger.debug("Requesting available policies...");

        final Query q = em.get().createQuery(
                "SELECT p FROM Policy p " +
                        "WHERE p.issueDate IS NULL " +
                        "AND (p.bookTime IS NULL OR p.bookTime < :expireTimePrm) " +
                        "ORDER BY p.bookTime, p.regNum");
        q.setParameter("expireTimePrm", DateTime.now().minusHours(1));
        final List<Policy> policies = (List<Policy>) q.getResultList();

        logger.debug("Retrieved {} available policies", policies.size());
        return policies;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#persist(ru.extas.model.Policy)
     */
    @Transactional
    @Override
    public void persist(final Policy policy) {
        logger.debug("Persisting policy: {}", policy.getRegNum());
        if (policy.getId() == null)
            em.get().persist(policy);
        else
            em.get().merge(policy);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#bookPolicy(ru.extas.model.Policy)
     */
    @Transactional
    @Override
    public void bookPolicy(final Policy policy) {
        policy.setBookTime(DateTime.now());
        persist(policy);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#issuePolicy(ru.extas.model.Policy)
     */
    @Transactional
    @Override
    public void issuePolicy(final Policy policy) {
        if (policy.getIssueDate() != null && policy.getIssueDate() != null) {
            policy.setIssueDate(DateTime.now());
            persist(policy);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#findByNum(java.lang.String)
     */
    @Transactional
    @Override
    public Policy findByNum(final String regNum) {
        logger.debug("Requesting policy by number...");

        final Query q = em.get().createQuery("SELECT p FROM Policy p WHERE p.regNum = :regNumPrm");
        q.setParameter("regNumPrm", regNum);
        return (Policy) q.getSingleResult();
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#bookPolicy(java.lang.String)
     */
    @Transactional
    @Override
    public void bookPolicy(final String regNum) {
        bookPolicy(findByNum(regNum));
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#issuePolicy(java.lang.String)
     */
    @Transactional
    @Override
    public void issuePolicy(final String regNum) {
        issuePolicy(findByNum(regNum));
    }

}
