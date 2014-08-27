package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Window;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка юридических лиц
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 20:03
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesSelectField extends CustomField<Set> {

	private final Company company;

	/**
	 * <p>Constructor for LegalEntitiesSelectField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesSelectField(Company company) {
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
				final Set<LegalEntity> list = dataSource != null ? (Set<LegalEntity>) dataSource.getValue() : new HashSet<LegalEntity>();
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

				actions.add(new UIAction("Добавить", "Выбрать юридическое лицо осуществляющуе деятельность на торговой точке", Fontello.DOC_NEW) {
					@Override
					public void fire(Object itemId) {
						final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите юридическое лицо", company);
						selectWindow.addCloseListener(new Window.CloseListener() {

							@Override
							public void windowClose(final Window.CloseEvent e) {
								if (selectWindow.isSelectPressed()) {
									((BeanItemContainer<LegalEntity>) container).addBean(selectWindow.getSelected());
									setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                                    NotificationUtil.showSuccess("Юридическое лицо добавлено");
								}
							}
						});
						selectWindow.showModal();
					}
				});

				actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", Fontello.EDIT_3) {
					@Override
					public void fire(final Object itemId) {
						final LegalEntity bean = GridItem.extractBean(table.getItem(itemId));
						final LegalEntityEditForm editWin = new LegalEntityEditForm(bean) {
							@Override
							protected void saveObject(final LegalEntity obj) {
								setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
							}
						};
                        FormUtils.showModalWin(editWin);
					}
				});
				actions.add(new ItemAction("Удалить", "Удалить юр.лицо из компании", Fontello.TRASH) {
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
