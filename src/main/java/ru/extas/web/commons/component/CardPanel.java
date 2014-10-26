package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ru.extas.web.commons.ExtaTheme;


/**
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 15:13
 */
public class CardPanel extends CssLayout {

    private final Label label;
    private final HorizontalLayout panelCaption;

    public CardPanel(final String caption, final Component action, final Component panelContent) {
        addStyleName(ExtaTheme.LAYOUT_CARD);
        panelCaption = new HorizontalLayout();
        panelCaption.addStyleName("v-panel-caption");
        panelCaption.setWidth(100, Unit.PERCENTAGE);
        label = new Label(caption);
        panelCaption.addComponent(label);
        panelCaption.setExpandRatio(label, 1);
        panelCaption.addComponent(action);

        addComponent(panelCaption);
        addComponent(panelContent);
    }
}
