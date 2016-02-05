package ru.extas.web.lead;

import ru.extas.model.lead.ProductInLead;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.LoanInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author sandarkin
 * @version 2.0
 */
public class LoanInLeadEditForm extends LoanInSmthEditForm<ProductInLead> {

    public LoanInLeadEditForm(final String caption, final ProductInLead targetObject,
                              final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}
