package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractStringValidator;
import ru.extas.model.security.UserProfile;
import ru.extas.server.security.UserManagementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 13.11.2014
 *         Time: 13:20
 */
public class UserNameUniqueValidator extends AbstractStringValidator {

    private final UserProfile curUser;

    public UserNameUniqueValidator(final UserProfile curUser) {
        super("Пользователь с именем {0} уже зарегестрирован в системе. Пожалуйста выберите другое имя.");
        this.curUser = curUser;
    }

    @Override
    protected boolean isValidValue(final String value) {
        final UserProfile user = lookup(UserManagementService.class).findUserByLogin(value);
        if(user != null)
            return user.equals(curUser);
        else
            return true;
    }

}
