package ru.extas.model.common;

import javax.persistence.EntityManager;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 25.09.2014
 *         Time: 12:04
 */
public class ModelUtils {

    public static final int ENUM_STRING_LENGTH = 30;
    public static final int ACTIVITI_ID_LENGTH = 64;

    public static <TEntity extends IdentifiedObject> void evictCache(final TEntity entity) {
        if (entity != null) {
            final EntityManager entityManager = lookup(EntityManager.class);
            evictCache(entityManager, entity);
        }
    }

    public static <TEntity extends IdentifiedObject> void evictCache(final EntityManager entityManager, final TEntity entity) {
        if (entity != null)
            checkNotNull(entityManager)
                    .getEntityManagerFactory()
                    .getCache()
                    .evict(entity.getClass(), entity.getId());
    }
}
