package ru.extas.guice;

import com.google.inject.Inject;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/**
 * Провайдер пользовательского интерфейса
 *
 * @author Valery Orlov
 */
public class ExtasGuiceProvider extends UIProvider {

    private static final long serialVersionUID = -673158892184775100L;

    @Inject
    private Class<? extends UI> uiClass;

    @Override
    public UI createInstance(UICreateEvent event) {
        return ExtasGuiceFilter.getInjector().getProvider(uiClass).get();
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return uiClass;
    }

}