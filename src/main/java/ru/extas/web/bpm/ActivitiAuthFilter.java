package ru.extas.web.bpm;

import org.activiti.engine.impl.identity.Authentication;
import ru.extas.server.UserManagementService;

import javax.inject.Inject;
import javax.servlet.*;
import java.io.IOException;

/**
 * Устанавливает глобальное окружение Activiti для запроса
 *
 * @author Valery Orlov
 *         Date: 19.11.13
 *         Time: 12:34
 * @version $Id: $Id
 * @since 0.3
 */
public class ActivitiAuthFilter implements Filter {

    @Inject
    private UserManagementService userManagementService;

    /** {@inheritDoc} */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (userManagementService.isUserAuthenticated())
            Authentication.setAuthenticatedUserId(userManagementService.getCurrentUserLogin());
        else
            Authentication.setAuthenticatedUserId(null);

        chain.doFilter(request, response);
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
    }
}
