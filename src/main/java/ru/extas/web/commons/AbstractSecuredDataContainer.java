package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;
import ru.extas.model.contacts.Employee;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecureAction;
import ru.extas.model.security.SecureTarget;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.UserManagementService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.collect.Iterables.getFirst;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 18.09.2014
 *         Time: 17:17
 */
public abstract class AbstractSecuredDataContainer<TEntityType extends IdentifiedObject> extends ExtaJpaContainer<TEntityType> {
    protected final ExtaDomain domain;

    public AbstractSecuredDataContainer(final Class<TEntityType> entityClass, final ExtaDomain domain) {
        super(entityClass);
        this.domain = domain;

        // Установить фильтр в соответствии с правами доступа пользователя
        setSecurityFilter();
    }

    /**
     * <p>setSecurityFilter.</p>
     */
    private void setSecurityFilter() {

        getEntityProvider().setQueryModifierDelegate(
                new DefaultQueryModifierDelegate() {
                    @Override
                    public void filtersWillBeAdded(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final List<Predicate> predicates) {
                        if (cb == null || cq == null || predicates == null)
                            return;

                        if (lookup(UserManagementService.class).isCurUserHasRole(UserRole.ADMIN))
                            return;

                        final Predicate predicate = createSecurityPredicate(cb, cq);
                        if (predicate != null) {
                            predicates.add(predicate);
                            cq.distinct(true);
                        }
                    }
                }
        );

    }

    private Predicate createSecurityPredicate(final CriteriaBuilder cb, final CriteriaQuery<?> cq) {
        Predicate predicate;
        final Root<TEntityType> objectRoot = (Root<TEntityType>) getFirst(cq.getRoots(), null);
        final UserManagementService securityService = lookup(UserManagementService.class);
        final Employee curUserContact = securityService.getCurrentUserEmployee();

        beginSecurityFilter();
        // Определить область видимости и Наложить фильтр в соответствии с областью видимости
        if (securityService.isPermittedTarget(domain, SecureTarget.ALL)) {
            // Доступно все, ничего не делаем кроме общего фильтра
            predicate = createPredicate4Target(cb, cq, SecureTarget.ALL);
        } else {
            // Если не все доступно, то добавляем проежде всего "собственные" объекты
            predicate = createPredicate4Target(cb, cq, SecureTarget.OWNONLY);

            if (securityService.isPermittedTarget(domain, SecureTarget.CORPORATE)) {
                final Predicate corpPredicate = createPredicate4Target(cb, cq, SecureTarget.CORPORATE);
                if (corpPredicate != null)
                    predicate = cb.or(predicate, corpPredicate);
            } else if (securityService.isPermittedTarget(domain, SecureTarget.SALE_POINT)) {
                final Predicate spPredicate = createPredicate4Target(cb, cq, SecureTarget.SALE_POINT);
                if (spPredicate != null)
                    predicate = cb.or(predicate, spPredicate);
            }
        }
        endSecurityFilter();
        return predicate;
    }

    protected void endSecurityFilter() {

    }

    protected void beginSecurityFilter() {

    }

    protected abstract Predicate createPredicate4Target(CriteriaBuilder cb, CriteriaQuery<?> cq, SecureTarget target);

    public boolean isInsertPermitted() {
        return lookup(UserManagementService.class).isPermitted(domain, null, SecureAction.INSERT);
    }

    public boolean isItemPermitAction(final String itemId, final SecureAction action) {
        final UserManagementService securityService = lookup(UserManagementService.class);

        // Прежде всего надо проверить разрешено ли действие для всех объектов
        if (securityService.isPermitted(domain, SecureTarget.ALL, action))
            return true;

        // Проверить, входит ли элемент в "собственные объекты"
        if (isItemFromTarget(itemId, SecureTarget.OWNONLY))
            return isPermitted4OwnedObj(itemId, action);

        // Проверить, входит ли элемент в "объекты торговой точки"
        if (isItemFromTarget(itemId, SecureTarget.SALE_POINT))
            return securityService.isPermitted(domain, SecureTarget.SALE_POINT, action);

        // Проверить, входит ли элемент в "Конпоративные объекты"
        if (isItemFromTarget(itemId, SecureTarget.CORPORATE))
            return securityService.isPermitted(domain, SecureTarget.CORPORATE, action);

        return false;
    }

    public boolean isPermitted4OwnedObj(final String itemId, final SecureAction action) {
        return true;
    }

    public boolean isItemFromTarget(final String itemId, final SecureTarget target) {
        beginSecurityFilter();

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery cq = cb.createQuery();
        final Root<TEntityType> root = cq.from(getEntityClass());
        final Predicate predicate = createPredicate4Target(cb, cq, target);
        cq.where(cb.and(cb.equal(root.get(IdentifiedObject_.id), itemId), predicate));
        cq.select(cb.countDistinct(root.get(IdentifiedObject_.id)));

        final Query qry = em.createQuery(cq);
        final Long results = (Long) qry.getSingleResult();

        endSecurityFilter();
        return results != 0;
    }
}
