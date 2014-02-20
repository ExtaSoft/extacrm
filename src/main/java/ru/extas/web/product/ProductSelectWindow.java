package ru.extas.web.product;

import ru.extas.model.Product;
import ru.extas.web.commons.DefaultAction;
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
 */
public class ProductSelectWindow extends CloseOnlylWindow {
	private Product selected;
	private boolean selectPressed;

	public ProductSelectWindow(final String caption) {
		super(caption);
		addStyleName("base-view");
		setContent(new SelectGrid());
	}

	public boolean isSelectPressed() {
		return selectPressed;
	}

	private class SelectGrid extends ProductsGrid {
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

	public Product getSelected() {
		return selected;
	}
}
