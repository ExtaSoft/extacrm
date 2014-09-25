package ru.extas.web.commons;

import ru.extas.model.common.SecuredObject;
import ru.extas.model.common.SecuredObject_;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person_;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.*;
import ru.extas.server.security.UserManagementService;

import javax.persistence.criteria.*;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Sets.newHashSet;
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
public class SecuredDataContainer<TEntityType extends SecuredObject> extends AbstractSecuredDataContainer<TEntityType> {

    /**
     * Creates a new <code>JPAContainer</code> instance for entities of class
     * <code>entityClass</code>. An entity provider must be provided using the
     * {@link #setEntityProvider(com.vaadin.addon.jpacontainer.EntityProvider)}
     * before the container can be used.
     *
     * @param entityClass the class of the entities that will reside in this container
     *                    (must not be null).
     * @param domain      a {@link ru.extas.model.security.ExtaDomain} object.
     */
    public SecuredDataContainer(final Class<TEntityType> entityClass, final ExtaDomain domain) {
        super(entityClass, domain);
    }

    @Override
    protected Predicate createPredicate4Target(CriteriaBuilder cb, CriteriaQuery<?> cq, SecureTarget target) {
        Predicate predicate = null;
        Root<TEntityType> objectRoot = (Root<TEntityType>) getFirst(cq.getRoots(), null);
        Person curUserContact = lookup(UserManagementService.class).getCurrentUserContact();

        switch (target) {
            case OWNONLY: {
                SetJoin<TEntityType, Person> associatesUsersRoot = objectRoot.join(SecuredObject_.associateUsers, JoinType.LEFT);
                predicate = cb.equal(associatesUsersRoot, curUserContact);
                break;
            }
            case SALE_POINT: {
                SetJoin<TEntityType, Person> associatesUsersRoot = objectRoot.join(SecuredObject_.associateUsers, JoinType.LEFT);
                Set<SalePoint> workPlaces = curUserContact.getWorkPlaces();
                predicate = createSalePointPredicate(cb, objectRoot, associatesUsersRoot, workPlaces);
                break;
            }
            case CORPORATE: {
                SetJoin<TEntityType, Person> associatesUsersRoot = objectRoot.join(SecuredObject_.associateUsers, JoinType.LEFT);
                Set<Company> companies = curUserContact.getEmployers();
                Set<SalePoint> workPlaces = newHashSet();
                for (Company company : companies) {
                    workPlaces.addAll(company.getSalePoints());
                }
                predicate = createSalePointPredicate(cb, objectRoot, associatesUsersRoot, workPlaces);
                break;
            }
            case ALL:
                predicate = composeWithAreaFilter(cb, objectRoot, null);
                break;
        }
        return predicate;
    }

    private Predicate createSalePointPredicate(CriteriaBuilder cb, Root<TEntityType> objectRoot, SetJoin<TEntityType, Person> associatesUsersRoot, Set<SalePoint> workPlaces) {
        if (workPlaces.isEmpty()) {
            SalePoint salePoint = new SalePoint();
            salePoint.setId("00-00");
            workPlaces.add(salePoint);
        }
        SetJoin<Person, SalePoint> workPlaceRoot = associatesUsersRoot.join(Person_.workPlaces, JoinType.LEFT);
        return composeWithAreaFilter(cb, objectRoot, workPlaceRoot.in(workPlaces));
    }

    private Predicate composeWithAreaFilter(CriteriaBuilder cb, Root<TEntityType> objectRoot, Predicate predicate) {
        UserProfile curUserProfile = lookup(UserManagementService.class).getCurrentUser();
        Set<String> permitRegions = newHashSet(curUserProfile.getPermitRegions());
        Set<String> permitBrands = newHashSet(curUserProfile.getPermitBrands());
        Set<UserGroup> groups = curUserProfile.getGroupList();
        if (groups != null) {
            for (UserGroup group : groups) {
                permitBrands.addAll(group.getPermitBrands());
                permitRegions.addAll(group.getPermitRegions());
            }
        }
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
