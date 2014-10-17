package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно с таблицей для выбора юр. лица
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 16:26
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitySelectWindow extends CloseOnlylWindow {

	private final Company company;
	private LegalEntity selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for LegalEntitySelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitySelectWindow(final String caption, final Company company) {
		super(caption);
		this.company = company;
        setWidth(800, Unit.PIXELS);
        setContent(new SelectGrid());
	}

	/**
	 * <p>isSelectPressed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSelectPressed() {
		return selectPressed;
	}

	private class SelectGrid extends LegalEntitiesGrid {
		private SelectGrid() {
			super(null);
		}

		@Override
		protected Container createContainer() {
            if (company != null) {
                final Set<LegalEntity> list = company.getLegalEntities();
                final BeanItemContainer<LegalEntity> itemContainer = new BeanItemContainer<>(LegalEntity.class);
                if (list != null) {
                    for (final LegalEntity item : list) {
                        itemContainer.addBean(item);
                    }
                }
                itemContainer.addNestedContainerProperty("regAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
                return itemContainer;
            } else {
                final ExtaDataContainer<LegalEntity> container = new ExtaDataContainer<>(LegalEntity.class);
                if (company != null)
                    container.addContainerFilter(new Compare.Equal("company", company));
                container.addNestedContainerProperty("regAddress.region");
                container.addNestedContainerProperty("company.name");
                return container;
            }
        }

		@Override
		protected List<UIAction> createActions() {
			final List<UIAction> actions = newArrayList();

			actions.add(new DefaultAction("Выбрать", "Выбрать выделенный в списке контакт и закрыть окно", Fontello.CHECK) {
				@Override
				public void fire(final Object itemId) {

					selected = extractBean(table.getItem(itemId));
					selectPressed = true;
					close();
				}
			});

			actions.addAll(super.createActions());

			return actions;
		}
	}

	/**
	 * <p>Getter for the field <code>selected</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.LegalEntity} object.
	 */
	public LegalEntity getSelected() {
		return selected;
	}
}
