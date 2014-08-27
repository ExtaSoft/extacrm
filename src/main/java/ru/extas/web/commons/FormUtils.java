package ru.extas.web.commons;

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
    public static void showModalWin(AbstractEditForm<?> editWin) {

        final Window window = new Window(editWin.getCaption(), editWin);
        window.setClosable(true);
        window.setModal(true);
        editWin.addCloseFormListener(new AbstractEditForm.CloseFormListener() {
            @Override
            public void closeForm(AbstractEditForm.CloseFormEvent event) {
                window.close();
            }
        });

        UI.getCurrent().addWindow(window);
    }
}
