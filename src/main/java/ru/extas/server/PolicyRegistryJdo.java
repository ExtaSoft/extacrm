/**
 * 
 */
package ru.extas.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.PMF;
import ru.extas.model.Policy;

/**
 * @author Valery Orlov
 * 
 */
public class PolicyRegistryJdo implements PolicyRegistry {

	private final Logger logger = LoggerFactory.getLogger(PolicyRegistryJdo.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#loadAvailable()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Policy> loadAvailable() {
		logger.debug("Requesting available policies...");
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Policy.class);
		q.setFilter("issueDate == null && bookTime < expareTimePrm");
		q.setOrdering("bookTime, regNum ascending");
		q.declareParameters("DateTime expareTimePrm");
		q.declareImports("import org.joda.time.DateTime;");
		try {
			List<Policy> pilicies = (List<Policy>) q.execute(DateTime.now().minusHours(1));
			logger.info("Retrieved {} available policies", pilicies.size());

			return pilicies;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#persist(ru.extas.model.Policy)
	 */
	@Override
	public void persist(Policy policy) {
		logger.debug("Persisting policy: {}", policy.getRegNum());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(policy);
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#bookPolicy(ru.extas.model.Policy)
	 */
	@Override
	public void bookPolicy(Policy policy) {
		policy.setBookTime(DateTime.now());
		persist(policy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#issuePolicy(ru.extas.model.Policy)
	 */
	@Override
	public void issuePolicy(Policy policy) {
		policy.setIssueDate(DateTime.now());
		persist(policy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#loadAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Policy> loadAll(int startIndex, int count, Object[] sortPropertyIds, boolean[] sortStates) {
		logger.debug("Requesting all policies from {} count {}", startIndex, count);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Policy.class);
		QueryUtils.setOrdering(q, sortPropertyIds, sortStates);
		QueryUtils.setRange(q, startIndex, count);
		try {
			List<Policy> policies = (List<Policy>) q.execute();
			logger.info("Retrieved {} policies", policies.size());

			return policies;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#findByNum(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Policy findByNum(String regNum) {
		logger.debug("Requesting policy by number...");
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Policy.class);
		q.setFilter("regNum == regNumPrm");
		q.declareParameters("String regNumPrm");
		try {
			List<Policy> pilicies = (List<Policy>) q.execute(regNum);
			if (!pilicies.isEmpty())
				return pilicies.get(0);
			else
				return null;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#bookPolicy(java.lang.String)
	 */
	@Override
	public void bookPolicy(String regNum) {
		Policy policy = findByNum(regNum);
		if (policy != null) {
			bookPolicy(policy);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#issuePolicy(java.lang.String)
	 */
	@Override
	public void issuePolicy(String regNum) {
		Policy policy = findByNum(regNum);
		if (policy != null) {
			issuePolicy(policy);
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
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Policy.class);
		q.setResult("count(this)");
		try {
			long count = (long) q.execute();
			logger.debug("Policies count {}", count);
			return (int) count;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

}
