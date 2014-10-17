/**
 *
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.PopupDateField;
import org.joda.time.LocalDate;

/**
 * Преднастроенный компонент ввода даты
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LocalDateField extends PopupDateField {

    private static final long serialVersionUID = 3615480008225092751L;

    public LocalDateField(final String caption) {
        this(caption, caption);
    }

    /**
     * Создает поле ввода даты с заголовком и описанием
     *
     * @param caption     заголовок
     * @param description описание
     */
    public LocalDateField(final String caption, final String description) {
        super(caption);
        setDescription(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));

        init();
    }

    public void init() {
        setImmediate(true);
        setWidth(10, Unit.EM);
        setInputPrompt("ДД.ММ.ГГГГ");
        setDateFormat("dd.MM.yyyy");
        setConverter(LocalDate.class);
        setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
    }

    public LocalDateField() {
        init();
    }
}
