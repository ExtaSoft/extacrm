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
 * @version $Id: $Id
 * @since 0.3
 */
public class NotificationUtil {


    /**
     * <p>showErrors.</p>
     *
     * @param caption  a {@link java.lang.String} object.
     * @param messages a {@link java.util.List} object.
     */
    public static void showErrors(final String caption, final List<String> messages) {

        final StringBuilder content = new StringBuilder();
        content.append("<ul><li>");
        Joiner.on("</li><li>").appendTo(content, messages);
        content.append("</li></ul>");

        final String description = content.toString();
        showError(caption, description);
    }

    public static void showError(final String caption, final String description) {
        final Notification notify = new Notification(caption, description, Notification.Type.ERROR_MESSAGE);
        notify.setStyleName(
                ExtaTheme.NOTIFICATION_ERROR + " " +
//                        ExtaTheme.NOTIFICATION_FAILURE + " " +
                        ExtaTheme.NOTIFICATION_CLOSABLE);
        notify.setPosition(Position.MIDDLE_CENTER);
        notify.setHtmlContentAllowed(true);
        notify.show(Page.getCurrent());
    }

    public static void showWarning(final String caption, final String description) {
        final Notification notify = new Notification(caption, description, Notification.Type.WARNING_MESSAGE);
        notify.setStyleName(ExtaTheme.NOTIFICATION_WARNING + " " +
                ExtaTheme.NOTIFICATION_CLOSABLE);
        notify.setPosition(Position.MIDDLE_CENTER);
        notify.setHtmlContentAllowed(true);
        notify.show(Page.getCurrent());
    }

    public static void showSuccess(final String caption) {
        showSuccess(caption, null);

    }

    public static void showSuccess(final String caption, final String description) {
        final Notification notify = new Notification(caption, description, Notification.Type.TRAY_NOTIFICATION);
        notify.setStyleName(
                ExtaTheme.NOTIFICATION_TRAY + " " +
                        ExtaTheme.NOTIFICATION_SUCCESS + " " +
                        ExtaTheme.NOTIFICATION_CLOSABLE);
        notify.setHtmlContentAllowed(true);
        notify.show(Page.getCurrent());
    }

    public static void showWarning(final String caption) {
        showWarning(caption, null);
    }

    public static void show(final String caption, final String description) {
        final Notification notify = new Notification(caption, description, Notification.Type.HUMANIZED_MESSAGE);
        notify.setStyleName(ExtaTheme.NOTIFICATION_CLOSABLE);
        notify.setHtmlContentAllowed(true);
        notify.show(Page.getCurrent());
    }

    public static void show(final String caption) {
        show(caption, null);
    }
}
