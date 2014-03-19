package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import ru.extas.server.SpringEntityManagerProvider;

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
 * @version $Id: $Id
 */
public class ExtaDataContainer<TEntityType> extends JPAContainer<TEntityType> {

    private static final long serialVersionUID = -7891940552175752858L;

    /**
     * Creates a new <code>JPAContainer</code> instance for entities of class
     * <code>entityClass</code>. An entity provider must be provided using the
     * {@link #setEntityProvider(com.vaadin.addon.jpacontainer.EntityProvider)}
     * before the container can be used.
     *
     * @param entityClass the class of the entities that will reside in this container
     *                    (must not be null).
     */
    public ExtaDataContainer(final Class<TEntityType> entityClass) {
        super(entityClass);

        // We need an entity provider to create a container
        CachingLocalEntityProvider<TEntityType> entityProvider =
                new CachingLocalEntityProvider<>(entityClass);

        //entityProvider.setCacheEnabled(false);
        entityProvider.setEntitiesDetached(false);

        entityProvider.setEntityManagerProvider(new InjectEntityManagerProvider());

        setEntityProvider(entityProvider);
    }

    private static class InjectEntityManagerProvider implements EntityManagerProvider, Serializable {
        @Override
        public EntityManager getEntityManager() {
            return lookup(SpringEntityManagerProvider.class).getEntityManager();
        }
    }
}
