package ru.extas.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Lead;

import javax.persistence.EntityManager;

/**
 * JPA имплементация службы управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:55
 */
public class LeadServiceJpa implements LeadService {

    private final Logger logger = LoggerFactory.getLogger(LeadService.class);

    @Inject
    private Provider<EntityManager> em;

    @Transactional
    @Override
    public void persist(Lead obj) {
        logger.debug("Persisting lead");
        if (obj.getId() == null)
            em.get().persist(obj);
        else
            em.get().merge(obj);
    }

}
