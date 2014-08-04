package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Поле ввода/редактирования списка торговых точек компании
 *
 * @author Valery Orlov
 *         Date: 20.02.14
 *         Time: 14:46
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsField extends CustomField<Set> {

	private final Company company;

	/**
	 * <p>Constructor for SalePointsField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointsField(final Company company) {
		this.company = company;
		setBuffered(true);
		addStyleName("base-view");
		setSizeFull();
		setWidth(600, Unit.PIXELS);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		SalePointsGrid grid = new SalePointsGrid(company) {
			@Override
			protected Container createContainer() {
				final Property dataSource = getPropertyDataSource();
				final Set<SalePoint> list = dataSource != null ? (Set<SalePoint>) dataSource.getValue() : new HashSet<SalePoint>();
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

				actions.add(new UIAction("Новый", "Ввод новой торговой точки в систему", Fontello.DOC_NEW) {
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
								setValue(newHashSet(((BeanItemContainer<SalePoint>) container).getItemIds()));
							}
						};
						editWin.showModal();
					}
				});

				actions.add(new DefaultAction("Изменить", "Редактирование торговой точки", Fontello.EDIT_3) {
					@Override
					public void fire(final Object itemId) {
						final BeanItem<SalePoint> beanItem = new GridItem<>(table.getItem(itemId));
						beanItem.expandProperty("actualAddress");
						final SalePointEditForm editWin = new SalePointEditForm("Редактирование торговой точки", beanItem) {
							@Override
							protected void saveObject(final SalePoint obj) {
								setValue(newHashSet(((BeanItemContainer<SalePoint>) container).getItemIds()));
							}
						};
						editWin.showModal();
					}
				});
				actions.add(new ItemAction("Удалить", "Удалить торговую точку", Fontello.TRASH) {
					@Override
					public void fire(final Object itemId) {
						container.removeItem(itemId);
						setValue(newHashSet(((BeanItemContainer<SalePoint>) container).getItemIds()));
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
