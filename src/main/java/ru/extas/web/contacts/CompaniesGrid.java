/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.contacts.Company;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (компании)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompaniesGrid extends ExtaGrid {

	private static final long serialVersionUID = 2299363623807745654L;

	/**
	 * <p>Constructor for CompaniesGrid.</p>
	 */
	public CompaniesGrid() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new CompanyDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<Company> container = new SecuredDataContainer<>(Company.class, ExtaDomain.COMPANY);
		container.addNestedContainerProperty("actualAddress.region");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод новой компании в систему", "icon-doc-new") {
			@Override
			public void fire(Object itemId) {
				final BeanItem<Company> newObj = new BeanItem<>(new Company());
				newObj.expandProperty("actualAddress");

				final CompanyEditForm editWin = new CompanyEditForm("Ввод новой компании в систему", newObj);
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

		actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<Company> beanItem = new GridItem<>(table.getItem(itemId));
				beanItem.expandProperty("actualAddress");
				final String caption = String.format("Редактирование компании: %s", beanItem.getBean().getName());
				final CompanyEditForm editWin = new CompanyEditForm(caption, beanItem);
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
