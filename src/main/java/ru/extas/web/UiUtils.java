package ru.extas.web;

import com.google.common.base.Throwables;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Field;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.ExtaException;
import ru.extas.web.commons.NotificationUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Optional;

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
    public static void initUi(final UI ui) {
        // Регистрируем конверторы по умолчанию
        VaadinSession.getCurrent().setConverterFactory(new ExtaConverterFactory());

        // Устанавливаем локаль
        final Locale uiLocale = lookup(Locale.class);
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
            final Throwable throwable = event.getThrowable();
            final Throwable rootThrowable = Throwables.getRootCause(throwable);
            final Optional<Throwable> appThrowable = Throwables.getCausalChain(throwable).stream().filter(t -> t instanceof ExtaException).findFirst();
            logger.error("", throwable);

            if (appThrowable.isPresent()){
                NotificationUtil.showError("Ошибка при работе прилоения", appThrowable.get().getLocalizedMessage());
            }else {
                final StringWriter strWr = new StringWriter();
                strWr.append("<div class='exceptionStackTraceBox'><pre>");
                rootThrowable.printStackTrace(new PrintWriter(strWr, true));
                strWr.append("</pre></div>");

                // Display the error message in a custom fashion
                NotificationUtil.showError("Непредусмотренная ошибка", strWr.toString());
            }
        }
    }

    /**
     * <p>showValidationError.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param fieldGroup a {@link com.vaadin.data.fieldgroup.FieldGroup} object.
     */
    public static void showValidationError(final String caption, final FieldGroup fieldGroup) {
        final StringBuilder msg = new StringBuilder();
        msg.append("<ul>");
        for (final Field<?> field : fieldGroup.getFields()) {
            try {
                field.validate();
            } catch (final Validator.InvalidValueException e) {
                msg.append("<li>");
                msg.append(e.getLocalizedMessage());
                msg.append("</li>");
            }
        }
        msg.append("</ul>");

        showWarning(caption, msg.toString());
    }


}
