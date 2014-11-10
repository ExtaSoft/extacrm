package ru.extas.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import ru.extas.web.commons.ExtaTheme;

/**
 * @author Valery Orlov
 *         Date: 18.07.2014
 *         Time: 21:03
 */
public class RootLayout extends HorizontalLayout {

    final CssLayout contentArea = new CssLayout();

    final CssLayout menuArea = new CssLayout();

    public RootLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName(ExtaTheme.MENU_ROOT);

        contentArea.setPrimaryStyleName(ExtaTheme.MENU_CONTENT);
//        contentArea.addStyleName(ExtaTheme.V_SCROLLABLE);
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }

    public void addMenu(final Component menu) {
        menu.addStyleName(ExtaTheme.MENU_PART);
        menuArea.addComponent(menu);
    }

    public void addBranding(final Component branding) {
        branding.setPrimaryStyleName(ExtaTheme.MENU_TITLE);
        menuArea.addComponent(branding);
    }

    public void addUserBadge(final Component badge) {
        badge.setPrimaryStyleName(ExtaTheme.VALO_MENU_BOTTOM);
        menuArea.addComponent(badge);
    }
}
