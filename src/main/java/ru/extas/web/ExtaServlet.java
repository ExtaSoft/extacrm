package ru.extas.web;

import com.vaadin.server.*;

import javax.servlet.ServletException;

/**
 * @author Valery Orlov
 *         Date: 14.07.2014
 *         Time: 10:47
 */
public class ExtaServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        // Системные сообщения
        getService().setSystemMessagesProvider(
                new SystemMessagesProvider() {
                    @Override
                    public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                        CustomizedSystemMessages messages = new CustomizedSystemMessages();
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

                        messages.setOutOfSyncNotificationEnabled(true);
                        messages.setOutOfSyncCaption("Ошибка синхронизации");
                        messages.setOutOfSyncMessage("Пожалуйста сообщите администратору." +
                                "Что-то вызвало рассинхронизацию вашего браузера и сервера." +
                                "<br/>Пожалуйста запишите все несохраненные данные и " +
                                "<u>кликните здесь</u> или нажмите клавишу ESC чтобы продолжить работу.");

                        messages.setCookiesDisabledNotificationEnabled(true);
                        messages.setCookiesDisabledCaption("Файлы cookie отключены");
                        messages.setCookiesDisabledMessage("Данному приложению, для работы, требуются cookie." +
                                "<br/>Пожалуйста включите cookie в вашем браузере и " +
                                "<u>кликните здесь</u> или нажмите клавишу ESC чтобы попробовать снова.");
                        return messages;
                    }
                });
    }
}
