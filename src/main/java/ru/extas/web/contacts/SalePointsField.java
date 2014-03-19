package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.Company;
import ru.extas.model.SalePoint;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.ItemAction;
import ru.extas.web.commons.UIAction;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Поле ввода/редактирования списка торговых точек компании
 *
 * @author Valery Orlov
 *         Date: 20.02.14
 *         Time: 14:46
 */
public class SalePointsField extends CustomField<List> {

	private final Company company;

	public SalePointsField(final Company company) {
		this.company = company;
		setBuffered(true);
		addStyleName("base-view");
		setSizeFull();
		setWidth(600, Unit.PIXELS);
	}

	@Override
	protected Component initContent() {
		SalePointsGrid grid = new SalePointsGrid(company) {
			@Override
			protected Container createContainer() {
				final Property dataSource = getPropertyDataSource();
				final List<SalePoint> list = dataSource != null ? (List<SalePoint>) dataSource.getValue() : new ArrayList<SalePoint>();
				BeanItemContainer<SalePoint> itemContainer = new BeanItemContainer<>(SalePoint.class);
				if (list != null) {
					for (final SalePoint item : list) {
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

				actions.add(new UIAction("Новый", "Ввод новой торговой точки в систему", "icon-doc-new") {
					@Override
					public void fire(Object itemId) {
						final SalePoint entity = new SalePoint();
						entity.setCompany(company);
						final BeanItem<SalePoint> newObj = new BeanItem<>(entity);
						newObj.expandProperty("actualAddress");

						final SalePointEditForm editWin = new SalePointEditForm("Ввод новой торговой точки в систему", newObj) {
							@Override
							protected void saveObject(final SalePoint obj) {
								((BeanItemContainer<SalePoint>) container).addBean(obj);
								setValue(((BeanItemContainer<SalePoint>) container).getItemIds());
							}
						};
						editWin.showModal();
					}
				});

				actions.add(new DefaultAction("Изменить", "Редактирование торговой точки", "icon-edit-3") {
					@Override
					public void fire(final Object itemId) {
						final BeanItem<SalePoint> beanItem = new GridItem<>(table.getItem(itemId));
						beanItem.expandProperty("actualAddress");
						final SalePointEditForm editWin = new SalePointEditForm("Редактирование торговой точки", beanItem) {
							@Override
							protected void saveObject(final SalePoint obj) {
								setValue(((BeanItemContainer<SalePoint>) container).getItemIds());
							}
						};
						editWin.showModal();
					}
				});
				actions.add(new ItemAction("Удалить", "Удалить торговую точку", "icon-trash") {
					@Override
					public void fire(final Object itemId) {
						container.removeItem(itemId);
						setValue(((BeanItemContainer<SalePoint>) container).getItemIds());
					}
				});
				return actions;
			}
		};
		grid.setSizeFull();

		return grid;
	}

	@Override
	public Class<? extends List> getType() {
		return List.class;
	}
}
