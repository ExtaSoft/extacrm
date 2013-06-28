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
import ru.extas.web.commons.GridDataSource;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

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

		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Формируем тулбар
		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		// commandBar.setSpacing(false);

		Button newPolyceBtn = new Button("Новый");
		newPolyceBtn.addStyleName("icon-user-add");
		newPolyceBtn.setDescription("Ввод нового пользователя в систему");
		commandBar.addComponent(newPolyceBtn);

		Button editPolyceBtn = new Button("Изменить");
		editPolyceBtn.addStyleName("icon-user-1");
		editPolyceBtn.setDescription("Редактировать данные пользователя");
		commandBar.addComponent(editPolyceBtn);

		panel.addComponent(commandBar);

		// Запрос данных
		UserManagementService userService = lookup(UserManagementService.class);
		final Collection<UserProfile> users = userService.loadUsers();
		final BeanItemContainer<UserProfile> beans = new BeanItemContainer<UserProfile>(UserProfile.class);
		beans.addAll(users);

		// Создаем таблицу скроллинга
		table = new Table("Пользователи", beans);

		table.setSizeFull();
		table.setPageLength(0);
		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		GridDataSource ds = new GridDataSource();
		ds.setTableColumnHeaders(table);
		ds.setTableVisibleColumns(table);
		ds.setTableCollapsedColumns(table);

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
