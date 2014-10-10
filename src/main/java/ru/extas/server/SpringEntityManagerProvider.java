package ru.extas.server;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * Предоставляет EntityManager из контекста
 *
 * @author Valery Orlov
 *         Date: 21.11.13
 *         Time: 12:30
 * @version $Id: $Id
 * @since 0.3
 */
@Service
public class SpringEntityManagerProvider {
    @PersistenceContext
    private EntityManager em;

    /**
     * <p>getEntityManager.</p>
     *
     * @return a {@link javax.persistence.EntityManager} object.
     */
    public EntityManager getEntityManager() {
        return em;
    }

    @Transactional
    public void runInTransaction(Runnable operation) {
        operation.run();
    }

}
