package ru.extas.web;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.NotificationUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.NotificationUtil.showWarning;

/**
 * Всякие вспомогательные методы для Vaadin UI
 *
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 17:04
 * @version $Id: $Id
 * @since 0.4.2
 */
public class UiUtils {

    /**
     * <p>initUi.</p>
     *
     * @param ui a {@link com.vaadin.ui.UI} object.
     */
    public static void initUi(UI ui) {
        // Регистрируем конверторы по умолчанию
        VaadinSession.getCurrent().setConverterFactory(new ExtaConverterFactory());

        // Устанавливаем локаль
        Locale uiLocale = lookup(Locale.class);
        VaadinSession.getCurrent().setLocale(uiLocale);

        // Configure the error handler for the UI
        ui.setErrorHandler(new UiErrorHandler());

    }

    private static class UiErrorHandler extends DefaultErrorHandler {
        private static final long serialVersionUID = 1L;
        private final static Logger logger = LoggerFactory.getLogger(UiErrorHandler.class);

        @Override
        public void error(final com.vaadin.server.ErrorEvent event) {
            // Протоколируем ошибку
            logger.error("", event.getThrowable());

            final StringWriter strWr = new StringWriter();
            strWr.append("<div class='exceptionStackTraceBox'><pre>");
            event.getThrowable().printStackTrace(new PrintWriter(strWr, true));
            strWr.append("</pre></div>");

            // Display the error message in a custom fashion
            NotificationUtil.showError("Непредусмотренная ошибка", strWr.toString());
        }
    }

    /**
     * <p>showValidationError.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param fieldGroup a {@link com.vaadin.data.fieldgroup.FieldGroup} object.
     */
    public static void showValidationError(String caption, FieldGroup fieldGroup) {
        StringBuilder msg = new StringBuilder();
        msg.append("<ul>");
        for (Field<?> field : fieldGroup.getFields()) {
            try {
                field.validate();
            } catch (Validator.InvalidValueException e) {
                msg.append("<li>");
                msg.append(e.getLocalizedMessage());
                msg.append("</li>");
            }
        }
        msg.append("</ul>");

        showWarning(caption, msg.toString());
    }


}
