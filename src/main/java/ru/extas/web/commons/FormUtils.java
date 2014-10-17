package ru.extas.web.commons;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import java.util.UUID;

/**
 * Вспомогательные методы для работы с формами ввода/редактирования
 *
 * @author Valery Orlov
 *         Date: 23.08.2014
 *         Time: 18:49
 */
public class FormUtils {
    public static void showModalWin(final ExtaEditForm<?> editWin) {

        final Window window = new Window(editWin.getCaption(), editWin);
        window.setClosable(true);
        window.setModal(true);
        window.addCloseListener(event -> editWin.closeForm());
        editWin.addCloseFormListener(event -> window.close());

        final UUID id = UUID.randomUUID();
        window.setId(id.toString());
        JavaScript.getCurrent().addFunction("extaGetHeight",
                arguments -> {
                    final int wndHeight = arguments.getInt(0);
                    final int brwHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
                    if(wndHeight >= brwHeight)
                        window.setHeight(100, Sizeable.Unit.PERCENTAGE);
                    //NotificationUtil.showWarning("Высота окна равна " + wndHeight + "<br/>Высота браузера равна " + brwHeight);
                });
        window.addAttachListener(x ->
                        JavaScript.getCurrent().execute("extaGetHeight(document.getElementById('" + window.getId() + "').clientHeight);")
        );
        UI.getCurrent().addWindow(window);
    }
}
