package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import ru.extas.web.commons.ExtaTheme;

/**
 * @author Valery Orlov
 *         Date: 08.08.2014
 *         Time: 16:35
 */
public class ExtaFormLayout extends FormLayout {

    public ExtaFormLayout() {
        init();
    }

    protected void init() {
        addStyleName(ExtaTheme.FORMLAYOUT_LIGHT);
        setSizeUndefined();
    }

    public ExtaFormLayout(final Component... children) {
        super(children);
    }
}
