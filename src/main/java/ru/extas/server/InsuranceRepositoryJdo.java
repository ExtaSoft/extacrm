package ru.extas.server;

import static com.google.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Insurance;
import ru.extas.model.PMF;
import ru.extas.model.UserRole;

/**
 * Имплементация сервиса управления имущественными страховками
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceRepositoryJdo implements InsuranceRepository {

	private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJdo.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Insurance> loadAll() {
		logger.debug("Requesting insuranses list...");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Insurance.class);
		Map<String, Object> parameters = newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			// пользователю доступны только собственные записи
			if (subject.hasRole(UserRole.USER.getName())) {
				q.setFilter("createdBy == createdByPrm");
				q.declareParameters("String createdByPrm");
				parameters.put("createdByPrm", subject.getPrincipal());
			}
			Collection<Insurance> insurances = (Collection<Insurance>) q.executeWithMap(parameters);
			logger.info("Retrieved {} insuranses", insurances.size());

			return insurances;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#create(ru.extas.model.Insurance)
	 */
	@Override
	public void persist(Insurance insurance) {
		logger.debug("Persisting insurance: {}", insurance.getRegNum());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(insurance);

			// TODO: Запускать транзакцию
			final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
			policyRepository.issuePolicy(insurance.getRegNum());
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#deleteById(java.lang.Long)
	 */
	@Override
	public void deleteById(String id) {
		logger.debug("Deleting insurance with id: {}", id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(Insurance.class, id));
		} finally {
			pm.close();
		}
	}

}
