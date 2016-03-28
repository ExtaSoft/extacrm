package ru.extas.web.contacts.person;

import com.vaadin.data.Container;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.window.CloseOnlyWindow;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Окно с таблицей для выбора физ. лица
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 16:26
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonSelectWindow extends CloseOnlyWindow {

	private Set<Person> selected;
	private boolean selectPressed;

	/**
	 * <p>Constructor for PersonSelectWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public PersonSelectWindow(final String caption) {
		super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

		addAttachListener(e -> setContent(new SelectGrid()));
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
            final ExtaDbContainer<Person> container = new ExtaDbContainer<>(Person.class);
            container.addNestedContainerProperty("registerAddress.regionWithType");
            container.addNestedContainerProperty("registerAddress.city");
            return container;
        }

        @Override
		protected List<UIAction> createActions() {
			final List<UIAction> actions = newArrayList();

			actions.add(new DefaultAction("Выбрать", "Выбрать выделенный в списке контакт и закрыть окно", Fontello.CHECK) {
				@Override
				public void fire(final Set itemIds) {

					selected = getEntities(itemIds);
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
	public Set<Person> getSelected() {
		return selected;
	}
}
