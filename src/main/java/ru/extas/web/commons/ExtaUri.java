package ru.extas.web.commons;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import ru.extas.model.security.ExtaDomain;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.EnumSet.allOf;

/**
 * Класс разгрызает URI приложения на составные части
 *
 * @author Valery Orlov
 *         Date: 22.08.2014
 *         Time: 14:29
 */
public class ExtaUri {

    public enum Mode {
        GRID(null),
        NEW("new"),
        EDIT("edit"),
        VIEW("view");

        private String name;

        Mode(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final String uri;
    private final String domainPrefix;
    private final String subdomain;
    private Mode mode;
    private final String id;
    private ExtaDomain domain;

    /**
     * Инициализируется текуцим URI приложения
     */
    public ExtaUri() {
        this(null);
    }

    /**
     * Инициализируется заданным URI
     * @param uriPart заданный URI
     */
    public ExtaUri(String uriPart) {
        uriPart = uriPart == null ? Page.getCurrent().getUriFragment() : uriPart;
        if (uriPart.startsWith("!"))
            uriPart = uriPart.substring(1);
        this.uri = uriPart;

        Iterable<String> uriPieces = Splitter.on('/').split(uri);

        domainPrefix = Iterables.get(uriPieces, 0, null);

        subdomain = Iterables.get(uriPieces, 1, null);

        String modeStr = Iterables.get(uriPieces, 2, null);
        if(isNullOrEmpty(modeStr))
            mode = Mode.GRID;
        else if(modeStr.equals(Mode.NEW.getName()))
            mode = Mode.NEW;
        else if(modeStr.equals(Mode.EDIT.getName()))
            mode = Mode.EDIT;
        else if(modeStr.equals(Mode.VIEW.getName()))
            mode = Mode.VIEW;

        id = VaadinService.getCurrentRequest().getParameter("id");

        if(!isNullOrEmpty(domainPrefix) && !isNullOrEmpty(subdomain)) {
            final String fullDomainName = Joiner.on("/").join(domainPrefix, subdomain);
            domain = Iterables.tryFind(allOf(ExtaDomain.class), new Predicate<ExtaDomain>() {
                @Override
                public boolean apply(ExtaDomain input) {
                    return input.getName().equals(fullDomainName);
                }
            }).orNull();
        }
    }

    public String getUri() {
        return uri;
    }

    public String getDomainPrefix() {
        return domainPrefix;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public Mode getMode() {
        return mode;
    }

    public String getId() {
        return id;
    }

    public ExtaDomain getDomain() {
        return domain;
    }
}
