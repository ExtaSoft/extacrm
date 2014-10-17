package ru.extas.web.contacts;

import com.vaadin.data.Container;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.Fontello;
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
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonSelectWindow extends CloseOnlylWindow {

	private Person selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for PersonSelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public PersonSelectWindow(final String caption) {
		super(caption);
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

	private class SelectGrid extends PersonsGrid {
        @Override
        protected Container createContainer() {
            final ExtaDataContainer<Person> container = new ExtaDataContainer<>(Person.class);
            container.addNestedContainerProperty("regAddress.region");
            return container;
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
	 * @return a {@link ru.extas.model.contacts.Person} object.
	 */
	public Person getSelected() {
		return selected;
	}
}
