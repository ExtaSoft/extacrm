package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.UserManagementService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 18.09.2014
 *         Time: 17:17
 */
public abstract class AbstractSecuredDataContainer<TEntityType extends IdentifiedObject> extends ExtaDataContainer<TEntityType> {
    protected ExtaDomain domain;
    protected UserManagementService securityService;

    public AbstractSecuredDataContainer(Class<TEntityType> entityClass, final ExtaDomain domain) {
        super(entityClass);
        this.domain = domain;
        this.securityService = lookup(UserManagementService.class);

        // Установить фильтр в соответствии с правами доступа пользователя
        setSecurityFilter();
    }

    /**
     * <p>setSecurityFilter.</p>
     */
    protected void setSecurityFilter() {

        getEntityProvider().setQueryModifierDelegate(
                new DefaultQueryModifierDelegate() {
                    @Override
                    public void filtersWillBeAdded(CriteriaBuilder cb, CriteriaQuery<?> cq, List<Predicate> predicates) {
                        if (cb == null || cq == null || predicates == null)
                            return;

                        if (securityService.isCurUserHasRole(UserRole.ADMIN))
                            return;

                        Predicate predicate = createSecurityPredicate(cb, cq);
                        if (predicate != null) {
                            predicates.add(predicate);
                            cq.distinct(true);
                        }
                    }
                }
        );

    }

    protected abstract Predicate createSecurityPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq);
}
