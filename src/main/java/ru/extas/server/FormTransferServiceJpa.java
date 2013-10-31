/**
 *
 */
package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.FormTransfer;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
@Repository
public class FormTransferServiceJpa implements FormTransferService {

    private final Logger logger = LoggerFactory.getLogger(FormTransferServiceJpa.class);
    @Inject
    private A7FormService a7FormService;
    @PersistenceContext
    private EntityManager em;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.FormTransferService#persist(ru.extas.model.FormTransfer)
     */
    @Transactional
    @Override
    public void persist(final FormTransfer tf) {
        logger.debug("Persisting FormTransfer");
        a7FormService.changeOwner(tf.getFormNums(), tf.getToContact());
        if (tf.getId() == null) {
            em.persist(tf);
        } else {
            em.merge(tf);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.FormTransferService#findByFormNum(java.lang.String)
     */
    @Transactional
    @Override
    public List<FormTransfer> findByFormNum(final String formNum) {
        logger.debug("Requesting FormTransfer by form number...");

        final Query q = em.createQuery("SELECT o FROM FormTransfer o WHERE :regNumPrm IN (o.formNums)");
        q.setParameter("regNumPrm", formNum);
        final List<FormTransfer> forms = (List<FormTransfer>) q.getResultList();
        if (!forms.isEmpty()) return forms;
        else return null;
    }

}
