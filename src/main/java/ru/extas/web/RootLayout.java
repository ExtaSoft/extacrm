package ru.extas.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author Valery Orlov
 *         Date: 18.07.2014
 *         Time: 21:03
 */
public class RootLayout extends HorizontalLayout {

    CssLayout contentArea = new CssLayout();

    CssLayout menuArea = new CssLayout();

    public RootLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName("valo-menu");

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }

    public void addMenu(final Component menu) {
        menu.addStyleName("valo-menu-part");
        menuArea.addComponent(menu);
    }

    public void addBranding(final Component branding) {
        branding.setPrimaryStyleName("valo-menu-title");
        menuArea.addComponent(branding);
    }

    public void addUserBadge(final Component badge) {
        badge.setPrimaryStyleName("valo-menu-bottom");
        menuArea.addComponent(badge);
    }
}
