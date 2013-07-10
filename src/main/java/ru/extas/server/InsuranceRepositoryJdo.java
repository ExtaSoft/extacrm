package ru.extas.server;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Insurance;
import ru.extas.model.PMF;

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
	@Override
	public Collection<Insurance> loadAll() {
		logger.debug("Requesting insuranses list...");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<Insurance> insurances = new ArrayList<Insurance>();
			Extent<Insurance> extent = pm.getExtent(Insurance.class, false);
			for (Insurance insurance : extent) {
				insurances.add(insurance);
			}
			extent.closeAll();
			logger.info("Retrieved {} insuranses", insurances.size());

			return insurances;
		} finally {
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
