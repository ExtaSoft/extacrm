package ru.extas.guice;

import org.apache.shiro.guice.web.ShiroWebModule;

import ru.extas.web.ExtaCrmUI;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.vaadin.ui.UI;

public class ExtasGuiceModule extends ServletModule {

	@Override
	protected void configureServlets() {
		ShiroWebModule.bindGuiceFilter(binder());
		serve("/*").with(ExtasGuiceServlet.class);
	}

	@Provides
	private Class<? extends UI> provideUIClass() {
		return ExtaCrmUI.class;
	}

}