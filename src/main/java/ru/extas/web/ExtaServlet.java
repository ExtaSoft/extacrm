package ru.extas.web;

import com.vaadin.server.*;
import ru.extas.server.settings.UserSettingsService;

import javax.servlet.ServletException;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>ExtaServlet class.</p>
 *
 * @author Valery Orlov
 *         Date: 14.07.2014
 *         Time: 10:47
 * @version $Id: $Id
 * @since 0.5.0
 */
public class ExtaServlet extends VaadinServlet implements SessionInitListener {

    /** {@inheritDoc} */
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        // Системные сообщения
        getService().setSystemMessagesProvider(
                systemMessagesInfo -> {
                    final CustomizedSystemMessages messages = new CustomizedSystemMessages();
                    messages.setSessionExpiredNotificationEnabled(true);
                    messages.setSessionExpiredCaption("Время сессии истекло");
                    messages.setSessionExpiredMessage("Пожалуйста запишите все несохраненные данные и " +
                             "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

                    messages.setCommunicationErrorNotificationEnabled(true);
                    messages.setCommunicationErrorCaption("Потеряно соединение с сервером");
                    messages.setCommunicationErrorMessage("Пожалуйста запишите все несохраненные данные и " +
                             "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

                    messages.setAuthenticationErrorNotificationEnabled(true);
                    messages.setAuthenticationErrorCaption("Ошибка аутентификации");
                    messages.setAuthenticationErrorMessage("Пожалуйста запишите все несохраненные данные и " +
                            "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

                    messages.setInternalErrorNotificationEnabled(true);
                    messages.setInternalErrorCaption("Внутренняя ошибка");
                    messages.setInternalErrorMessage("Пожалуйста сообщите администратору." +
                            "<br/>Пожалуйста запишите все несохраненные данные и " +
                            "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

//                    messages.setOutOfSyncNotificationEnabled(true);
//                    messages.setOutOfSyncCaption("Ошибка синхронизации");
//                    messages.setOutOfSyncMessage("Пожалуйста сообщите администратору." +
//                            "Что-то вызвало рассинхронизацию вашего браузера и сервера." +
//                            "<br/>Пожалуйста запишите все несохраненные данные и " +
//                            "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

                    messages.setCookiesDisabledNotificationEnabled(true);
                    messages.setCookiesDisabledCaption("Файлы cookie отключены");
                    messages.setCookiesDisabledMessage("Данному приложению, для работы, требуются cookie." +
                            "<br/>Пожалуйста включите cookie в вашем браузере и " +
                            "<u>кликните здесь</u> или нажмите клавишу ESC чтобы попробовать снова.");
                    return messages;
                });

        getService().addSessionInitListener(this);
    }


    @Override
    public void sessionInit(final SessionInitEvent event) throws ServiceException {
        event.getSession().addBootstrapListener(MyBootstrapListener.INSTANCE);

    }

    private static class MyBootstrapListener implements BootstrapListener {
        private static final long serialVersionUID = 1L;
        private static final MyBootstrapListener INSTANCE = new MyBootstrapListener();

        @Override
        public void modifyBootstrapPage(final BootstrapPageResponse response) {
            final String iconPath = lookup(UserSettingsService.class).getFaviconPath();
            response.getDocument().head()
                    .getElementsByAttributeValue("rel", "shortcut icon")
                    .attr("href", iconPath);
            response.getDocument().head()
                    .getElementsByAttributeValue("rel", "icon")
                    .attr("href", iconPath);
        }

        @Override
        public void modifyBootstrapFragment(final BootstrapFragmentResponse response) {
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}
