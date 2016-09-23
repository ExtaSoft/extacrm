package ru.extas.web.commons.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import ru.extas.web.commons.ExtaTheme;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Заголовок, разделяющий группу полей на фонме ввода/редактирования.
 * По сути стилизованый лейбл.
 *
 * @author Valery Orlov
 *         Date: 30.08.2014
 *         Time: 18:04
 */
public class FormGroupHeaderExpand extends Button {

    private final List<Component> controlled = newArrayList();
    private boolean expanded;

    public FormGroupHeaderExpand(String caption, boolean expanded) {
        super(caption);
        this.expanded = expanded;
    }

    public FormGroupHeaderExpand(final String content) {
        this(content, false);
        initialize();
    }


    private void initialize() {
        addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addStyleName(ExtaTheme.LABEL_H3);
        addStyleName(ExtaTheme.LABEL_COLORED);

        addClickListener(e -> {
            expanded = !expanded;
            updateState();
        });

        addAttachListener(e -> updateState());
    }

    public FormGroupHeaderExpand addControlled(Component component) {
        if (component != null)
            controlled.add(component);
        return this;
    }

    public void expand() {
        expanded = true;
        updateState();
    }

    protected void updateState() {
        setIcon(expanded ? FontAwesome.MINUS_SQUARE_O : FontAwesome.PLUS_SQUARE_O);
        controlled.forEach(i -> i.setVisible(expanded));
    }

    public void collapse() {
        expanded = false;
        updateState();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


}
