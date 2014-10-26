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
 * @version $Id: $Id
 * @since 0.4.2
 */
public class MyUIProvider extends UIProvider {

    /** {@inheritDoc} */
    @Override
    public Class<? extends UI> getUIClass(final UIClassSelectionEvent event) {

        final VaadinRequest request = event.getRequest();
        if (request.getPathInfo().startsWith("/ui/elifui"))
            return LeadInputFormUI.class;
        else
            return ExtaCrmUI.class;
    }

}
