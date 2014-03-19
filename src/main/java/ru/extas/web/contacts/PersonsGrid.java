/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.Person;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class PersonsGrid extends ExtaGrid {

	/**
	 * <p>Constructor for PersonsGrid.</p>
	 */
	public PersonsGrid() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new PersonDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		final ExtaDataContainer<Person> container = new ExtaDataContainer<>(Person.class);
		container.addNestedContainerProperty("actualAddress.region");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового Контакта в систему", "icon-user-add-1") {
			@Override
			public void fire(Object itemId) {
				final BeanItem<Person> newObj = new BeanItem<>(new Person());
				newObj.expandProperty("actualAddress");

				final PersonEditForm editWin = new PersonEditForm("Ввод нового контакта в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", "icon-user-2") {
			@Override
			public void fire(final Object itemId) {

				final BeanItem<Person> curObj = new GridItem<>(table.getItem(itemId));
				curObj.expandProperty("actualAddress");

				final String caption = String.format("Редактирование контакта: %s", curObj.getBean().getName());
				final PersonEditForm editWin = new PersonEditForm(caption, curObj);
				editWin.addCloseListener(new CloseListener() {

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainerItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});

		return actions;
	}
}
