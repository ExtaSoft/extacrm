package ru.extas.web;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import ru.extas.web.lead.embedded.LeadInputFormUI;

/**
 * Провайдер пользовательского интерфейса
 *
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 20:20
 */
public class MyUIProvider extends UIProvider {

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {

        VaadinRequest request = event.getRequest();
        if (request.getPathInfo().startsWith("/ui/elifui"))
            return LeadInputFormUI.class;
        else
            return ExtaCrmUI.class;
    }

}
