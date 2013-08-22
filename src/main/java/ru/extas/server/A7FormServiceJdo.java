/**
 *
 */
package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.A7Form;
import ru.extas.model.A7Form.Status;
import ru.extas.model.Contact;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * @author Valery Orlov
 */
public class A7FormServiceJdo implements A7FormService {

    private final Logger logger = LoggerFactory.getLogger(A7FormServiceJdo.class);
    @Inject
    private Provider<PersistenceManager> pm;
    @Inject
    private Provider<UnitOfWork> unitOfWork;
    @Inject
    private UserManagementService userService;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#persist(ru.extas.model.A7Form)
     */
    @Override
    public void persist(final A7Form form) {
        unitOfWork.get().begin();
        try {
            logger.info("Persisting A-7 with num {}...", form.getRegNum());
            pm.get().makePersistent(form);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#spendForm(java.lang.String)
     */
    @Override
    public void spendForm(final String formNum) {
        unitOfWork.get().begin();
        try {
            final A7Form form = findByNum(formNum);
            form.setStatus(Status.SPENT);
            persist(form);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#changeOwner(java.util.List)
     */
    @Override
    public void changeOwner(final List<String> formNums, final Contact owner) {
        unitOfWork.get().begin();
        try {
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
            pm.get().makePersistentAll(forms);
        } finally {
            unitOfWork.get().end();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.A7FormService#changeStatus(java.util.List, ru.extas.model.A7Form.Status)
     */
    @Override
    public void changeStatus(final List<String> formNums, final Status newStatus) {
        unitOfWork.get().begin();
        try {
            final List<A7Form> forms = findByNum(formNums);
            for (final A7Form form : forms)
                form.setStatus(newStatus);

            pm.get().makePersistentAll(forms);
        } finally {
            unitOfWork.get().end();
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

        unitOfWork.get().begin();
        final Query q = pm.get().newQuery(A7Form.class);
        try {
            q.setFilter("regNum == regNumPrm");
            q.declareParameters("String regNumPrm");
            final List<A7Form> forms = (List<A7Form>) q.execute(formNum);
            if (!forms.isEmpty()) return forms.get(0);
            else return null;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
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

        unitOfWork.get().begin();
        final Query q = pm.get().newQuery(A7Form.class);
        try {
            q.setFilter("regNumsPrm.contains(regNum)");
            q.declareParameters("java.util.List regNumsPrm");
            final List<A7Form> forms = (List<A7Form>) q.execute(formNums);
            if (forms.isEmpty()) return null;
            return forms;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<A7Form> loadAvailable() {
        logger.debug("Requesting available A-7 forms...");

        unitOfWork.get().begin();

        final Query q = pm.get().newQuery(A7Form.class);
        try {
            q.setFilter("owner == ownerPrm && status == statusPrm");
            q.setOrdering("regNum ascending");
            q.declareParameters("Contact ownerPrm, Enum statusPrm");
            q.declareImports("import ru.extas.model.Contact;");

            // Поиск контакта пользователя
            final Contact owner = userService.getCurrentUserContact();
            final List<A7Form> forms = (List<A7Form>) q.execute(owner, Status.NEW);

            logger.debug("Retrieved {} available A-7 forms", forms.size());
            return forms;
        } finally {
            q.closeAll();
            unitOfWork.get().end();
        }
    }
}
