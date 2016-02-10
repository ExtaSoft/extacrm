package ru.extas.security;

import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.LegalEntity_;
import ru.extas.model.security.ExtaDomain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;

public class LegalEntitySecurityFilter extends SecurityFilter<LegalEntity> {
        public LegalEntitySecurityFilter() {
            super(LegalEntity.class, ExtaDomain.LEGAL_ENTITY);
        }

        @Override
        protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root objectRoot, Predicate predicate, final Set permitRegions, final Set permitBrands) {
            if (!permitRegions.isEmpty()) {
                final Predicate regPredicate =
                        objectRoot.get(LegalEntity_.Address)
                                .get(AddressInfo_.region)
                                .in(permitRegions);
                predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
            }
            if (!permitBrands.isEmpty()) {
                final Predicate brPredicate =
                        objectRoot.join(LegalEntity_.motorBrands, JoinType.LEFT)
                                .in(permitBrands);
                predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
            }
            return predicate;
        }
    }
