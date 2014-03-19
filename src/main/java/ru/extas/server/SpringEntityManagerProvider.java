package ru.extas.server;

import org.springframework.stereotype.Service;

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
 */
@Service
public class SpringEntityManagerProvider {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    /**
     * <p>getEntityManager.</p>
     *
     * @return a {@link javax.persistence.EntityManager} object.
     */
    public EntityManager getEntityManager() {
        return em;
    }
}
