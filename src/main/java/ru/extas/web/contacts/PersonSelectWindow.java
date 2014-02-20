package ru.extas.web.contacts;

import ru.extas.model.Person;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно с таблицей для выбора физ. лица
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 16:26
 */
public class PersonSelectWindow extends CloseOnlylWindow {

	private Person selected;
	private boolean selectPressed;

	public PersonSelectWindow(final String caption) {
		super(caption);
		addStyleName("base-view");
		setContent(new SelectGrid());
	}

	public boolean isSelectPressed() {
		return selectPressed;
	}

	private class SelectGrid extends PersonsGrid {
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

	public Person getSelected() {
		return selected;
	}
}
