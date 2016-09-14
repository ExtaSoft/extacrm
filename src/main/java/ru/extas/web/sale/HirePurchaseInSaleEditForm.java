package ru.extas.web.sale;

import ru.extas.model.sale.ProductInSale;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.HirePurchaseInSmthEditForm;

import java.math.BigDecimal;

/**
 * @author Valery Orlov
 * @author sandarkin
 */
public class HirePurchaseInSaleEditForm extends HirePurchaseInSmthEditForm<ProductInSale> {

    public HirePurchaseInSaleEditForm(final String caption, final ProductInSale targetObject,
                                      final SupplierSer<BigDecimal> priceSupplier,
                                      final SupplierSer<String> brandSupplier) {
        super(caption, targetObject, priceSupplier, brandSupplier);
    }

}
