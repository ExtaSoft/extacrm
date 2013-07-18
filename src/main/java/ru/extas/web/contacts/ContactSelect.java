/**
 * 
 */
package ru.extas.web.contacts;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import ru.extas.model.Contact;
import ru.extas.server.ContactService;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Компонент для выбора контакта с возможностью добавления нового
 * 
 * @author Valery Orlov
 * 
 */
public class ContactSelect extends ComboBox {

	private static final long serialVersionUID = -8005905898383483037L;

	/**
	 * @param caption
	 */
	public ContactSelect(String caption) {
		this(caption, "Выберете существующего клиента или введите нового");
	}

	/**
	 * @param caption
	 * @param description
	 */
	public ContactSelect(String caption, String description) {
		super(caption);

		// Преконфигурация
		setDescription(description);
		setInputPrompt("Существующий или новый клиент");
		setWidth(25, Unit.EM);
		setImmediate(true);

		// Инициализация контейнера
		ContactService contactService = lookup(ContactService.class);
		final Collection<Contact> contacts = contactService.loadContacts();
		final BeanItemContainer<Contact> clientsCont = new BeanItemContainer<Contact>(Contact.class);
		clientsCont.addAll(contacts);

		// Устанавливаем контент выбора
		setFilteringMode(FilteringMode.CONTAINS);
		setContainerDataSource(clientsCont);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("name");

		// Функционал добавления нового контакта
		setNullSelectionAllowed(false);
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void addNewItem(String newItemCaption) {
				final Contact newcontact = new Contact();
				newcontact.setName(newItemCaption);

				final ContactEditForm editWin = new ContactEditForm("Ввод нового контакта в систему", newcontact);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							clientsCont.addBean(newcontact);
							ContactSelect.this.setValue(newcontact);
							Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
	}

}
