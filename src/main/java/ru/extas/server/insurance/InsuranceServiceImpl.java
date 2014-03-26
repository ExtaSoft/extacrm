package ru.extas.server.insurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.insurance.Insurance;
import ru.extas.server.users.UserManagementService;
import ru.extas.server.users.UserManagementServiceImpl;

import javax.inject.Inject;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class InsuranceServiceImpl implements InsuranceService {

    private final static Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

    @Inject
    private InsuranceRepository insuranceRepository;

    @Inject
    private PolicyService policyService;
    @Inject
    private A7FormService formService;
    @Inject
    private UserManagementService userService;


    /** {@inheritDoc} */
    @Transactional
    @Override
    public void saveAndIssue(Insurance insurance) {
        logger.debug("Persisting insurance: {}", insurance.getRegNum());
        insuranceRepository.save(insurance);
        policyService.issuePolicy(insurance.getRegNum());
        formService.spendForm(insurance.getA7Num());
    }

}
