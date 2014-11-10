package ru.extas.web.commons;

import com.vaadin.data.Item;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;

import java.text.MessageFormat;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Генерирует колонку со ссылкой на e-mail
 *
 * @author Valery Orlov
 *         Date: 20.10.2014
 *         Time: 12:53
 */
public class EmailLinkColumnGen extends GridDataDecl.ComponentColumnGenerator {

    @Override
    public Object generateCell(final Object columnId, final Item item, final Object itemId) {
        final String url = (String) item.getItemProperty(columnId).getValue();
        if (!isNullOrEmpty(url)) {
            final Link link = new Link(url, new ExternalResource(MessageFormat.format("mailto:{0}", url)));
            link.setTargetName("_blank");
            return link;
        } else
            return null;
    }

}
