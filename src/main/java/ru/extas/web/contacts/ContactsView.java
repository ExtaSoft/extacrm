/**
 * 
 */
package ru.extas.web.contacts;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Contact;
import ru.extas.server.ContactService;
import ru.extas.web.commons.ExtaAbstractView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Реализует экран контактов
 * 
 * @author Valery Orlov
 * 
 */
public class ContactsView extends ExtaAbstractView {

	private static final long serialVersionUID = -1272779672761523416L;
	private final Logger logger = LoggerFactory.getLogger(ContactsView.class);
	private Table table;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.ExtaAbstractView#getContent()
	 */
	@Override
	protected Component getContent() {
		logger.info("Creating view content...");
		// Запрос данных
		ContactService contactService = lookup(ContactService.class);
		final Collection<Contact> contacts = contactService.loadContacts();
		final BeanItemContainer<Contact> beans = new BeanItemContainer<Contact>(Contact.class);
		beans.addAll(contacts);

		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Формируем тулбар
		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newBtn = new Button("Новый");
		newBtn.addStyleName("icon-user-add-1");
		newBtn.setDescription("Ввод нового Контакта в систему");
		newBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Contact newObj = new Contact();

				final ContactEditForm editWin = new ContactEditForm("Ввод нового контакта в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							beans.addBean(newObj);
							table.setValue(newObj);
							Notification.show("Пользователь сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		commandBar.addComponent(newBtn);

		Button editBtn = new Button("Изменить");
		editBtn.addStyleName("icon-user-2");
		editBtn.setDescription("Редактирование контактных данных");
		editBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// FIXME: Проверить, что имеется выбранная запись
				final Contact curObj = (Contact) table.getValue();

				final ContactEditForm editWin = new ContactEditForm("Редактирование контактных данных", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							// FIXME: Избавиться от дорогой операции обновления
							table.refreshRowCache();
							Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		commandBar.addComponent(editBtn);

		panel.addComponent(commandBar);

		// Создаем таблицу скроллинга
		table = new Table("Контакты", beans);
		table.setSizeFull();

		ContactDataDecl ds = new ContactDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.ExtaAbstractView#getTitle()
	 */
	@Override
	protected Component getTitle() {
		final Component title = new Label("Контакты");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

}
