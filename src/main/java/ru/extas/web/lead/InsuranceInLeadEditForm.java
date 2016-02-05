package ru.extas.web.lead;

import ru.extas.model.lead.ProductInLead;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.InsuranceInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author sandarkin
 * @version 2.0
 */
public class InsuranceInLeadEditForm extends InsuranceInSmthEditForm<ProductInLead> {

    public InsuranceInLeadEditForm(final String caption, final ProductInLead targetObject,
                                   final SupplierSer<BigDecimal> priceSupplier,
                                   final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}
