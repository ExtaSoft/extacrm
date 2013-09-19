package ru.extas.guice;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import org.apache.shiro.guice.web.ShiroWebModule;
import ru.extas.server.ServiceLocator;

/**
 * Web модуль инъекций
 *
 * @author Valery Orlov
 */
class ExtasWebGuiceModule extends ServletModule {

    @Override
    protected void configureServlets() {

        // Guice JPA persistence
        install(new JpaPersistModule(ServiceLocator.EXTACRM_JPA_UNIT));
        filter("/*").through(PersistFilter.class);

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