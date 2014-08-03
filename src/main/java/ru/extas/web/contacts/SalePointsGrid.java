/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
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
public class SalePointsGrid extends ExtaGrid {

	private static final long serialVersionUID = 2299363623807745654L;

	protected final Company company;

	/**
	 * <p>Constructor for SalePointsGrid.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointsGrid(final Company company) {
		super(false);
		this.company = company;
		initialize();
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
		container.addNestedContainerProperty("actualAddress.region");
		container.addNestedContainerProperty("company.name");
		if (company != null)
			container.addContainerFilter(new Compare.Equal("company", company));
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового Контакта в систему", Fontello.DOC_NEW) {
			@Override
			public void fire(Object itemId) {
				final SalePoint salePoint = new SalePoint();
				salePoint.setCompany(company);
				final BeanItem<SalePoint> newObj = new BeanItem<>(salePoint);
				newObj.expandProperty("actualAddress");

				final SalePointEditForm editWin = new SalePointEditForm("Ввод новой торговой точки в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", Fontello.EDIT_3) {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<SalePoint> beanItem = new GridItem<>(table.getItem(itemId));
				beanItem.expandProperty("actualAddress");
				final SalePointEditForm editWin = new SalePointEditForm("Редактирование данных торговой точки", beanItem);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainerItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});
		return actions;
	}
}
