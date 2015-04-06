package ru.extas.web.sale;

import org.vaadin.viritin.fields.EnumSelect;
import ru.extas.model.sale.Sale;

/**
 * Created by valery on 06.04.15.
 */
public class CancelReasonSelect extends EnumSelect<Sale.CancelReason> {

    public CancelReasonSelect(String caption) {
        super(caption);
    }
}
