package ru.extas.web.commons;

import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

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

        window.addCloseListener(event -> editWin.fireCloseForm());
        editWin.addCloseFormListener(event -> window.close());

        if (editWin.getWinHeight() != Sizeable.SIZE_UNDEFINED && editWin.getWinWidth() != Sizeable.SIZE_UNDEFINED)
            editWin.addAttachListener(e -> editWin.adjustSize());
        else
            new WinSizeAdjuster(editWin, window);

        UI.getCurrent().addWindow(window);
    }

    private static class WinSizeAdjuster {

        private int formHeight;
        private final int brwHeight;
        private int wndHeight;
        private boolean adjusted;

        public WinSizeAdjuster(final ExtaEditForm<?> editWin, final Window window) {
            brwHeight = UI.getCurrent().getPage().getBrowserWindowHeight();

            final SizeReporter winSizeReporter = new SizeReporter(window);
            winSizeReporter.addResizeListener(e -> {
                wndHeight = e.getHeight();
                adjustWindow(editWin, window);
            });

            final SizeReporter formSizeReporter = new SizeReporter(editWin);
            formSizeReporter.addResizeListenerOnce(e -> formHeight = e.getHeight());
        }

        protected void adjustWindow(final ExtaEditForm<?> editWin, final Window window) {
            if (!adjusted) {
                if(formHeight != 0 && wndHeight > formHeight || wndHeight >= brwHeight) {
                    if (wndHeight >= brwHeight)
                        window.setHeight(100, Sizeable.Unit.PERCENTAGE);
                    else
                        window.setHeight(wndHeight, Sizeable.Unit.PIXELS);
                    editWin.adjustSize();
                    adjusted = true;
                }
            }
        }
    }
}
