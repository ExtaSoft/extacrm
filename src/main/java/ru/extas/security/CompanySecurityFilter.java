package ru.extas.security;

import ru.extas.model.common.Address_;
import ru.extas.model.contacts.*;
import ru.extas.model.security.ExtaDomain;

import javax.persistence.criteria.*;
import java.util.Set;

public class CompanySecurityFilter extends SecurityFilter<Company> {
        public CompanySecurityFilter() {
            super(Company.class, ExtaDomain.COMPANY);
        }

        @Override
        protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root objectRoot, Predicate predicate, final Set permitRegions, final Set permitBrands) {
            if (!permitRegions.isEmpty()) {
                final SetJoin<Company, SalePoint> spJoin = objectRoot.join(Company_.salePoints, JoinType.LEFT);
                final Predicate regPredicate =
                        spJoin.get(SalePoint_.posAddress)
                                .get(Address_.regionWithType)
                                .in(permitRegions);
                predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
            }
            if (!permitBrands.isEmpty()) {
                final SetJoin<Company, LegalEntity> leJoin = objectRoot.join(Company_.legalEntities, JoinType.LEFT);
                final Predicate brPredicate =
                        leJoin.join(LegalEntity_.motorBrands, JoinType.LEFT)
                                .in(permitBrands);
                predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
            }
            return predicate;
        }
    }
