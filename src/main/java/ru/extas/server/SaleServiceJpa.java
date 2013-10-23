package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Sale;

import javax.persistence.EntityManager;

/**
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:33
 */
public class SaleServiceJpa implements SaleService {

    private final Logger logger = LoggerFactory.getLogger(SaleServiceJpa.class);

    @Inject
    private Provider<EntityManager> em;

    @Transactional
    @Override
    public void persist(Sale obj) {
        logger.debug("Persisting sale");
        if (obj.getId() == null)
            em.get().persist(obj);
        else
            em.get().merge(obj);
    }
}
