package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Sale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:33
 */
@Repository
public class SaleServiceJpa implements SaleService {

    private final Logger logger = LoggerFactory.getLogger(SaleServiceJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void persist(Sale obj) {
        logger.debug("Persisting sale");
        if (obj.getId() == null)
            em.persist(obj);
        else
            em.merge(obj);
    }
}
