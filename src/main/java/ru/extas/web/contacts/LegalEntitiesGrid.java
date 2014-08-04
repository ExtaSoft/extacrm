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
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesGrid extends ExtaGrid {

	private static final long serialVersionUID = 2299363623807745654L;
	private final Company company;

	/**
	 * <p>Constructor for LegalEntitiesGrid.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesGrid(final Company company) {
		super(false);
		this.company = company;
		initialize();
	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new LegalEntityDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<LegalEntity> container = new SecuredDataContainer<>(LegalEntity.class, ExtaDomain.LEGAL_ENTITY);
		if (company != null)
			container.addContainerFilter(new Compare.Equal("company", company));
		container.addNestedContainerProperty("actualAddress.region");
		container.addNestedContainerProperty("company.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового Юридического лица в систему", Fontello.DOC_NEW) {
			@Override
			public void fire(Object itemId) {
				final LegalEntity entity = new LegalEntity();
				entity.setCompany(company);
				final BeanItem<LegalEntity> newObj = new BeanItem<>(entity);
				newObj.expandProperty("actualAddress");

				final LegalEntityEditForm editWin = new LegalEntityEditForm("Ввод нового юр. лица в систему", newObj);
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
				final BeanItem<LegalEntity> beanItem = new GridItem<>(table.getItem(itemId));
				beanItem.expandProperty("actualAddress");
				final LegalEntityEditForm editWin = new LegalEntityEditForm("Редактирование контактных данных", beanItem);
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
