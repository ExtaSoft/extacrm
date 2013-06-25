package ru.extas.guice;

import org.apache.shiro.guice.web.ShiroWebModule;

import ru.extas.web.ExtaCrmUI;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.vaadin.ui.UI;

/**
 * Web модуль инъекций
 * 
 * @author Valery Orlov
 * 
 */
public class ExtasWebGuiceModule extends ServletModule {

	@Override
	protected void configureServlets() {
		// Фильтр системы безопасности
		ShiroWebModule.bindGuiceFilter(binder());

		// Сервлет пользовательского интерфейса
		// bind(GAEVaadinServlet.class).in(Singleton.class);
		// Map<String, String> param = new HashMap<String, String>();
		// param.put("UI", "ru.extas.web.ExtaCrmUI");
		// serve("/*").with(GAEVaadinServlet.class, param);
		serve("/*").with(ExtasGuiceServlet.class);
	}

	@Provides
	private Class<? extends UI> provideUIClass() {
		return ExtaCrmUI.class;
	}

}