package ru.extas.web.product;

import ru.extas.model.sale.Product;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно для выбора продукта
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 17:11
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductSelectWindow extends CloseOnlylWindow {
	private Product selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for ProductSelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public ProductSelectWindow(final String caption) {
		super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
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

	private class SelectGrid extends ProductsGrid {
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
	 * @return a {@link ru.extas.model.sale.Product} object.
	 */
	public Product getSelected() {
		return selected;
	}
}
