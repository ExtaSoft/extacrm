package ru.extas.security;

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
import java.io.Serializable;

import static com.google.common.collect.Iterables.getFirst;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Абстрактный класс создающий предикат на основе прав доступа к объекту
 *
 * Created by valery on 20.03.15.
 */
public abstract class AbstractSecurityFilter<TEntityType extends IdentifiedObject> implements Serializable {

    protected final ExtaDomain domain;
    final Class<TEntityType> entityClass;

    public AbstractSecurityFilter(final Class<TEntityType> entityClass, final ExtaDomain domain) {
        this.domain = domain;
        this.entityClass = entityClass;
    }

    public Predicate createSecurityPredicate(final CriteriaBuilder cb, final CriteriaQuery<?> cq) {
        // Все разрешено для админа
        if (lookup(UserManagementService.class).isCurUserHasRole(UserRole.ADMIN))
            return null;

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

        boolean isPermit = false;

        // Прежде всего надо проверить разрешено ли действие для всех объектов
        if (!isPermit && securityService.isPermitted(domain, SecureTarget.ALL, action))
            isPermit = true;

        // Проверить, входит ли элемент в "объекты торговой точки"
        if (!isPermit && isItemFromTarget(itemId, SecureTarget.SALE_POINT))
            isPermit = securityService.isPermitted(domain, SecureTarget.SALE_POINT, action);

        // Проверить, входит ли элемент в "Конпоративные объекты"
        if (!isPermit && isItemFromTarget(itemId, SecureTarget.CORPORATE))
            isPermit = securityService.isPermitted(domain, SecureTarget.CORPORATE, action);

        // Проверить, входит ли элемент в "собственные объекты"
        if (!isPermit && isItemFromTarget(itemId, SecureTarget.OWNONLY))
            isPermit = securityService.isPermitted(domain, SecureTarget.OWNONLY, action) ||
                    isPermitted4OwnedObj(itemId, action);

        return isPermit;
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

    public ExtaDomain getDomain() {
        return domain;
    }

    public Class<TEntityType> getEntityClass() {
        return entityClass;
    }
}
