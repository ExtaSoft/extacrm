package ru.extas.web.commons;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.common.SecuredObject_;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person_;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.users.UserProfile;
import ru.extas.security.ExtaDomain;
import ru.extas.security.SecureTarget;
import ru.extas.security.UserRole;
import ru.extas.server.users.UserManagementService;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

import static ru.extas.server.ServiceLocator.lookup;

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
public class SecuredDataContainer<TEntityType extends SecuredObject> extends ExtaDataContainer<TEntityType> {

    private ExtaDomain domain;

    /**
     * Creates a new <code>JPAContainer</code> instance for entities of class
     * <code>entityClass</code>. An entity provider must be provided using the
     * {@link #setEntityProvider(com.vaadin.addon.jpacontainer.EntityProvider)}
     * before the container can be used.
     *
     * @param entityClass the class of the entities that will reside in this container
     *                    (must not be null).
     */
    public SecuredDataContainer(final Class<TEntityType> entityClass, ExtaDomain domain) {
        super(entityClass);
        this.domain = domain;

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
                    public void filtersWillBeAdded(CriteriaBuilder cb, CriteriaQuery<?> cq, List<Predicate> predicates) {
                        if (cb == null || cq == null || predicates == null)
                            return;

                        UserManagementService userService = lookup(UserManagementService.class);
                        if(userService.isCurUserHasRole(UserRole.ADMIN))
                            return;

                        Root<TEntityType> objectRoot = (Root<TEntityType>) cq.getRoots().iterator().next();
                        Person curUserContact = userService.getCurrentUserContact();
                        Predicate predicate = null;

                        // Определить область видимости и Наложить фильтр в соответствии с областью видимости
                        if (userService.isPermittedTarget(domain, SecureTarget.ALL)) {
                            // Доступно все, ничего не делаем кроме общего фильтра
                            predicate = composeWithAreaFilter(cb, objectRoot, userService, null);
                        } else {
                            // Если не все доступно, то добавляем проежде всего "собственные" объекты
                            SetJoin<TEntityType, Person> associatesUsersRoot = objectRoot.join(SecuredObject_.associateUsers, JoinType.LEFT);
                            predicate = cb.equal(associatesUsersRoot, curUserContact);

                            if (userService.isPermittedTarget(domain, SecureTarget.CORPORATE)) {
                                Set<Company> companies = curUserContact.getEmployers();
                                if (companies.isEmpty()) {
                                    Company company = new Company();
                                    company.setId("00-00");
                                    companies.add(company);
                                }
                                SetJoin<Person, Company> companiesRoot = associatesUsersRoot.join(Person_.employers, JoinType.LEFT);
                                predicate = cb.or(predicate, composeWithAreaFilter(cb, objectRoot, userService, companiesRoot.in(companies)));
                            } else if (userService.isPermittedTarget(domain, SecureTarget.SALE_POINT)) {
                                Set<SalePoint> workPlaces = curUserContact.getWorkPlaces();
                                if (workPlaces.isEmpty()) {
                                    SalePoint salePoint = new SalePoint();
                                    salePoint.setId("00-00");
                                    workPlaces.add(salePoint);
                                }
                                SetJoin<Person, SalePoint> workPlaceRoot = associatesUsersRoot.join(Person_.workPlaces, JoinType.LEFT);
                                predicate = cb.or(predicate, composeWithAreaFilter(cb, objectRoot, userService, workPlaceRoot.in(workPlaces)));
                            }
                        }
                        if (predicate != null) {
                            predicates.add(predicate);
                            cq.distinct(true);
                        }

                    }

                    protected Predicate composeWithAreaFilter(CriteriaBuilder cb, Root<TEntityType> objectRoot, UserManagementService userService, Predicate predicate) {
                        UserProfile curUserProfile = userService.getCurrentUser();
                        Set<String> permitRegions = curUserProfile.getPermitRegions();
                        Set<String> permitBrands = curUserProfile.getPermitBrands();
                        if (!permitRegions.isEmpty()) {
                            Predicate regPredicate = objectRoot.join(SecuredObject_.associateRegions, JoinType.LEFT).in(permitRegions);
                            predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
                        }
                        if (!permitBrands.isEmpty()) {
                            Predicate brPredicate = objectRoot.join(SecuredObject_.associateBrands, JoinType.LEFT).in(permitBrands);
                            predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
                        }
                        return predicate;
                    }
                }
        );


    }

}
