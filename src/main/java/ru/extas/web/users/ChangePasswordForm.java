/**
 * 
 */
package ru.extas.web.users;

import static ru.extas.server.ServiceLocator.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;
import ru.extas.shiro.UserRealm;
import ru.extas.web.commons.AbstractEditForm;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;

/**
 * Форма смены пароля пользователя.
 * 
 * @author Valery Orlov
 * 
 */
public class ChangePasswordForm extends AbstractEditForm<UserProfile> {

	private static final long serialVersionUID = -6196099478095832935L;
	private final Logger logger = LoggerFactory.getLogger(ChangePasswordForm.class);

	@PropertyId("password")
	private PasswordField passField;

	private PasswordField passConfField;

	/**
	 * @param caption
	 * @param obj
	 */
	public ChangePasswordForm(UserProfile obj) {
		super("Смена пароля", obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(UserProfile obj) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#saveObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void saveObject(UserProfile obj) {
		logger.debug("Saving changed password...");

		// Шифруем пароль
		UserRealm.securePassword(obj);
		// Сбрасываем флаг смены пароля.
		obj.setChangePassword(false);

		UserManagementService userService = lookup(UserManagementService.class);
		userService.persistUser(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void checkBeforeSave(UserProfile obj) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#createEditFields(ru.extas.model
	 * .AbstractExtaObject)
	 */
	@Override
	protected FormLayout createEditFields(UserProfile obj) {
		FormLayout form = new FormLayout();

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
			public void validate(Object value) throws InvalidValueException {
				if (!value.equals(passField.getValue()))
					throw new InvalidValueException("Пароли не совпадают!");

			}
		});
		passConfField.setValue(obj.getPassword());
		form.addComponent(passConfField);

		return form;
	}

}