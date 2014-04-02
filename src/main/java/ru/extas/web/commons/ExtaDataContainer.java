package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.server.SpringEntityManagerProvider;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 02.04.2014
 *         Time: 18:11
 */
public class ExtaDataContainer<TEntityType extends IdentifiedObject> extends JPAContainer<TEntityType> {

    private static final long serialVersionUID = -7891940552175752858L;

    public ExtaDataContainer(Class<TEntityType> entityClass) {
        super(entityClass);
        // We need an entity provider to create a container
        CachingLocalEntityProvider<TEntityType> entityProvider =
                new CachingLocalEntityProvider<>(entityClass);

        //entityProvider.setCacheEnabled(false);
        entityProvider.setEntitiesDetached(false);

        entityProvider.setEntityManagerProvider(new InjectEntityManagerProvider());

        setEntityProvider(entityProvider);
    }


    protected static class InjectEntityManagerProvider implements EntityManagerProvider, Serializable {
        @Override
        public EntityManager getEntityManager() {
            return lookup(SpringEntityManagerProvider.class).getEntityManager();
        }
    }
}
