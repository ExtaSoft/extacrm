package ru.extas.web.commons.component;

import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле редактирования номера телефона
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 12:00
 * @version $Id: $Id
 * @since 0.3
 */
public class PhoneField extends EditField {
    /**
     * <p>Constructor for PhoneField.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public PhoneField(String caption, String description) {
        super(caption, description);
        initField();
    }

    private void initField() {
        setColumns(20);
        setDescription("Введите телефон в формате 8 XXX XXX-XX-XX");
        setInputPrompt("8 XXX XXX-XX-XX");
        setConverter(lookup(PhoneConverter.class));
    }

    /**
     * <p>Constructor for PhoneField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public PhoneField(String caption) {
        super(caption);
        initField();
    }


}
