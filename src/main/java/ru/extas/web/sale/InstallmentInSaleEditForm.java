package ru.extas.web.sale;

import ru.extas.model.product.ProductInstance;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.InstallmentInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author Valery Orlov
 * @author sandarkin
 */
public class InstallmentInSaleEditForm extends InstallmentInSmthEditForm<ProductInstance> {

    public InstallmentInSaleEditForm(final String caption, final ProductInstance targetObject,
                                     final SupplierSer<BigDecimal> priceSupplier,
                                     final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}
