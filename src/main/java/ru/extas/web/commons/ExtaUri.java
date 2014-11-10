package ru.extas.web.commons;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.extas.model.security.ExtaDomain;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.EnumSet.allOf;

/**
 * Класс разгрызает URI приложения на составные части
 *
 * @author Valery Orlov
 *         Date: 22.08.2014
 *         Time: 14:29
 */
public class ExtaUri implements Serializable {

    public static final char SEPARATOR = '/';
    public static final String ID_PRM_NAME = "id";
    private String domainPrefix;
    private String subdomain;
    private Mode mode;
    private String id;
    private ExtaDomain domain;

    public ExtaUri(final ExtaDomain domain, final Mode mode, final String id) {
        this.mode = mode;
        setId(id);
        this.domain = domain;

        final Iterable<String> uriPieces = Splitter.on(SEPARATOR).split(domain.getName());

        domainPrefix = Iterables.get(uriPieces, 0, null);
        subdomain = Iterables.get(uriPieces, 1, null);
    }

    /**
     * Инициализируется текуцим URI приложения
     */
    public ExtaUri() {
        this(null);
    }

    /**
     * Инициализируется заданным URI
     *
     * @param uriPart заданный URI
     */
    public ExtaUri(String uriPart) {
        if (isNullOrEmpty(uriPart)) uriPart = NavigationUtils.getUriFragment();
        else uriPart = NavigationUtils.sanityUri(uriPart);

        if (!isNullOrEmpty(uriPart)) {
            try {
                final UriComponents uriComponents = UriComponentsBuilder.fromUri(new URI(uriPart)).build();
                final List<String> idParam = uriComponents.getQueryParams().get(ID_PRM_NAME);
                if (!CollectionUtils.isEmpty(idParam))
                    id = idParam.get(0);

                final List<String> uriPieces = uriComponents.getPathSegments();
                domainPrefix = Iterables.get(uriPieces, 0, null);
                subdomain = Iterables.get(uriPieces, 1, null);

                final String modeStr = Iterables.get(uriPieces, 2, null);
                if (isNullOrEmpty(modeStr))
                    mode = Mode.GRID;
                else if (modeStr.equals(Mode.NEW.getName()))
                    mode = Mode.NEW;
                else if (modeStr.equals(Mode.EDIT.getName()))
                    mode = Mode.EDIT;
                else if (modeStr.equals(Mode.VIEW.getName()))
                    mode = Mode.VIEW;
            } catch (final URISyntaxException e) {
            }

            final StringBuilder domainName = new StringBuilder();
            if (!isNullOrEmpty(domainPrefix)) {
                domainName.append(domainPrefix);
                if (!isNullOrEmpty(subdomain))
                    domainName.append(SEPARATOR).append(subdomain);

                domain = allOf(ExtaDomain.class).stream()
                        .filter(input -> input.getName().equals(domainName.toString()))
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (domain != null)
            builder.append(domain.getName());
        if (mode != null && mode != Mode.GRID) {
            builder.append(SEPARATOR);
            builder.append(mode.getName());
        }
        if (!isNullOrEmpty(id)) {
            builder.append(SEPARATOR);
            builder.append("?");
            builder.append(ID_PRM_NAME);
            builder.append("=");
            builder.append(id);
        }
        return builder.toString();
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

    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    public String getId() {
        if (isNullOrEmpty(id))
            return null;
        return UUIDUtils.fromUrl(id);
    }

    public void setId(final String id) {
        if (!isNullOrEmpty(id))
            this.id = UUIDUtils.toUrl(id);
        else
            this.id = null;
    }

    public ExtaDomain getDomain() {
        return domain;
    }

    public void setDomain(final ExtaDomain domain) {
        this.domain = domain;
    }

    public enum Mode {
        GRID(null),
        NEW("new"),
        EDIT("edit"),
        VIEW("view");

        private final String name;

        Mode(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
