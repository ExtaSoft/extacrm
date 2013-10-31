/**
 *
 */
package ru.extas.server;

import com.vaadin.server.VaadinServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * Поставщик служб приложения
 *
 * @author Valery Orlov
 */
public final class ServiceLocator {

    public static final String EXTACRM_JPA_UNIT = "extacrmJpaUnit";

    /**
     * Ищет подходящий экземпляр для интерфейса службы
     *
     * @param srvType Тип службы
     * @return экземпляр службы
     */
    public static <TServiceType> TServiceType lookup(Class<TServiceType> srvType) {

        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        return context.getBean(srvType);
    }
}
