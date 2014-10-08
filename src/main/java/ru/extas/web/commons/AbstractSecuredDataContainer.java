package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.common.IdentifiedObject_;
import ru.extas.model.contacts.Person;
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
public abstract class AbstractSecuredDataContainer<TEntityType extends IdentifiedObject> extends ExtaDataContainer<TEntityType> {
    protected ExtaDomain domain;

    public AbstractSecuredDataContainer(Class<TEntityType> entityClass, final ExtaDomain domain) {
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
                    public void filtersWillBeAdded(CriteriaBuilder cb, CriteriaQuery<?> cq, List<Predicate> predicates) {
                        if (cb == null || cq == null || predicates == null)
                            return;

                        if (lookup(UserManagementService.class).isCurUserHasRole(UserRole.ADMIN))
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

    private Predicate createSecurityPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq) {
        Predicate predicate;
        Root<TEntityType> objectRoot = (Root<TEntityType>) getFirst(cq.getRoots(), null);
        UserManagementService securityService = lookup(UserManagementService.class);
        Person curUserContact = securityService.getCurrentUserContact();

        beginSecurityFilter();
        // Определить область видимости и Наложить фильтр в соответствии с областью видимости
        if (securityService.isPermittedTarget(domain, SecureTarget.ALL)) {
            // Доступно все, ничего не делаем кроме общего фильтра
            predicate = createPredicate4Target(cb, cq, SecureTarget.ALL);
        } else {
            // Если не все доступно, то добавляем проежде всего "собственные" объекты
            predicate = createPredicate4Target(cb, cq, SecureTarget.OWNONLY);

            if (securityService.isPermittedTarget(domain, SecureTarget.CORPORATE)) {
                Predicate corpPredicate = createPredicate4Target(cb, cq, SecureTarget.CORPORATE);
                if (corpPredicate != null)
                    predicate = cb.or(predicate, corpPredicate);
            } else if (securityService.isPermittedTarget(domain, SecureTarget.SALE_POINT)) {
                Predicate spPredicate = createPredicate4Target(cb, cq, SecureTarget.CORPORATE);
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

    public boolean isItemPermitAction(String itemId, SecureAction action) {
        UserManagementService securityService = lookup(UserManagementService.class);

        // Прежде всего надо проверить разрешено ли действие для всех объектов
        if (securityService.isPermitted(domain, SecureTarget.ALL, action))
            return true;

        // Проверить, входит ли элемент в "собственные объекты"
        if (isItemFromTarget(itemId, SecureTarget.OWNONLY))
            return securityService.isPermitted(domain, SecureTarget.OWNONLY, action);

        // Проверить, входит ли элемент в "объекты торговой точки"
        if (isItemFromTarget(itemId, SecureTarget.SALE_POINT))
            return securityService.isPermitted(domain, SecureTarget.SALE_POINT, action);

        // Проверить, входит ли элемент в "Конпоративные объекты"
        if (isItemFromTarget(itemId, SecureTarget.CORPORATE))
            return securityService.isPermitted(domain, SecureTarget.CORPORATE, action);

        return false;
    }

    public boolean isItemFromTarget(String itemId, SecureTarget target) {
        beginSecurityFilter();

        EntityManager em = lookup(EntityManager.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<TEntityType> root = cq.from(getEntityClass());
        Predicate predicate = createPredicate4Target(cb, cq, target);
        cq.where(cb.and(cb.equal(root.get(IdentifiedObject_.id), itemId), predicate));
        cq.select(cb.countDistinct(root.get(IdentifiedObject_.id)));

        Query qry = em.createQuery(cq);
        Long results = (Long) qry.getSingleResult();

        endSecurityFilter();
        return results != 0;
    }
}
