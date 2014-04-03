/**
 *
 */
package ru.extas.server.insurance;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.insurance.Policy;

import javax.inject.Inject;
import java.util.List;

/**
 * <p>PolicyServiceImpl class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PolicyRepositoryImpl implements PolicyService {

    private final static Logger logger = LoggerFactory.getLogger(PolicyRepositoryImpl.class);

    @Inject
    private PolicyRepository policyRepository;

    /** {@inheritDoc} */
    @Transactional
    @Override
    public List<Policy> loadAvailable() {
        logger.debug("Requesting available policies...");

        final List<Policy> policies = policyRepository.findAvailableAtTime(DateTime.now().minusHours(1));

        logger.debug("Retrieved {} available policies", policies.size());
        return policies;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public void bookPolicy(final Policy policy) {
        policy.setBookTime(DateTime.now());
        policyRepository.save(policy);
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public void issuePolicy(final Policy policy) {
        policy.setIssueDate(DateTime.now());
        policyRepository.save(policy);
    }


    /** {@inheritDoc} */
    @Transactional
    @Override
    public void bookPolicy(final String regNum) {
        bookPolicy(policyRepository.findByRegNum(regNum));
    }


    /** {@inheritDoc} */
    @Transactional
    @Override
    public void issuePolicy(final String regNum) {
        issuePolicy(policyRepository.findByRegNum(regNum));
    }

}
