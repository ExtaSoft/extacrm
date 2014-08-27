/**
 *
 */
package ru.extas.web.commons.component;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.PopupDateField;
import org.joda.time.DateTime;

/**
 * Преднастроенный компонент ввода даты
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class DateTimeField extends PopupDateField {

    private static final long serialVersionUID = 3615480008225092751L;

    /**
     * Создает поле ввода даты с заголовком и описанием
     *
     * @param caption     заголовок
     * @param description описание
     */
    public DateTimeField(final String caption, final String description) {
        super(caption);

        setImmediate(true);
        setWidth(10, Unit.EM);
        setDescription(description);
        setInputPrompt("ДД.ММ.ГГГГ ЧЧ:ММ:СС");
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setDateFormat("dd.MM.yyyy HH:mm:ss");
        setResolution(Resolution.SECOND);
        setConverter(DateTime.class);
        setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ ЧЧ:ММ:СС");
    }

}
