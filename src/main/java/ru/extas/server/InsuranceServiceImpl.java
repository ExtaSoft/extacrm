package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;

import javax.inject.Inject;

/**
 * Имплементация сервиса управления имущественными страховками
 *
 * @author Valery Orlov
 * @version $Id: $Id
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

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.InsuranceRepository#getAll()
 */
/** {@inheritDoc} */
@Transactional
@Override
public Iterable<Insurance> loadAll() {
	logger.debug("Requesting insurances list...");
	// пользователю доступны только собственные записи
	if (userService.isCurUserHasRole(UserRole.USER)) {
		return insuranceRepository.findByCreatedBy(userService.getCurrentUserLogin());
	} else
		return insuranceRepository.findAll();

}

/*
 * (non-Javadoc)
 *
 * @see ru.extas.server.InsuranceRepository#create(ru.extas.model.Insurance)
 */
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
