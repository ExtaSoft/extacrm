package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Lead;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA имплементация службы управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:55
 */
@Repository
public class LeadServiceJpa implements LeadService {

    private final Logger logger = LoggerFactory.getLogger(LeadServiceJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void persist(Lead obj) {
        logger.debug("Persisting lead");
        if (obj.getId() == null)
            em.persist(obj);
        else
            em.merge(obj);
    }

}
