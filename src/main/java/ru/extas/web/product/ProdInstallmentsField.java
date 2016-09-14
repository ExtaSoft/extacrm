package ru.extas.web.product;

import ru.extas.model.product.ProdInstallments;

/**
 * Селектор продуктов рассрочки
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 20:22
 */
public class ProdInstallmentsField extends ProductField<ProdInstallments> {

    public ProdInstallmentsField(final String caption, final String description) {
        super(caption, description, ProdInstallments.class);
    }

}
