package ru.extas.web;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import ru.extas.web.commons.ExtaAbstractView;

/**
 * Страница ошибки
 *
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 13:56
 * @version $Id: $Id
 * @since 0.4.2
 */
public class ErrorView extends ExtaAbstractView {
    /** {@inheritDoc} */
    @Override
    protected Component createContent() {
        final Component msg = new Label("Пожалуйста выберите другой пункт главного меню!");
        msg.setSizeUndefined();
        msg.addStyleName("view-title");
        return msg;
    }

    /** {@inheritDoc} */
    @Override
    protected Component createTitle() {
        final Component title = new Label("Неверные данные в адресной строке!!!");
        title.setSizeUndefined();
        title.addStyleName("view-title");
        return title;
    }
}
