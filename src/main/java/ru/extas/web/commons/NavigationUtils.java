package ru.extas.web.commons;

import com.vaadin.server.Page;
import com.vaadin.ui.UI;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Valery Orlov
 *         Date: 28.08.2014
 *         Time: 2:19
 */
public class NavigationUtils {

    public static void setUriFragment(ExtaUri uri) {
        UI.getCurrent().getPage().setUriFragment("!" + uri.toString(), false);
    }

    public static String getUriFragment() {
        String uriPart = Page.getCurrent().getUriFragment();
        uriPart = sanityUri(uriPart);
        return uriPart;
    }

    public static String sanityUri(String uriPart) {
        if (!isNullOrEmpty(uriPart) && uriPart.startsWith("!"))
            uriPart = uriPart.substring(1);
        return uriPart;
    }
}
