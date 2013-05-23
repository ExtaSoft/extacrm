package ru.extas.guice;

import ru.extas.web.ExtaCrmUI;

import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.vaadin.shared.Version;
import com.vaadin.ui.UI;

public class ExtasGuiceModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/*").with(ExtasGuiceServlet.class);

		bind(String.class).annotatedWith(Names.named("title")).toInstance("Basic Guice Vaadin Application");
		bind(String.class).annotatedWith(Names.named("version")).toInstance("<b>Vaadin " + Version.getFullVersion() + "</b>");
	}

	@Provides
	private Class<? extends UI> provideUIClass() {
		return ExtaCrmUI.class;
	}

}