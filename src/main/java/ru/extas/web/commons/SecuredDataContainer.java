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
import ru.extas.server.users.UserManagementService;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

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

                        UserManagementService userService = lookup(UserManagementService.class);
                        Person curUserContact = userService.getCurrentUserContact();

                        Root<TEntityType> objectRoot = (Root<TEntityType>) cq.getRoots().iterator().next();

                        // Определить область видимости и Наложить фильтр в соответствии с областью видимости
                        if(userService.isPermittedTarget(domain, SecureTarget.ALL)) {
                            // Доступно все, ничего не делаем кроме общего фильтра
                            addAreaFilter(objectRoot, userService, predicates);
                        }
                        else if(userService.isPermittedTarget(domain, SecureTarget.CORPORATE)) {
                            Set<Company> companies = curUserContact.getEmployers();
                            if(!companies.isEmpty()) {
                                SetJoin<Person, Company> companiesRoot = objectRoot.join(SecuredObject_.associateUsers).join(Person_.employers);
                                predicates.add(companiesRoot.in(companies));
                            }
                            addAreaFilter(objectRoot, userService, predicates);
                        }
                        else if(userService.isPermittedTarget(domain, SecureTarget.SALE_POINT)) {
                            Set<SalePoint> workPlaces = curUserContact.getWorkPlaces();
                            if(!workPlaces.isEmpty()) {
                                SetJoin<Person, SalePoint> workPlaceRoot = objectRoot.join(SecuredObject_.associateUsers).join(Person_.workPlaces);
                                predicates.add(workPlaceRoot.in(workPlaces));
                            }
                            addAreaFilter(objectRoot, userService, predicates);
                        }
                        else if(userService.isPermittedTarget(domain, SecureTarget.OWNONLY)) {
                            SetJoin<TEntityType, Person> userRoot = objectRoot.join(SecuredObject_.associateUsers);

                            predicates.add(cb.equal(userRoot, curUserContact));
                        }
                        else
                            throw new SecurityException("Forbidden access to objects!!!");// Ничего недоступно
					}

                    protected void addAreaFilter(Root<TEntityType> objectRoot, UserManagementService userService, List<Predicate> predicates) {
                        UserProfile curUserProfile = userService.getCurrentUser();
                        Set<String> permitRegions = curUserProfile.getPermitRegions();
                        Set<String> permitBrands = curUserProfile.getPermitBrands();
                        if(!permitRegions.isEmpty()){
                            predicates.add(objectRoot.join(SecuredObject_.associateRegions).in(permitRegions));
                        }
                        if(!permitBrands.isEmpty()) {
                            predicates.add(objectRoot.join(SecuredObject_.associateBrands).in(permitBrands));
                        }
                    }
                });


    }

}
