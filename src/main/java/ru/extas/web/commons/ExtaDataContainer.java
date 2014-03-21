package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.security.EntitySecurityManager;
import ru.extas.security.SecurityUtils;
import ru.extas.server.SpringEntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Контейтер для данных из базы
 * <p/>
 * Date: 12.09.13
 * Time: 22:50
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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

		// Установить фильтр в соответствии с правами доступа пользователя
		setSecurityFilter();
	}

	/**
	 * <p>setSecurityFilter.</p>
	 */
	public void setSecurityFilter() {

		getEntityProvider().setQueryModifierDelegate(
				new DefaultQueryModifierDelegate() {
					@Override
					public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
						// Найти ссылку на соответствующий менеджер
						EntitySecurityManager securityManager = SecurityUtils.getSecurityManagerByClass(getEntityClass());
						securityManager.secureJpaQuery(criteriaBuilder, query, predicates);
					}
				});


    }

	private static class InjectEntityManagerProvider implements EntityManagerProvider, Serializable {
		@Override
		public EntityManager getEntityManager() {
			return lookup(SpringEntityManagerProvider.class).getEntityManager();
		}
	}
}
