package ru.extas.web.product;

import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.Product;

/**
 * Селектор кредитных продуктов
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 20:22
 */
public class ProdCreditField extends ProductField<ProdCredit> {

    public ProdCreditField(final String caption, final String description) {
        super(caption, description, ProdCredit.class);
    }

}
