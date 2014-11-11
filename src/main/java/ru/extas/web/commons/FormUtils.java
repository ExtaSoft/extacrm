package ru.extas.web.commons;

import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import java.text.MessageFormat;
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

        if (editWin.getWinHeight() != Sizeable.SIZE_UNDEFINED)
            window.setHeight(editWin.getWinHeight(), editWin.getWinHeightUnit());
        if (editWin.getWinWidth() != Sizeable.SIZE_UNDEFINED)
            window.setWidth(editWin.getWinWidth(), editWin.getWinWidthUnit());

        window.addCloseListener(event -> editWin.closeForm());
        editWin.addCloseFormListener(event -> window.close());

        final UUID id = UUID.randomUUID();
        window.setId(id.toString());
        JavaScript.getCurrent().addFunction("extaGetHeight",
                arguments -> {
                    final int wndHeight = arguments.getJSONObject(0).getInt("height");
                    final int brwHeight = UI.getCurrent().getPage().getBrowserWindowHeight();
                    if (wndHeight >= brwHeight)
                        window.setHeight(100, Sizeable.Unit.PERCENTAGE);
                    else
                        window.setHeight(wndHeight, Sizeable.Unit.PIXELS);
                    editWin.adjustSize();
                });
        final SizeReporter sizeReporter = new SizeReporter(window);
        sizeReporter.addResizeListenerOnce(e -> {
            final String script = MessageFormat.format(
                    "extaGetHeight(document.getElementById(''{0}'').getBoundingClientRect());", window.getId());
            JavaScript.getCurrent().execute(script);
        });
//        window.addAttachListener(e -> {
//            final String script = MessageFormat.format(
//                    "extaGetHeight(document.getElementById(''{0}'').getBoundingClientRect());", window.getId());
//            JavaScript.getCurrent().execute(script);
//        });
        UI.getCurrent().addWindow(window);
    }
}
