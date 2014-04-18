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
    protected Component getContent() {
        final Component msg = new Label("Пожалуйста выберите другой пункт главного меню!");
        msg.setSizeUndefined();
        msg.addStyleName("h2");
        return msg;
    }

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Неверные данные в адресной строке!!!");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }
}
