/**
 * 
 */
package ru.extas.web.contacts;

import ru.extas.model.Contact;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;

import com.vaadin.data.util.BeanItem;
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
	public ContactSelect(final String caption) {
		this(caption, "Выберете существующего клиента или введите нового");
	}

	/**
	 * @param caption
	 * @param description
	 */
	public ContactSelect(final String caption, final String description) {
		super(caption);

		// Преконфигурация
		setDescription(description);
		setInputPrompt("Существующий или новый клиент");
		setWidth(25, Unit.EM);
		setImmediate(true);

		// Инициализация контейнера
		final LazyJdoContainer<Contact> container = new LazyJdoContainer<Contact>(Contact.class, 20, "this");
		// setConverter(new SingleSelectConverter<Contact>(this, container));

		// Устанавливаем контент выбора
		setFilteringMode(FilteringMode.STARTSWITH);
		setContainerDataSource(container);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("name");

		// Функционал добавления нового контакта
		setNullSelectionAllowed(false);
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked" })
			@Override
			public void addNewItem(final String newItemCaption) {
				final Object newObjId = container.addItem();
				final BeanItem<Contact> newObj = (BeanItem<Contact>)container.getItem(newObjId);
				newObj.getBean().setName(newItemCaption);

				final ContactEditForm editWin = new ContactEditForm("Ввод нового контакта в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							ContactSelect.this.setValue(newObj.getBean());
							Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
						} else {
							container.removeItem(newObjId);
						}

					}
				});
				editWin.showModal();
			}
		});
	}

}
