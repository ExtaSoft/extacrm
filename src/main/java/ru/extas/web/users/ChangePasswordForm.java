/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.security.UserProfile;
import ru.extas.security.UserRealm;
import ru.extas.server.security.UserRegistry;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.component.ExtaFormLayout;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма смены пароля пользователя.
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ChangePasswordForm extends ExtaEditForm<UserProfile> {

    private static final long serialVersionUID = -6196099478095832935L;
    private final static Logger logger = LoggerFactory.getLogger(ChangePasswordForm.class);

    @PropertyId("password")
    private PasswordField passField;

    private PasswordField passConfField;

    /**
     * <p>Constructor for ChangePasswordForm.</p>
     *
     * @param userProfile a {@link com.vaadin.data.util.BeanItem} object.
     */
    public ChangePasswordForm(final UserProfile userProfile) {
        super("Смена пароля", userProfile);
    }

    /** {@inheritDoc} */
    @Override
    protected void initEntity(final UserProfile userProfile) {
    }

    /** {@inheritDoc} */
    @Override
    protected UserProfile saveEntity(UserProfile userProfile) {
        logger.debug("Saving changed password...");

        // Шифруем пароль
        UserRealm.securePassword(userProfile);
        // Сбрасываем флаг смены пароля.
        userProfile.setChangePassword(false);

        final UserRegistry userService = lookup(UserRegistry.class);
        userProfile = userService.save(userProfile);
        return userProfile;
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout form = new ExtaFormLayout();

        passField = new PasswordField("Пароль");
//        passField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        passField.setIcon(Fontello.LOCK);
        passField.setImmediate(true);
        passField.setDescription("Введите пароль для входа в систему");
        passField.setInputPrompt("Пароль");
        passField.setRequired(true);
        passField.setRequiredError("Пароль пользователя не может быть пустым.");
        passField.setNullRepresentation("");
        form.addComponent(passField);

        passConfField = new PasswordField("Подтверждение пароля");
//        passConfField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        passConfField.setIcon(Fontello.LOCK);
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
        passConfField.setValue(getEntity().getPassword());
        form.addComponent(passConfField);

        return form;
    }

}
