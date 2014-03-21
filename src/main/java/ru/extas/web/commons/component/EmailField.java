package ru.extas.web.commons.component;

import com.vaadin.data.validator.EmailValidator;

/**
 * Поле ввода адреса электронной почты
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 13:15
 * @version $Id: $Id
 * @since 0.3
 */
public class EmailField extends EditField {
    /**
     * <p>Constructor for EmailField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public EmailField(String caption, String description) {
        super(caption, description);
        initField();
    }

    /**
     * <p>Constructor for EmailField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public EmailField(String caption) {
        super(caption);
        initField();
    }

    private void initField() {
        setColumns(20);
        setDescription("Введите имя e-mail контакта который будет использоваться для связи");
        setInputPrompt("e-mail");
        addValidator(new EmailValidator("{0} не является допустимым адресом электронной почты."));
    }
}
