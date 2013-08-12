/**
 * 
 */
package ru.extas.web.users;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.UserProfile;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.ExtaAbstractView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
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
		final LazyJdoContainer<UserProfile> container = new LazyJdoContainer<UserProfile>(UserProfile.class, 50, null);

		final CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Формируем тулбар
		final HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		final Button newBtn = new Button("Новый");
		newBtn.addStyleName("icon-user-add");
		newBtn.setDescription("Ввод нового пользователя в систему");
		newBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(final ClickEvent event) {
				final Object newObjId = table.addItem();
				final BeanItem<UserProfile> newObj = (BeanItem<UserProfile>)table.getItem(newObjId);

				final UserEditForm editWin = new UserEditForm("Ввод нового пользователя в систему", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							table.select(newObjId);
							Notification.show("Пользователь сохранен", Type.TRAY_NOTIFICATION);
						} else {
							table.removeItem(newObjId);
						}
					}
				});
				editWin.showModal();
			}
		});
		commandBar.addComponent(newBtn);

		final Button editBtn = new Button("Изменить");
		editBtn.addStyleName("icon-user-1");
		editBtn.setDescription("Редактирование данных пользователя");
		editBtn.setEnabled(false);
		editBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(final ClickEvent event) {
				final Object curObjId = checkNotNull(table.getValue(), "No selected row");
				final BeanItem<UserProfile> curObj = (BeanItem<UserProfile>)table.getItem(curObjId);

				final UserEditForm editWin = new UserEditForm("Редактирование данных пользователя", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
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
		table = new Table("Пользователи", container);
		table.setSizeFull();

		// Обеспечиваем корректную работу кнопок зависящих от выбранной записи
		table.setImmediate(true);
		table.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				final boolean enableBtb = event.getProperty().getValue() != null;
				editBtn.setEnabled(enableBtb);
			}
		});
// if (table.size() > 0)
// table.select(table.firstItemId());

		final UsersDataDecl ds = new UsersDataDecl();
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
