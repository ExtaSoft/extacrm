package ru.extas.security;

import ru.extas.model.contacts.*;
import ru.extas.model.security.ExtaDomain;

import javax.persistence.criteria.*;
import java.util.Set;

public class SalePointSecurityFilter extends SecurityFilter<SalePoint> {
    public SalePointSecurityFilter() {
        super(SalePoint.class, ExtaDomain.SALE_POINT);
    }

    @Override
    protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root<SalePoint> objectRoot, Predicate predicate, final Set<String> permitRegions, final Set<String> permitBrands) {
        if (!permitRegions.isEmpty()) {
            final Predicate regPredicate =
                    objectRoot.get(SalePoint_.regAddress)
                            .get(AddressInfo_.region)
                            .in(permitRegions);
            predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
        }
        if (!permitBrands.isEmpty()) {
            final SetJoin<SalePoint, LegalEntity> leJoin = objectRoot.join(SalePoint_.legalEntities, JoinType.LEFT);
            final Predicate brPredicate =
                    leJoin.join(LegalEntity_.motorBrands, JoinType.LEFT)
                            .in(permitBrands);
            predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
        }
        return predicate;
    }
}
