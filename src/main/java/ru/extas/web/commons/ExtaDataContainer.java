package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Контейтер для данных из базы
 * <p/>
 * Date: 12.09.13
 * Time: 22:50
 *
 * @author Valery Orlov
 */
public class ExtaDataContainer<TEntityType> extends JPAContainer<TEntityType> {

    private static final long serialVersionUID = -7891940552175752858L;

    /**
     * Creates a new <code>JPAContainer</code> instance for entities of class
     * <code>entityClass</code>. An entity provider must be provided using the
     * {@link #setEntityProvider(com.vaadin.addon.jpacontainer.EntityProvider) }
     * before the container can be used.
     *
     * @param entityClass the class of the entities that will reside in this container
     *                    (must not be null).
     */
    public ExtaDataContainer(final Class<TEntityType> entityClass) {
        super(entityClass);

        // We need an entity provider to create a container
        EntityProvider<TEntityType> entityProvider =
                new CachingLocalEntityProvider<>(entityClass);

        entityProvider.setEntityManagerProvider(new GuiceEntityManagerProvider());

        setEntityProvider(entityProvider);
    }

    private static class GuiceEntityManagerProvider implements EntityManagerProvider, Serializable {
        @Override
        public EntityManager getEntityManager() {
            return lookup(EntityManager.class);
        }
    }
}
