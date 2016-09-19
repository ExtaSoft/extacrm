package ru.extas.web.product;

import com.vaadin.ui.ComboBox;
import ru.extas.model.product.ProductInstance;
import ru.extas.web.util.ComponentUtil;

/**
 * Выбор статуса продукта в продаже
 *
 * @author Valery Orlov
 *         Date: 17.03.2015
 *         Time: 14:44
 */
public class ProdInstanceStateSelect extends ComboBox {

    public ProdInstanceStateSelect(final String caption, final String description) {
        super(caption);

        setDescription(description);
        setWidth(15, Unit.EM);
        setImmediate(true);
        setScrollToSelectedItem(true);

        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(this, ProductInstance.State.class);
    }
}
