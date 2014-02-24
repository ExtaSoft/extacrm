/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.UserProfile;
import ru.extas.model.UserRole;
import ru.extas.server.UserRegistry;
import ru.extas.shiro.UserRealm;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.util.ComponentUtil;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
public class UserEditForm extends AbstractEditForm<UserProfile> {

	private static final long serialVersionUID = -5016687382646391930L;
	private final static Logger logger = LoggerFactory.getLogger(UserEditForm.class);
	private final String initialPassword;
	// Компоненты редактирования
	@PropertyId("contact")
	private PersonSelect nameField;
	@PropertyId("login")
	private TextField loginField;
	@PropertyId("role")
	private ComboBox roleField;
	@PropertyId("blocked")
	private CheckBox blockedField;
	@PropertyId("changePassword")
	private CheckBox changePasswordField;
	@PropertyId("password")
	private PasswordField passField;
	private PasswordField passConfField;

	/**
	 * @param caption
	 * @param obj
	 */
	public UserEditForm(final String caption, final BeanItem<UserProfile> obj) {

		super(caption, obj);
		initialPassword = obj.getBean().getPassword();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(final UserProfile obj) {
		if (obj.getId() == null) {
			// Инициализируем новый объект
			obj.setRole(UserRole.USER);
			obj.setChangePassword(true);
		}
	}

	@Override
	protected void saveObject(final UserProfile obj) {
		logger.debug("Saving user profile...");
		securePassword(obj);
		final UserRegistry userService = lookup(UserRegistry.class);
		userService.save(obj);
		Notification.show("Пользователь сохранен", Notification.Type.TRAY_NOTIFICATION);
	}

	/**
	 * @param user
	 */
	private void securePassword(final UserProfile user) {
		// Проверить менялся ли пароль
		if (!passField.getValue().equals(initialPassword)) {
			UserRealm.securePassword(user);
		}
	}

	@Override
	protected void checkBeforeSave(final UserProfile obj) {
	}

	@Override
	protected ComponentContainer createEditFields(final UserProfile obj) {
		// Have some layout
		final FormLayout form = new FormLayout();

		// FIXME Ограничить выбор контакта только сотрудниками
		nameField = new PersonSelect("Имя");
		nameField.setImmediate(true);
		nameField.setWidth(50, Unit.EX);
		nameField.setDescription("Введите имя (ФИО) пользователя");
		nameField.setRequired(true);
		nameField.setRequiredError("Имя пользователя не может быть пустым. Пожалуйста введите ФИО пользователя.");
		form.addComponent(nameField);

		// FIXME Проверить уникальность логина
		loginField = new TextField("Логин (e-mail)");
		loginField.setReadOnly(obj.getId() != null);
		loginField.setImmediate(true);
		loginField.setWidth(40, Unit.EX);
		loginField.setDescription("Введите имя e-mail пользователя который будет использоваться для входа в систему");
		loginField.setInputPrompt("e-mail");
		loginField.setRequired(true);
		loginField
				.setRequiredError("Логин пользователя не может быть пустым. Пожалуйста введите действительный e-mail пользователя.");
		loginField.setNullRepresentation("");
		loginField.addValidator(new EmailValidator("{0} не является допустимым адресом электронной почты."));
		form.addComponent(loginField);

		passField = new PasswordField("Пароль");
		passField.setImmediate(true);
		passField.setDescription("Введите пароль для входа в систему");
		passField.setInputPrompt("Пароль");
		passField.setRequired(true);
		passField.setRequiredError("Пароль пользователя не может быть пустым.");
		passField.setNullRepresentation("");
		form.addComponent(passField);

		passConfField = new PasswordField("Подтверждение пароля");
		passConfField.setImmediate(true);
		passConfField.setDescription("Введите повторно пароль для для его подтвержедения");
		passConfField.setInputPrompt("Подтверждение пароля");
		passConfField.setRequired(true);
		passConfField.setNullRepresentation("");
		// TODO: Сделать симметричную проверку пароля
		passConfField.addValidator(new Validator() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(final Object value) throws InvalidValueException {
				if (!value.equals(passField.getValue()))
					throw new InvalidValueException("Пароли не совпадают!");

			}
		});
		passConfField.setValue(initialPassword);
		form.addComponent(passConfField);

		roleField = new ComboBox("Роль");
		roleField
				.setDescription("Роль пользователя в системе. Определяет основные права доступа к разделам и объектам системы.");
		roleField.setRequired(true);
		roleField.setNullSelectionAllowed(false);
		roleField.setNewItemsAllowed(false);
		ComponentUtil.fillSelectByEnum(roleField, UserRole.class);
		form.addComponent(roleField);

		blockedField = new CheckBox("Блокировать");
		blockedField.setDescription("Установите, чтобы блокировать вход пользователя в систему.");
		form.addComponent(blockedField);

		changePasswordField = new CheckBox("Сменить пароль");
		changePasswordField
				.setDescription("Установите, чтобы потребовать у пользователя смены пароля при следующем входе с систему.");
		form.addComponent(changePasswordField);

		return form;
	}
}
