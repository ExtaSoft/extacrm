package ru.extas.security;

import ru.extas.model.common.IdentifiedObject_;
import ru.extas.model.contacts.Employee;
import ru.extas.model.security.*;
import ru.extas.server.security.UserManagementService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by valery on 20.03.15.
 */
public class SecurityFilter<TEntityType extends SecuredObject> extends AbstractSecurityFilter<TEntityType> {

    private Join<TEntityType, ObjectSecurityRule> securityRuleJoin;

    public SecurityFilter(final Class<TEntityType> entityClass, final ExtaDomain domain) {
        super(entityClass, domain);
    }

    @Override
    protected Predicate createPredicate4Target(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final SecureTarget target) {
        Predicate predicate = null;
        final Root<TEntityType> objectRoot = (Root<TEntityType>) getFirst(cq.getRoots(), null);
        final Employee curUserContact = lookup(UserManagementService.class).getCurrentUserEmployee();

        switch (target) {
            case OWNONLY: {
                final MapJoin<ObjectSecurityRule, String, UserObjectAccess> usersRoot =
                        getSecurityRoleJoin(objectRoot)
                                .join(ObjectSecurityRule_.users, JoinType.LEFT);
                predicate = cb.equal(usersRoot.get(UserObjectAccess_.userId), curUserContact.getId());
                break;
            }
            case SALE_POINT: {
                final SetJoin<ObjectSecurityRule, String> salePointsRoot =
                        getSecurityRoleJoin(objectRoot)
                                .join(ObjectSecurityRule_.salePointIds, JoinType.LEFT);
                final Set<String> workPlaces = newHashSet();
                workPlaces.add(Optional.ofNullable(curUserContact.getWorkPlace()).map(w -> w.getId()).orElse(null));
                Optional.ofNullable(curUserContact.getUserProfile())
                        .map(p -> p.getSalePoints())
                        .ifPresent(s -> s.forEach(w -> workPlaces.add(w.getId())));
                predicate = composeWithAreaFilter(cb, objectRoot, salePointsRoot.in(workPlaces));
                break;
            }
            case CORPORATE: {
                final SetJoin<ObjectSecurityRule, String> companiesRoot = getSecurityRoleJoin(objectRoot)
                        .join(ObjectSecurityRule_.companyIds, JoinType.LEFT);
                predicate = composeWithAreaFilter(cb, objectRoot, cb.equal(companiesRoot, curUserContact.getCompany().getId()));
                break;
            }
            case ALL:
                predicate = composeWithAreaFilter(cb, objectRoot, null);
                break;
        }
        return predicate;
    }

    private Join<TEntityType, ObjectSecurityRule> getSecurityRoleJoin(final Root<TEntityType> objectRoot) {
        if (securityRuleJoin == null)
            securityRuleJoin = objectRoot.join(SecuredObject_.securityRule, JoinType.LEFT);
        return securityRuleJoin;
    }

    @Override
    protected void endSecurityFilter() {
        securityRuleJoin = null;
    }

    @Override
    protected void beginSecurityFilter() {
        securityRuleJoin = null;
    }

    private Predicate composeWithAreaFilter(final CriteriaBuilder cb, final Root<TEntityType> objectRoot, final Predicate predicate) {
        final UserProfile curUserProfile = lookup(UserManagementService.class).getCurrentUser();
        final Set<String> permitRegions = newHashSet(curUserProfile.getPermitRegions());
        final Set<String> permitBrands = newHashSet(curUserProfile.getPermitBrands());
        final Set<UserGroup> groups = curUserProfile.getGroupList();
        if (groups != null) {
            for (final UserGroup group : groups) {
                permitBrands.addAll(group.getPermitBrands());
                permitRegions.addAll(group.getPermitRegions());
            }
        }
        return createAreaPredicate(cb, objectRoot, predicate, permitRegions, permitBrands);
    }

    protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root<TEntityType> objectRoot, Predicate predicate, final Set<String> permitRegions, final Set<String> permitBrands) {
        if (!permitRegions.isEmpty()) {
            final Predicate regPredicate =
                    getSecurityRoleJoin(objectRoot)
                            .join(ObjectSecurityRule_.regions, JoinType.LEFT)
                            .in(permitRegions);
            predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
        }
        if (!permitBrands.isEmpty()) {
            final Predicate brPredicate =
                    getSecurityRoleJoin(objectRoot)
                            .join(ObjectSecurityRule_.brands, JoinType.LEFT)
                            .in(permitBrands);
            predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
        }
        return predicate;
    }

    @Override
    public boolean isPermitted4OwnedObj(final String itemId, final SecureAction action) {
        final Employee curUserContact = lookup(UserManagementService.class).getCurrentUserEmployee();

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery cq = cb.createQuery();
        final Root<TEntityType> root = cq.from(getEntityClass());

        final MapJoin<ObjectSecurityRule, String, UserObjectAccess> join = getSecurityRoleJoin(root)
                .join(ObjectSecurityRule_.users, JoinType.LEFT);
        cq.where(cb.and(
                cb.equal(join.join(UserObjectAccess_.userId, JoinType.LEFT), curUserContact.getId()),
                cb.equal(root.get(IdentifiedObject_.id), itemId)));
        cq.select(join.value().get(UserObjectAccess_.role));

        final Query qry = em.createQuery(cq);
        final AccessRole result = (AccessRole) qry.getSingleResult();
        if (result != null) {
            switch (result) {
                case READER:
                    return action == SecureAction.VIEW;
                case EDITOR:
                    return action != SecureAction.DELETE;
                case OWNER:
                    return true;
            }
        }
        return false;
    }
}
