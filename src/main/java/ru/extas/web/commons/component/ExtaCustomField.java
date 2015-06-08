package ru.extas.web.commons.component;

import com.vaadin.ui.CustomField;

/**
 * @author Valery Orlov
 *         Date: 02.06.2015
 *         Time: 14:52
 */
public abstract class ExtaCustomField<T> extends CustomField<T> {

    public ExtaCustomField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setRequiredError(String.format("Поле '%s' обязательно для заполнения!", caption));
    }
}
