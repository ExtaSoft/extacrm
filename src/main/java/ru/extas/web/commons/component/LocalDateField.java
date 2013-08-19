/**
 *
 */
package ru.extas.web.commons.component;

import com.google.appengine.repackaged.org.joda.time.LocalDate;
import com.vaadin.ui.PopupDateField;

/**
 * Преднастроенный компонент ввода даты
 *
 * @author Valery Orlov
 */
public class LocalDateField extends PopupDateField {

    private static final long serialVersionUID = 3615480008225092751L;

    /**
     * Создает поле ввода даты с заголовком и описанием
     *
     * @param caption     заголовок
     * @param description описание
     */
    public LocalDateField(String caption, String description) {
        super(caption);

        setImmediate(true);
        setWidth(10, Unit.EM);
        setDescription(description);
        setInputPrompt("ДД.ММ.ГГГГ");
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setDateFormat("dd.MM.yyyy");
        setConverter(LocalDate.class);
        setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
    }

}
