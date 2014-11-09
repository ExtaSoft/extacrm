/**
 *
 */
package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.*;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.ObjectSecurityRule_;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (Точки продаж)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsGrid extends ExtaGrid<SalePoint> {

	private static final long serialVersionUID = 2299363623807745654L;

    private SupplierSer<Company> companySupplier;

    /**
	 * <p>Constructor for SalePointsGrid.</p>
	 *
	 */
	public SalePointsGrid() {
		super(SalePoint.class);
	}

    @Override
    public ExtaEditForm<SalePoint> createEditForm(final SalePoint salePoint, final boolean isInsert) {
        final SalePointEditForm form = new SalePointEditForm(salePoint);
        form.setCompanySupplier(companySupplier);
        return form;
    }

    @Override
    public SalePoint createEntity() {
        final SalePoint entity = super.createEntity();
        if(companySupplier != null)
            entity.setCompany(companySupplier.get());
        return entity;
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new SalePointsDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<SalePoint> container = new SecuredDataContainer<SalePoint>(SalePoint.class, ExtaDomain.SALE_POINT){
			@Override
			protected Predicate createAreaPredicate(CriteriaBuilder cb, Root<SalePoint> objectRoot, Predicate predicate, Set<String> permitRegions, Set<String> permitBrands) {
				if (!permitRegions.isEmpty()) {
                    final Predicate regPredicate =
							objectRoot.get(SalePoint_.regAddress)
									.get(AddressInfo_.region)
                                    .in(permitRegions);
                    predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
                }
				if (!permitBrands.isEmpty()) {
					SetJoin<SalePoint, LegalEntity> leJoin = objectRoot.join(SalePoint_.legalEntities, JoinType.LEFT);
					final Predicate brPredicate =
							leJoin.join(LegalEntity_.motorBrands, JoinType.LEFT)
									.in(permitBrands);
					predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
				}
				return predicate;
			}
		};
		container.addNestedContainerProperty("regAddress.region");
		container.addNestedContainerProperty("regAddress.city");
		container.addNestedContainerProperty("regAddress.streetBld");
		container.addNestedContainerProperty("company.name");
		if (companySupplier != null)
			container.addContainerFilter(new Compare.Equal("company", companySupplier.get()));
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод новой торговой точки в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование данных торговой точки"));

        return actions;
	}

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
