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

import ru.extas.model.Policy;
import ru.extas.vaadin.addon.jdocontainer.QueryUtils;

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
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(Policy.class);
		try {
			q.setFilter("issueDate == null && bookTime < expareTimePrm");
			q.setOrdering("bookTime, regNum ascending");
			q.declareParameters("DateTime expareTimePrm");
			q.declareImports("import org.joda.time.DateTime;");
			final List<Policy> pilicies = (List<Policy>)q.execute(DateTime.now().minusHours(1));
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
	public void persist(final Policy policy) {
		logger.debug("Persisting policy: {}", policy.getRegNum());
		final PersistenceManager pm = PMF.get().getPersistenceManager();
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
	public void bookPolicy(final Policy policy) {
		policy.setBookTime(DateTime.now());
		persist(policy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.PolicyRegistry#issuePolicy(ru.extas.model.Policy)
	 */
	@Override
	public void issuePolicy(final Policy policy) {
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
	public List<Policy> loadAll(final int startIndex, final int count, final Object[] sortPropertyIds,
			final boolean[] sortStates) {
		logger.debug("Requesting all policies from {} count {}", startIndex, count);
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(Policy.class);
		QueryUtils.setOrdering(q, sortPropertyIds, sortStates);
		QueryUtils.setRange(q, startIndex, count);
		try {
			final List<Policy> policies = (List<Policy>)q.execute();
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
	public Policy findByNum(final String regNum) {
		logger.debug("Requesting policy by number...");
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(Policy.class);
		q.setFilter("regNum == regNumPrm");
		q.declareParameters("String regNumPrm");
		try {
			final List<Policy> pilicies = (List<Policy>)q.execute(regNum);
			if (!pilicies.isEmpty()) return pilicies.get(0);
			else return null;
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
	public void bookPolicy(final String regNum) {
		final Policy policy = findByNum(regNum);
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
	public void issuePolicy(final String regNum) {
		final Policy policy = findByNum(regNum);
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
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(Policy.class);
		q.setResult("count(this)");
		try {
			final long count = (long)q.execute();
			logger.debug("Policies count {}", count);
			return (int)count;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

}
