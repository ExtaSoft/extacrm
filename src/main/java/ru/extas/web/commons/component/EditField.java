/**
 *
 */
package ru.extas.web.commons.component;

import org.vaadin.viritin.fields.MTextField;

/**
 * Преднастроенный компонент ввода текста
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class EditField extends MTextField {

    private static final long serialVersionUID = -7195209978050069287L;

    /**
     * Создает поле ввода с заголовком и описанием
     *
     * @param caption     заголовок
     * @param description описание
     */
    public EditField(final String caption, final String description) {
        super(caption);

        setImmediate(true);
        setDescription(description);
        setInputPrompt(description);
        setRequiredError(String.format("Поле '%s' необходимо заполнить", caption));
    }

    /**
     * Constructs an empty <code>TextField</code> with given caption.
     *
     * @param caption the caption <code>String</code> for the editor.
     */
    public EditField(final String caption) {
        this(caption, caption);
    }

    public EditField() {
        this(null, null);
    }
}
