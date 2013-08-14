/**
 * 
 */
package ru.extas.server;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.A7Form;
import ru.extas.model.A7Form.Status;
import ru.extas.model.Contact;

/**
 * @author Valery Orlov
 * 
 */
public class A7FormServiceJdo implements A7FormService {

	private final Logger logger = LoggerFactory.getLogger(A7FormServiceJdo.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#persist(ru.extas.model.A7Form)
	 */
	@Override
	public void persist(final A7Form form) {
		final PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			logger.info("Prsisting A-7 with num {}...", form.getRegNum());
			pm.makePersistent(form);
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#spendForm(java.lang.String)
	 */
	@Override
	public void spendForm(final String formNum) {
		final A7Form form = findByNum(formNum);
		form.setStatus(A7Form.Status.SPENT);
		persist(form);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#changeOwner(java.util.List)
	 */
	@Override
	public void changeOwner(final List<String> formNums, final Contact owner) {
		List<A7Form> forms = findByNum(formNums);
		if (forms != null) {
			for (final A7Form form : forms)
				form.setOwner(owner);
		} else {
			// Новые бланки
			forms = newArrayListWithCapacity(formNums.size());
			for (final String formNum : formNums)
				forms.add(new A7Form(formNum, owner));
		}
		final PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistentAll(forms);
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#changeStatus(java.util.List, ru.extas.model.A7Form.Status)
	 */
	@Override
	public void changeStatus(final List<String> formNums, final Status newStatus) {
		final List<A7Form> forms = findByNum(formNums);
		for (final A7Form form : forms)
			form.setStatus(newStatus);

		final PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistentAll(forms);
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#findByNum(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public A7Form findByNum(final String formNum) {
		logger.debug("Requesting A-7 by number...");
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(A7Form.class);
		q.setFilter("regNum == regNumPrm");
		q.declareParameters("String regNumPrm");
		try {
			final List<A7Form> forms = (List<A7Form>)q.execute(formNum);
			if (!forms.isEmpty()) return forms.get(0);
			else return null;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.A7FormService#findByNum(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<A7Form> findByNum(final List<String> formNums) {
		logger.debug("Requesting A-7 by numbers...");
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(A7Form.class);
		q.setFilter("regNumsPrm.contains(regNum)");
		q.declareParameters("java.util.List regNumsPrm");
		try {
			final List<A7Form> forms = (List<A7Form>)q.execute(formNums);
			if (forms.isEmpty()) return null;
			return forms;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

}
