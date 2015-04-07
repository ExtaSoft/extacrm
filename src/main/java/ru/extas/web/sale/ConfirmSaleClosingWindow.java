package ru.extas.web.sale;

import org.vaadin.viritin.layouts.MVerticalLayout;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.window.YesNoWindow;

/**
 * @author Valery Orlov
 *         Date: 07.04.2015
 *         Time: 13:03
 */
public class ConfirmSaleClosingWindow extends YesNoWindow {

    private final CancelReasonSelect reasonSelect;

    public ConfirmSaleClosingWindow() {
        super("Вы действительно хотите отменить продажу?");

        reasonSelect = new CancelReasonSelect();
        reasonSelect.setValue(Sale.CancelReason.VENDOR_REJECTED);
        setContent(new MVerticalLayout(reasonSelect).withMargin(true));
    }

    public Sale.CancelReason getReason(){
        return reasonSelect.getValue();
    }
}
