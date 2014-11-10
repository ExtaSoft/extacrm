/**
 *
 */
package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import ru.extas.model.contacts.*;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.ObjectSecurityRule_;
import ru.extas.web.commons.*;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (компании)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompaniesGrid extends ExtaGrid<Company> {

    private static final long serialVersionUID = 2299363623807745654L;

    /**
     * <p>Constructor for CompaniesGrid.</p>
     */
    public CompaniesGrid() {
        super(Company.class);
    }

    @Override
    public ExtaEditForm<Company> createEditForm(final Company company, final boolean isInsert) {
        return new CompanyEditForm(company);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new CompanyDataDecl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDataContainer<Company> container = new SecuredDataContainer<Company>(Company.class, ExtaDomain.COMPANY) {
            @Override
            protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root objectRoot, Predicate predicate, final Set permitRegions, final Set permitBrands) {
                if (!permitRegions.isEmpty()) {
                    final SetJoin<Company, SalePoint> spJoin = objectRoot.join(Company_.salePoints, JoinType.LEFT);
                    final Predicate regPredicate =
                            spJoin.get(SalePoint_.regAddress)
                                    .get(AddressInfo_.region)
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
        };
        container.sort(new Object[]{"name"}, new boolean[]{true});
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод новой компании в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование контактных данных"));

        return actions;
    }
}
