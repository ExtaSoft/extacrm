package ru.extas.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.server.*;

import java.util.Properties;

/**
 * Сервлет пользовательского интерфейса
 *
 * @author Valery Orlov
 */
@Singleton
public class ExtasGuiceServlet extends VaadinServlet implements SessionInitListener {

    private static final long serialVersionUID = -2786123692482071945L;

    @Inject
    private ExtasUIProvider uiProvider;

    @Override
    protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters) {

        // /initParameters.setProperty(SERVLET_PARAMETER_PRODUCTION_MODE,
        // "false");
        return super.createDeploymentConfiguration(initParameters);
    }

    @Override
    protected void servletInitialized() {
        getService().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(uiProvider);
    }

}