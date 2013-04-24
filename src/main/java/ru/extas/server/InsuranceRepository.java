
package ru.extas.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import ru.extas.model.Insurance;
import ru.extas.model.PMF;

public class InsuranceRepository {

	public Collection<Insurance> getAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<Insurance> insurances = new ArrayList<Insurance>();
		    Extent<Insurance> extent = pm.getExtent(Insurance.class, false);
		    for (Insurance insurance : extent) {
		        insurances.add(insurance);
		    }
		    extent.closeAll();
			
		    return insurances;
		} finally {
			pm.close();
		}
	}

	public void create(Insurance insurance) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		    pm.makePersistent(insurance);
		} finally {
			pm.close();
		}
	}

	public void deleteById(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(Insurance.class, id));
		} finally {
			pm.close();
		}
	}
}
