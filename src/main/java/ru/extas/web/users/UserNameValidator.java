package ru.extas.web.users;

import com.vaadin.data.validator.RegexpValidator;

/**
 * Проверяет допустимость имени пользователя (email или простое)
 *
 * @author Valery Orlov
 *         Date: 13.11.2014
 *         Time: 13:20
 */
public class UserNameValidator extends RegexpValidator {
    public UserNameValidator() {
        super("^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$|^[a-z0-9_\\.\\--]{3,50}$",
                true,
                "{0} не является допустимым именем пользователя. " +
                        "В качестве имени пользователя допускается адрес электронной почты " +
                        "или имя состоящее из букв латинского алфавита, цифр, тире, подчеркивания и точки.");
    }
}
