/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;

import java.util.List;

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

	protected final Company company;

	/**
	 * <p>Constructor for SalePointsGrid.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointsGrid(final Company company) {
		super(SalePoint.class, false);
		this.company = company;
		initialize();
	}

    @Override
    public ExtaEditForm<SalePoint> createEditForm(SalePoint salePoint, boolean isInsert) {
        return new SalePointEditForm(salePoint);
    }

    @Override
    public SalePoint createEntity() {
        final SalePoint entity = super.createEntity();
        entity.setCompany(company);
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
		final ExtaDataContainer<SalePoint> container = new SecuredDataContainer<>(SalePoint.class, ExtaDomain.SALE_POINT);
		container.addNestedContainerProperty("regAddress.region");
		container.addNestedContainerProperty("company.name");
		if (company != null)
			container.addContainerFilter(new Compare.Equal("company", company));
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового Контакта в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование контактных данных"));

        return actions;
	}
}
