package ru.extas.web.product;

import ru.extas.model.product.ProdHirePurchase;

/**
 * Селектор продуктов Аренда с выкупом
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 20:22
 */
public class ProdHirePurchaseField extends ProductField<ProdHirePurchase> {

    public ProdHirePurchaseField(final String caption, final String description) {
        super(caption, description, ProdHirePurchase.class);
    }

}
