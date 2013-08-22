package ru.extas.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.server.ExtasServicesModule;
import ru.extas.shiro.ExtasShiroWebModule;
import ru.extas.web.WebUIModule;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;

/**
 * Фильтр управления инъекциями
 *
 * @author Valery Orlov
 */
public class ExtasGuiceFilter extends GuiceFilter {
    private static final Pattern URI_ADMIN_PATTERN = compile("/_ah/.*");
    private static final Set<String> URI_NOADMIN_SET = new HashSet<>(asList("/_ah/warmup"));
    private static Injector INJECTOR;
    private final Logger logger = LoggerFactory.getLogger(ExtasGuiceFilter.class);

    public static Injector getInjector() {
        return INJECTOR;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (URI_ADMIN_PATTERN.matcher(uri).matches())
            if (!URI_NOADMIN_SET.contains(uri)) {
                chain.doFilter(request, response);
                return;
            }
        super.doFilter(request, response, chain);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (INJECTOR != null)
            throw new ServletException("Injector already created?!");

        INJECTOR = Guice.createInjector(
                new ExtasShiroWebModule(filterConfig.getServletContext()),
                new ExtasWebGuiceModule(),
                new ExtasServicesModule(),
                new WebUIModule());

        logger.debug("Created injector with {} bindings.", INJECTOR.getAllBindings().size());
        super.init(filterConfig);
    }

}
