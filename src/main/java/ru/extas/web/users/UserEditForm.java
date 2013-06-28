/**
 * 
 */
package ru.extas.web.users;

import static ru.extas.server.ServiceLocator.lookup;
import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Valery Orlov
 * 
 */
public class UserEditForm extends Window {

	private static final long serialVersionUID = -5016687382646391930L;

	private boolean okPressed = false;
	private final HorizontalLayout buttonsPanel = new HorizontalLayout();
	private final Button cancelBtn;
	private final Button okBtn;

	// Компоненты редактирования
	@PropertyId("name")
	private TextField nameField;
	@PropertyId("login")
	private TextField loginField;
	@PropertyId("role")
	private TextField roleField;
	@PropertyId("blocked")
	private TextField blockedField;
	@PropertyId("changePassword")
	private TextField changePasswordField;

	/**
	 * @param string
	 * @param newObj
	 */
	public UserEditForm(String caption, final UserProfile obj) {

		super(caption);

		FormLayout form = createEditFields();

		BeanItem<UserProfile> item = new BeanItem<UserProfile>(obj);

		// Now create a binder
		final FieldGroup binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);

		cancelBtn = new Button("Отмена", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				binder.discard();
				UI.getCurrent().removeWindow(UserEditForm.this);
			}
		});
		cancelBtn.setStyleName("icon-cancel");

		okBtn = new Button("OK", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				try {
					okPressed = true;
					binder.commit();
					UserManagementService userService = lookup(UserManagementService.class);
					userService.persistUser(obj);
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UI.getCurrent().removeWindow(UserEditForm.this);
			}
		});

		okBtn.setStyleName("icon-ok");
		this.buttonsPanel.addComponent(okBtn);
		this.buttonsPanel.setComponentAlignment(okBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.addComponent(cancelBtn);
		this.buttonsPanel.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.setSpacing(true);

		setContent(form);

	}

	private FormLayout createEditFields() {
		// Have some layout
		FormLayout form = new FormLayout();

		nameField = new TextField("Имя");
		nameField.setDescription("Введите имя (ФИО) пользователя");
		nameField.setInputPrompt("Имя (Отчество) Фамилия");
		nameField.setRequired(true);
		nameField.setRequiredError("Имя пользователя не может быть пустым. Пожалуйста введите ФИО пользователя.");
		form.addComponent(nameField);

		loginField = new TextField("Логин (e-mail)");
		loginField.setDescription("Введите имя e-mail пользователя который будет использоваться для входа в систему");
		loginField.setInputPrompt("E-Mail");
		loginField.setRequired(true);
		loginField.setRequiredError("Логин пользователя не может быть пустым. Пожалуйста введите действительный e-mail пользователя.");
		form.addComponent(loginField);

		roleField = new TextField("Роль");
		roleField.setDescription("Роль пользователя в системе. Определяет основные права доступа к разделам и объектам системы.");
		// roleField.setInputPrompt("E-Mail");
		roleField.setRequired(true);
		// roleField.setRequiredError("Логин пользователя не может быть пустым. Пожалуйста введите действительный e-mail пользователя.");
		form.addComponent(roleField);

		blockedField = new TextField("Блокировать");
		form.addComponent(blockedField);

		changePasswordField = new TextField("Сменить пароль");
		form.addComponent(loginField);

		return form;
	}

	@Override
	public void setContent(Component content) {
		if (content != null) {
			final VerticalLayout contentContainer = new VerticalLayout(content, this.buttonsPanel);
			contentContainer.setMargin(true);
			contentContainer.setSpacing(true);
			contentContainer.setComponentAlignment(this.buttonsPanel, Alignment.MIDDLE_RIGHT);
			content = contentContainer;
		}
		super.setContent(content);
	}

	public boolean isOkPressed() {
		return this.okPressed;
	}

	public void showModal() {
		setClosable(true);
		setModal(true);

		UI.getCurrent().addWindow(this);
	}

}
