package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно выбора торговой точки
 *
 * @author Valery Orlov
 *         Date: 21.02.14
 *         Time: 13:40
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointSelectWindow extends CloseOnlylWindow {
	private SalePoint selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for SalePointSelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointSelectWindow(final String caption, final Company company) {
		super(caption);
		addStyleName(ExtaTheme.BASE_VIEW);
		setContent(new SelectGrid(company));
	}

	/**
	 * <p>isSelectPressed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSelectPressed() {
		return selectPressed;
	}

	private class SelectGrid extends SalePointsGrid {
		public SelectGrid(final Company company) {
			super(company);
		}

        @Override
        protected Container createContainer() {
            final ExtaDataContainer<SalePoint> container = new ExtaDataContainer<>(SalePoint.class);
            container.addNestedContainerProperty("actualAddress.region");
            container.addNestedContainerProperty("company.name");
            if (company != null)
                container.addContainerFilter(new Compare.Equal("company", company));
            return container;
        }

        @Override
		protected List<UIAction> createActions() {
			List<UIAction> actions = newArrayList();

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
	 * @return a {@link ru.extas.model.contacts.SalePoint} object.
	 */
	public SalePoint getSelected() {
		return selected;
	}
}
