package ru.extas.security;

import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.Employee_;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.insurance.FormTransfer;
import ru.extas.model.insurance.FormTransfer_;
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

public class FormTransferSecurityFilter extends AbstractSecurityFilter<FormTransfer> {
            public FormTransferSecurityFilter() {
                super(FormTransfer.class, ExtaDomain.INSURANCE_TRANSFER);
            }

            @Override
            protected Predicate createPredicate4Target(final CriteriaBuilder cb, final CriteriaQuery<?> cq, final SecureTarget target) {
                Predicate predicate = null;
                final Root<FormTransfer> objectRoot = (Root<FormTransfer>) getFirst(cq.getRoots(), null);
                final Employee curUserContact = lookup(UserManagementService.class).getCurrentUserEmployee();

                switch (target) {
                    case OWNONLY:
                        predicate = cb.or(
                                cb.equal(objectRoot.get(FormTransfer_.fromContact), curUserContact),
                                cb.equal(objectRoot.get(FormTransfer_.toContact), curUserContact));
                        break;
                    case SALE_POINT: {
                        final Set<SalePoint> workPlaces = newHashSet();
                        workPlaces.add(curUserContact.getWorkPlace());
                        Optional.ofNullable(curUserContact.getUserProfile())
                                .map(p -> p.getSalePoints())
                                .ifPresent(s -> workPlaces.addAll(s));
                        if (!isEmpty(workPlaces)) {
                            final Join<Employee, SalePoint> workPlaceRootF = objectRoot.join(FormTransfer_.fromContact, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            final Join<Employee, SalePoint> workPlaceRootT = objectRoot.join(FormTransfer_.toContact, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            predicate = cb.or(workPlaceRootF.in(workPlaces), workPlaceRootT.in(workPlaces));
                        }
                        break;
                    }
                    case CORPORATE: {
                        final Set<SalePoint> workPlaces = null;
                        final Set<Company> companies = newHashSet();
                        companies.add(curUserContact.getCompany());
                        for (final Company company : companies) {
                            workPlaces.addAll(company.getSalePoints());
                        }
                        if (!isEmpty(workPlaces)) {
                            final Join<Employee, SalePoint> workPlaceRootF = objectRoot.join(FormTransfer_.fromContact, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            final Join<Employee, SalePoint> workPlaceRootT = objectRoot.join(FormTransfer_.toContact, JoinType.LEFT).join(Employee_.workPlace, JoinType.LEFT);
                            predicate = cb.or(workPlaceRootF.in(workPlaces), workPlaceRootT.in(workPlaces));
                        }
                        break;
                    }
                    case ALL:
                        break;
                }

                return predicate;
            }
        }
