package ru.extas.security;

import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.Employee_;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.insurance.A7Form;
import ru.extas.model.insurance.A7Form_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecureTarget;
import ru.extas.server.security.UserManagementService;

import javax.persistence.criteria.*;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

public class A7FormSecurityFilter extends AbstractSecurityFilter<A7Form> {
            public A7FormSecurityFilter() {
                super(A7Form.class, ExtaDomain.INSURANCE_A_7);
            }

            @Override
            protected Predicate createPredicate4Target(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final SecureTarget target) {
                Predicate predicate = null;
                final Root<A7Form> objectRoot = (Root<A7Form>) getFirst(cq.getRoots(), null);
                final Employee curUserContact = lookup(UserManagementService.class).getCurrentUserEmployee();

                switch (target) {
                    case OWNONLY:
                        predicate = cb.equal(objectRoot.get(A7Form_.owner), curUserContact);
                        break;
                    case SALE_POINT: {
                        final Set<SalePoint> workPlaces = newHashSet();
                        workPlaces.add(curUserContact.getWorkPlace());
                        Optional.ofNullable(curUserContact.getUserProfile())
                                .map(p -> p.getSalePoints())
                                .ifPresent(s -> workPlaces.addAll(s));
                        if (!isEmpty(workPlaces)) {
                            final Join<Employee, SalePoint> workPlaceRoot = objectRoot.join(A7Form_.owner, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            predicate = workPlaceRoot.in(workPlaces);
                        }
                        break;
                    }
                    case CORPORATE:
                        final Set<SalePoint> workPlaces = null;
                        final Set<Company> companies = newHashSet();
                        companies.add(curUserContact.getCompany());
                        for (final Company company : companies) {
                            workPlaces.addAll(company.getSalePoints());
                        }
                        if (!isEmpty(workPlaces)) {
                            final Join<Employee, SalePoint> workPlaceRoot = objectRoot.join(A7Form_.owner, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            workPlaceRoot.in(workPlaces);
                        }
                        break;
                    case ALL:
                        break;
                }

                return predicate;
            }
        }
