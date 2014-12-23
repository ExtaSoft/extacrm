package ru.extas.web.product;

import ru.extas.model.sale.ProdInstallments;

/**
 * Селектор продуктов рассрочки
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 20:22
 */
public class ProdInstallmentsField extends ProductField<ProdInstallments> {

    public ProdInstallmentsField(String caption, String description) {
        super(caption, description, ProdInstallments.class);
    }

}
