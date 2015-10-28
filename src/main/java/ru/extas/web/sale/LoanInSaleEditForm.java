package ru.extas.web.sale;

import ru.extas.model.sale.ProductInSale;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.LoanInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author Valery Orlov
 * @author sandarkin
 */
public class LoanInSaleEditForm extends LoanInSmthEditForm<ProductInSale> {

    public LoanInSaleEditForm(final String caption, final ProductInSale targetObject,
                              final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}

