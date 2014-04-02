package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.ItemAction;
import ru.extas.web.commons.UIAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка юридических лиц (из владельца)
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 20:03
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesField extends CustomField<Set> {

	private final Company company;

	/**
	 * <p>Constructor for LegalEntitiesField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesField(Company company) {
		this.company = company;
		setBuffered(true);
		addStyleName("base-view");
		setSizeFull();
		setWidth(600, Unit.PIXELS);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		LegalEntitiesGrid grid = new LegalEntitiesGrid(company) {
			@Override
			protected Container createContainer() {
				final Property dataSource = getPropertyDataSource();
				final List<LegalEntity> list = dataSource != null ? (List<LegalEntity>) dataSource.getValue() : new ArrayList<LegalEntity>();
				BeanItemContainer<LegalEntity> itemContainer = new BeanItemContainer<>(LegalEntity.class);
				if (list != null) {
					for (final LegalEntity item : list) {
						itemContainer.addBean(item);
					}
				}
				itemContainer.addNestedContainerProperty("actualAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
				return itemContainer;
			}

			@Override
			protected List<UIAction> createActions() {
				List<UIAction> actions = newArrayList();

				actions.add(new UIAction("Новый", "Ввод нового Юридического лица в систему", "icon-doc-new") {
					@Override
					public void fire(Object itemId) {
						final LegalEntity entity = new LegalEntity();
						entity.setCompany(company);
						final BeanItem<LegalEntity> newObj = new BeanItem<>(entity);
						newObj.expandProperty("actualAddress");

						final LegalEntityEditForm editWin = new LegalEntityEditForm("Ввод нового юр. лица в систему", newObj) {
							@Override
							protected void saveObject(final LegalEntity obj) {
								((BeanItemContainer<LegalEntity>) container).addBean(obj);
								setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
							}
						};
						editWin.showModal();
					}
				});

				actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", "icon-edit-3") {
					@Override
					public void fire(final Object itemId) {
						final BeanItem<LegalEntity> beanItem = new GridItem<>(table.getItem(itemId));
						beanItem.expandProperty("actualAddress");
						final LegalEntityEditForm editWin = new LegalEntityEditForm("Редактирование контактных данных", beanItem) {
							@Override
							protected void saveObject(final LegalEntity obj) {
								setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
							}
						};
						editWin.showModal();
					}
				});
				actions.add(new ItemAction("Удалить", "Удалить юр.лицо из компании", "icon-trash") {
					@Override
					public void fire(final Object itemId) {
						container.removeItem(itemId);
						setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
					}
				});
				return actions;
			}
		};
		grid.setSizeFull();

		return grid;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
