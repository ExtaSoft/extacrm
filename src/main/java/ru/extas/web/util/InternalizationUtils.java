package ru.extas.web.util;

import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;
import org.joda.time.DateTimeZone;

/**
 * @author Valery Orlov
 *         Date: 17.09.2014
 *         Time: 23:31
 */
public class InternalizationUtils {

    public DateTimeZone getClientTimeZone() {

        UI ui = UI.getCurrent();
        if (ui != null) {
            Page page = ui.getPage();
            if (page != null) {
                WebBrowser br = page.getWebBrowser();
                if (br != null) {
                    int timezoneOffset = br.getRawTimezoneOffset();
                    return DateTimeZone.forOffsetMillis(timezoneOffset);
                }
            }
        }
        return DateTimeZone.forID("Europe/Moscow");
    }
}
