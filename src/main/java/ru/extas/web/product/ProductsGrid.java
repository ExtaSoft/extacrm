package ru.extas.web.product;

import com.vaadin.data.Container;
import ru.extas.model.sale.Product;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <p>ProductsGrid class.</p>
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 17:15
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductsGrid extends ExtaGrid<Product> {

    public ProductsGrid() {
        super(Product.class);
    }

    @Override
    public ExtaEditForm<Product> createEditForm(final Product product, final boolean isInsert) {
        return null;
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new ProductDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDbContainer<Product> container = new ExtaDbContainer<>(Product.class);
		container.sort(new Object[]{"createdDate"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		return newArrayList();
	}
}
