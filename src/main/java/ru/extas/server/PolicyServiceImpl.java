/**
 *
 */
package ru.extas.server;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Policy;

import javax.inject.Inject;
import java.util.List;

/**
 * <p>PolicyServiceImpl class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PolicyServiceImpl implements PolicyService {

	private final static Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);

	@Inject
	private PolicyRegistry policyRegistry;

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.PolicyRegistry#loadAvailable()
	 */
	/** {@inheritDoc} */
	@Transactional
	@Override
	public List<Policy> loadAvailable() {
		logger.debug("Requesting available policies...");

		final List<Policy> policies = policyRegistry.findAvailableAtTime(DateTime.now().minusHours(1));

		logger.debug("Retrieved {} available policies", policies.size());
		return policies;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.PolicyRegistry#bookPolicy(ru.extas.model.Policy)
	 */
	/** {@inheritDoc} */
	@Transactional
	@Override
	public void bookPolicy(final Policy policy) {
		policy.setBookTime(DateTime.now());
		policyRegistry.save(policy);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.PolicyRegistry#issuePolicy(ru.extas.model.Policy)
	 */
	/** {@inheritDoc} */
	@Transactional
	@Override
	public void issuePolicy(final Policy policy) {
		policy.setIssueDate(DateTime.now());
		policyRegistry.save(policy);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.PolicyRegistry#bookPolicy(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Transactional
	@Override
	public void bookPolicy(final String regNum) {
		bookPolicy(policyRegistry.findByRegNum(regNum));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.PolicyRegistry#issuePolicy(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Transactional
	@Override
	public void issuePolicy(final String regNum) {
		issuePolicy(policyRegistry.findByRegNum(regNum));
	}

}
