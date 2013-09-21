/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.A7Form;
import ru.extas.model.A7Form.Status;
import ru.extas.model.Contact;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
public class A7FormServiceJpa implements A7FormService {

    private final Logger logger = LoggerFactory.getLogger(A7FormServiceJpa.class);
    @Inject
    private Provider<EntityManager> em;
    @Inject
    private UserManagementService userService;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#persist(ru.extas.model.A7Form)
     */
    @Transactional
    @Override
    public void persist(final A7Form form) {
        logger.info("Persisting A-7 with num {}...", form.getRegNum());
        if (form.getId() == null)
            em.get().persist(form);
        else
            em.get().merge(form);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#spendForm(java.lang.String)
     */
    @Transactional
    @Override
    public void spendForm(final String formNum) {
        final A7Form form = findByNum(formNum);
        if (form != null && form.getStatus() != Status.SPENT) {
            form.setStatus(Status.SPENT);
            persist(form);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#changeOwner(java.util.List)
     */
    @Transactional
    @Override
    public void changeOwner(final List<String> formNums, final Contact owner) {
        for (String num : formNums) {
            A7Form form = findByNum(num);
            if (form != null) {
                form.setOwner(owner);
                em.get().merge(form);
            } else {
                // НовыЙ бланки
                form = new A7Form(num, owner);
                em.get().persist(form);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#changeStatus(java.util.List, ru.extas.model.A7Form.Status)
     */
    @Transactional
    @Override
    public void changeStatus(final List<String> formNums, final Status newStatus) {
        for (final String num : formNums) {
            A7Form form = findByNum(num);
            if (form != null) {
                form.setStatus(newStatus);
                em.get().merge(form);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#findByNum(java.lang.String)
     */
    @Transactional
    @Override
    public A7Form findByNum(final String formNum) {
        logger.debug("Requesting A-7 by number...");

        final Query q = em.get().createQuery(
                "SELECT a FROM A7Form a WHERE a.regNum = :regNumPrm");
        q.setParameter("regNumPrm", formNum);
        final List<A7Form> forms = (List<A7Form>) q.getResultList();
        if (!forms.isEmpty())
            return forms.get(0);
        else
            return null;
    }

    @Transactional
    @Override
    public List<A7Form> loadAvailable() {
        logger.debug("Requesting available A-7 forms...");

        // Поиск контакта пользователя
        final Contact owner = userService.getCurrentUserContact();
        final Query q = em.get().createQuery(
                "SELECT a FROM A7Form a WHERE a.owner = :ownerPrm AND a.status = :statusPrm");
        q.setParameter("ownerPrm", owner);
        q.setParameter("statusPrm", Status.NEW);

        final List<A7Form> forms = (List<A7Form>) q.getResultList();

        logger.debug("Retrieved {} available A-7 forms", forms.size());
        return forms;
    }
}
