package ru.extas.web.commons.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import ru.extas.web.commons.ExtaTheme;

/**
 * Заголовок, разделяющий группу полей на фонме ввода/редактирования.
 * По сути стилизованый лейбл.
 *
 * @author Valery Orlov
 *         Date: 30.08.2014
 *         Time: 18:04
 */
public class FormGroupHeader extends Label {
    public FormGroupHeader() {
        initialize();
    }

    public FormGroupHeader(final String content) {
        super(content);
        initialize();
    }

    public FormGroupHeader(final String content, final ContentMode contentMode) {
        super(content, contentMode);
        initialize();
    }

    private void initialize() {
        addStyleName(ExtaTheme.LABEL_H3);
        addStyleName(ExtaTheme.LABEL_COLORED);
    }

}
