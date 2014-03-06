/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.UserProfile;
import ru.extas.security.UserRealm;
import ru.extas.server.UserRegistry;
import ru.extas.web.commons.window.AbstractEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма смены пароля пользователя.
 *
 * @author Valery Orlov
 */
public class ChangePasswordForm extends AbstractEditForm<UserProfile> {

private static final long serialVersionUID = -6196099478095832935L;
private final static Logger logger = LoggerFactory.getLogger(ChangePasswordForm.class);

@PropertyId("password")
private PasswordField passField;

private PasswordField passConfField;

/**
 * @param obj
 */
public ChangePasswordForm(final BeanItem<UserProfile> obj) {
	super("Смена пароля", obj);
}

/*
 * (non-Javadoc)
 *
 * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
 * AbstractExtaObject)
 */
@Override
protected void initObject(final UserProfile obj) {
}

/*
 * (non-Javadoc)
 *
 * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
 * AbstractExtaObject)
 */
@Override
protected void saveObject(final UserProfile obj) {
	logger.debug("Saving changed password...");

	// Шифруем пароль
	UserRealm.securePassword(obj);
	// Сбрасываем флаг смены пароля.
	obj.setChangePassword(false);

	final UserRegistry userService = lookup(UserRegistry.class);
	userService.save(obj);
}

/*
 * (non-Javadoc)
 *
 * @see
 * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
 * AbstractExtaObject)
 */
@Override
protected void checkBeforeSave(final UserProfile obj) {
}

/*
 * (non-Javadoc)
 *
 * @see
 * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
 * .AbstractExtaObject)
 */
@Override
protected ComponentContainer createEditFields(final UserProfile obj) {
	final FormLayout form = new FormLayout();

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
	passConfField.setValue(obj.getPassword());
	form.addComponent(passConfField);

	return form;
}

}
