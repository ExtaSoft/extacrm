/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Policy;
import ru.extas.vaadin.addon.jdocontainer.QueryUtils;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
public class PolicyRegistryJdo implements PolicyRegistry {

    private final Logger logger = LoggerFactory.getLogger(PolicyRegistryJdo.class);

    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#loadAvailable()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Policy> loadAvailable() {
        logger.debug("Requesting available policies...");
        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(Policy.class);
        try {
            q.setFilter("issueDate == null && bookTime < expareTimePrm");
            q.setOrdering("bookTime, regNum ascending");
            q.declareParameters("DateTime expareTimePrm");
            q.declareImports("import org.joda.time.DateTime;");
            final List<Policy> pilicies = (List<Policy>) q.execute(DateTime.now().minusHours(1));
            logger.info("Retrieved {} available policies", pilicies.size());

            return pilicies;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#persist(ru.extas.model.Policy)
     */
    @Override
    public void persist(final Policy policy) {
        logger.debug("Persisting policy: {}", policy.getRegNum());
        unitOfWork.get().begin();
        try {
            pm.get().makePersistent(policy);
        } finally {
            unitOfWork.get().begin();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#bookPolicy(ru.extas.model.Policy)
     */
    @Override
    public void bookPolicy(final Policy policy) {
        unitOfWork.get().begin();
        try {
            policy.setBookTime(DateTime.now());
            persist(policy);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#issuePolicy(ru.extas.model.Policy)
     */
    @Override
    public void issuePolicy(final Policy policy) {
        unitOfWork.get().begin();
        try {
            policy.setIssueDate(DateTime.now());
            persist(policy);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#loadAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Policy> loadAll(final int startIndex, final int count, final Object[] sortPropertyIds,
                                final boolean[] sortStates) {
        logger.debug("Requesting all policies from {} count {}", startIndex, count);
        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(Policy.class);
        QueryUtils.setOrdering(q, sortPropertyIds, sortStates);
        QueryUtils.setRange(q, startIndex, count);
        try {
            final List<Policy> policies = (List<Policy>) q.execute();
            logger.info("Retrieved {} policies", policies.size());

            return policies;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#findByNum(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Policy findByNum(final String regNum) {
        logger.debug("Requesting policy by number...");
        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(Policy.class);
        q.setFilter("regNum == regNumPrm");
        q.declareParameters("String regNumPrm");
        try {
            final List<Policy> pilicies = (List<Policy>) q.execute(regNum);
            if (!pilicies.isEmpty()) return pilicies.get(0);
            else return null;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#bookPolicy(java.lang.String)
     */
    @Override
    public void bookPolicy(final String regNum) {
        unitOfWork.get().begin();
        try {
            final Policy policy = findByNum(regNum);
            if (policy != null) {
                bookPolicy(policy);
            }
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#issuePolicy(java.lang.String)
     */
    @Override
    public void issuePolicy(final String regNum) {
        unitOfWork.get().begin();
        try {
            final Policy policy = findByNum(regNum);
            if (policy != null) {
                issuePolicy(policy);
            }
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.PolicyRegistry#queryPoliciesCount()
     */
    @Override
    public int queryPoliciesCount() {
        logger.debug("Requesting all policies count...");
        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(Policy.class);
        q.setResult("count(this)");
        try {
            final long count = (long) q.execute();
            logger.debug("Policies count {}", count);
            return (int) count;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

}
