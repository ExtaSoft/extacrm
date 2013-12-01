/**
 *
 */
package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.A7Form;
import ru.extas.model.A7Form.Status;
import ru.extas.model.Contact;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Valery Orlov
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class A7FormServiceJpa implements A7FormService {

    private final static Logger logger = LoggerFactory.getLogger(A7FormServiceJpa.class);

    @PersistenceContext
    private EntityManager em;
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
            em.persist(form);
        else
            em.merge(form);
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
                em.merge(form);
            } else {
                // НовыЙ бланки
                form = new A7Form(num, owner);
                em.persist(form);
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
    public void changeStatus(final A7Form form, final Status newStatus) {
        if (form != null) {
            form.setStatus(newStatus);
            persist(form);
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

        final Query q = em.createQuery(
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
        final Query q = em.createQuery(
                "SELECT a FROM A7Form a WHERE a.owner = :ownerPrm AND a.status = :statusPrm");
        q.setParameter("ownerPrm", owner);
        q.setParameter("statusPrm", Status.NEW);

        final List<A7Form> forms = (List<A7Form>) q.getResultList();

        logger.debug("Retrieved {} available A-7 forms", forms.size());
        return forms;
    }
}
