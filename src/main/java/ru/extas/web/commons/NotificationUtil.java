package ru.extas.web.commons;

import com.google.common.base.Joiner;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

import java.util.List;

/**
 * Упрощаем работу с уведомлениями
 *
 * @author Valery Orlov
 *         Date: 27.09.13
 *         Time: 9:14
 */
public class NotificationUtil {


    public static void showErrors(String caption, List<String> messages) {

        StringBuilder content = new StringBuilder();
        content.append("<ul><li>");
        Joiner.on("</li><li>").appendTo(content, messages);
        content.append("</li></ul>");

        final Notification notify = new Notification(caption, content.toString(), Notification.Type.WARNING_MESSAGE);
        notify.setPosition(Position.MIDDLE_CENTER);
        notify.setHtmlContentAllowed(true);
        notify.show(Page.getCurrent());
    }
}
