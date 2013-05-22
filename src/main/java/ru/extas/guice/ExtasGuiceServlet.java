package ru.extas.guice;

import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.GAEVaadinServlet;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@Singleton
public class ExtasGuiceServlet extends GAEVaadinServlet implements SessionInitListener {

	@Inject
	private ExtasGuiceProvider basicProvider;

	@Override
	protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters) {
		initParameters.setProperty(SERVLET_PARAMETER_PRODUCTION_MODE, "false");
		return super.createDeploymentConfiguration(initParameters);
	}

	@Override
	protected void servletInitialized() {
		getService().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		event.getSession().addUIProvider(basicProvider);
	}

}