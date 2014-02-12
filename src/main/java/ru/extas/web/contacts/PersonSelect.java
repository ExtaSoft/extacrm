package ru.extas.web.contacts;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.Person;

/**
 * Выбор контакта - физ. лица
 * с возможностью добавления нового
 * <p/>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 */
public class PersonSelect extends AbstractContactSelect<Person> {

	private Person defNewObj;

	public PersonSelect(final String caption) {
		this(caption, new Person());
	}

	public PersonSelect(final String caption, Person defNewObj) {
		super(caption, Person.class);
		this.defNewObj = defNewObj;
		addNewItemFeature();
	}

	public PersonSelect(final String caption, final String description) {
		this(caption, description, new Person());
		addNewItemFeature();
	}

	public PersonSelect(final String caption, final String description, Person defNewObj) {
		super(caption, description, Person.class);
		this.defNewObj = defNewObj;
		addNewItemFeature();
	}

	private void addNewItemFeature() {
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked"})
			@Override
			public void addNewItem(final String newItemCaption) {
				final BeanItem<Person> newObj;
				newObj = new BeanItem<>(defNewObj.clone());
				if (defNewObj.getName() == null)
					newObj.getBean().setName(newItemCaption);
				newObj.expandProperty("actualAddress");

				final String edFormCaption = "Ввод нового контакта в систему";
				final PersonEditForm editWin = new PersonEditForm(edFormCaption, newObj);
				editWin.setModified(true);

				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							container.refresh();
							setValue(newObj.getBean().getId());
						}
					}
				});
				editWin.showModal();
			}
		});
	}
}
