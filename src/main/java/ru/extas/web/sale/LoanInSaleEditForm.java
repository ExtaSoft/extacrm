package ru.extas.web.sale;

import ru.extas.model.product.ProductInstance;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.LoanInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author Valery Orlov
 * @author sandarkin
 */
public class LoanInSaleEditForm extends LoanInSmthEditForm<ProductInstance> {

    public LoanInSaleEditForm(final String caption, final ProductInstance targetObject,
                              final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}

