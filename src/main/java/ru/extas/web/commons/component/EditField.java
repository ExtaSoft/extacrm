/**
 *
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.TextField;

/**
 * Преднастроенный компонент ввода текста
 *
 * @author Valery Orlov
 */
public class EditField extends TextField {

    private static final long serialVersionUID = -7195209978050069287L;

    /**
     * Создает поле ввода с заголовком и описанием
     *
     * @param caption     заголовок
     * @param description описание
     */
    public EditField(String caption, String description) {
        super(caption);

        setImmediate(true);
        setDescription(description);
        setInputPrompt(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setNullRepresentation("");
    }

    /**
     * Constructs an empty <code>TextField</code> with given caption.
     *
     * @param caption the caption <code>String</code> for the editor.
     */
    public EditField(final String caption) {
        this(caption, caption);
    }
}
