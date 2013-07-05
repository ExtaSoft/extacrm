/**
 * 
 */
package ru.extas.web.users;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;
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
 * Реализует экран управления пользователями и правами доступа
 * 
 * @author Valery Orlov
 * 
 */
public class UsersView extends ExtaAbstractView {

	private static final long serialVersionUID = -1272779672761523416L;
	private final Logger logger = LoggerFactory.getLogger(UsersView.class);
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
		UserManagementService userService = lookup(UserManagementService.class);
		final Collection<UserProfile> users = userService.loadUsers();
		final BeanItemContainer<UserProfile> beans = new BeanItemContainer<UserProfile>(UserProfile.class);
		beans.addAll(users);

		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Формируем тулбар
		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newBtn = new Button("Новый");
		newBtn.addStyleName("icon-user-add");
		newBtn.setDescription("Ввод нового пользователя в систему");
		newBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final UserProfile newObj = new UserProfile();

				final UserEditForm editWin = new UserEditForm("Ввод нового пользователя в систему", newObj);
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
		editBtn.addStyleName("icon-user-1");
		editBtn.setDescription("Редактирование данных пользователя");
		editBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// FIXME: Проверить, что имеется выбранная запись
				final UserProfile curObj = (UserProfile) table.getValue();

				final UserEditForm editWin = new UserEditForm("Редактирование данных пользователя", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							Notification.show("Пользователь сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		commandBar.addComponent(editBtn);

		panel.addComponent(commandBar);

		// Создаем таблицу скроллинга
		table = new Table("Пользователи", beans);

		table.setSizeFull();

		UsersDataDecl ds = new UsersDataDecl();
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
		final Component title = new Label("Пользователи");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

}
