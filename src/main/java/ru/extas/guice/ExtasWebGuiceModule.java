package ru.extas.guice;

import org.apache.shiro.guice.web.ShiroWebModule;

import com.google.inject.servlet.ServletModule;

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

}