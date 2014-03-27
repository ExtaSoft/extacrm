package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import ru.extas.model.Company;
import ru.extas.model.LegalEntity;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно с таблицей для выбора юр. лица
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 16:26
 * @version $Id: $Id
 */
public class LegalEntitySelectWindow extends CloseOnlylWindow {

	private final Company company;
	private LegalEntity selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for LegalEntitySelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param company a {@link ru.extas.model.Company} object.
	 */
	public LegalEntitySelectWindow(final String caption, final Company company) {
		super(caption);
		this.company = company;
		addStyleName("base-view");
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
                final List<LegalEntity> list = company.getLegalEntities();
                BeanItemContainer<LegalEntity> itemContainer = new BeanItemContainer<>(LegalEntity.class);
                if (list != null) {
                    for (final LegalEntity item : list) {
                        itemContainer.addBean(item);
                    }
                }
                itemContainer.addNestedContainerProperty("actualAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
                return itemContainer;
            } else {
                return super.createContainer();
            }
        }

		@Override
		protected List<UIAction> createActions() {
			List<UIAction> actions = newArrayList();

			actions.add(new DefaultAction("Выбрать", "Выбрать выделенный в списке контакт и закрыть окно", "icon-check") {
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
	 * @return a {@link ru.extas.model.LegalEntity} object.
	 */
	public LegalEntity getSelected() {
		return selected;
	}
}
