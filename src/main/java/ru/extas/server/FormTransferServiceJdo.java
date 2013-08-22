/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.FormTransfer;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
public class FormTransferServiceJdo implements FormTransferService {

    private final Logger logger = LoggerFactory.getLogger(FormTransferServiceJdo.class);

    @Inject
    private A7FormService a7FormService;
    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;


    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.FormTransferService#persist(ru.extas.model.FormTransfer)
     */
    @Override
    public void persist(final FormTransfer tf) {
        unitOfWork.get().begin();
        try {
            logger.info("Persisting FormTransfer");
            pm.get().makePersistent(tf);
            a7FormService.changeOwner(tf.getFormNums(), tf.getToContact());
        } finally {
            unitOfWork.get().end();
        }
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
        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(FormTransfer.class);
        q.setFilter("formNums.contains(regNumPrm)");
        q.declareParameters("String regNumPrm");
        try {
            final List<FormTransfer> forms = (List<FormTransfer>) q.execute(formNum);
            if (!forms.isEmpty()) return forms;
            else return null;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

}
