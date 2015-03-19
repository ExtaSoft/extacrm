package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecuredObject;
import ru.extas.security.AbstractSecurityFilter;
import ru.extas.security.SecurityFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Контейтер для данных из базы
 * <p>
 * Date: 12.09.13
 * Time: 22:50
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SecuredDataContainer<TEntityType extends IdentifiedObject> extends ExtaJpaContainer<TEntityType> {

    private final AbstractSecurityFilter<TEntityType> securityFilter;

    public SecuredDataContainer(final AbstractSecurityFilter<TEntityType> securityFilter) {
        super(securityFilter.getEntityClass());

        this.securityFilter = securityFilter;
        // Установить фильтр в соответствии с правами доступа пользователя
        getEntityProvider().setQueryModifierDelegate(
                new DefaultQueryModifierDelegate() {
                    @Override
                    public void filtersWillBeAdded(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final List<Predicate> predicates) {
                        if (cb == null || cq == null || predicates == null)
                            return;

                        final Predicate predicate = securityFilter.createSecurityPredicate(cb, cq);
                        if (predicate != null) {
                            predicates.add(predicate);
                            cq.distinct(true);
                        }
                    }
                }
        );
    }

    public AbstractSecurityFilter<TEntityType> getSecurityFilter() {
        return securityFilter;
    }

    public static <TSecuredType extends SecuredObject> SecuredDataContainer<TSecuredType> create(Class<TSecuredType> entityClass, ExtaDomain domain) {
        return new SecuredDataContainer<TSecuredType>(new SecurityFilter<TSecuredType>(entityClass, domain));
    }
}
