package ru.extas.web.commons.component;

/**
 * Поле редактирования номера телефона
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 12:00
 */
public class PhoneField extends EditField {
    public PhoneField(String caption, String description) {
        super(caption, description);
        initField();
    }

    private void initField() {
        setColumns(20);
        setDescription("Введите телефон в формате +7 XXX XXX XXXX");
        setInputPrompt("+7 XXX XXX XXXX");
        // TODO: Добавить проверку правильности ввода телефона

    }

    public PhoneField(String caption) {
        super(caption);
        initField();
    }
}
