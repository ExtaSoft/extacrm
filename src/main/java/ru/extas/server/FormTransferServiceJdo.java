/**
 * 
 */
package ru.extas.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.FormTransfer;

import com.google.inject.Inject;

/**
 * @author Valery Orlov
 * 
 */
public class FormTransferServiceJdo implements FormTransferService {

	private final Logger logger = LoggerFactory.getLogger(FormTransferServiceJdo.class);

	private final A7FormService a7FormService;

	/**
	 * @param a7FormService
	 */
	@Inject
	public FormTransferServiceJdo(final A7FormService a7FormService) {
		super();
		this.a7FormService = a7FormService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.FormTransferService#persist(ru.extas.model.FormTransfer)
	 */
	@Override
	public void persist(final FormTransfer tf) {
		final PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			logger.info("Prsisting FormTransfer");
			pm.makePersistent(tf);
		} finally {
			pm.close();
		}
		a7FormService.changeOwner(tf.getFormNums(), tf.getToContact());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.FormTransferService#findByFormNum(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FormTransfer> findByFormNum(final String formNum) {
		logger.debug("Requesting FormTransfer by form number...");
		final PersistenceManager pm = PMF.get().getPersistenceManager();

		final Query q = pm.newQuery(FormTransfer.class);
		q.setFilter("formNums.contains(regNumPrm)");
		q.declareParameters("String regNumPrm");
		try {
			final List<FormTransfer> forms = (List<FormTransfer>)q.execute(formNum);
			if (!forms.isEmpty()) return forms;
			else return null;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

}
