package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.*;

import java.util.HashSet;
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
		addStyleName(ExtaTheme.BASE_VIEW);
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

				actions.add(new UIAction("Новый", "Ввод нового Юридического лица в систему", Fontello.DOC_NEW) {
					@Override
					public void fire(Object itemId) {
						final LegalEntity entity = new LegalEntity();
						entity.setCompany(company);

						final LegalEntityEditForm editWin = new LegalEntityEditForm(entity) {
							@Override
							protected LegalEntity saveObject(final LegalEntity obj) {
								((BeanItemContainer<LegalEntity>) container).addBean(obj);
								setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                                return obj;
                            }
						};
                        FormUtils.showModalWin(editWin);
					}
				});

				actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", Fontello.EDIT_3) {
					@Override
					public void fire(final Object itemId) {
						final LegalEntity legalEntity = GridItem.extractBean(table.getItem(itemId));
						final LegalEntityEditForm editWin = new LegalEntityEditForm(legalEntity) {
							@Override
							protected LegalEntity saveObject(final LegalEntity obj) {
								setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                                return obj;
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
