package ru.extas.web.product;

import com.vaadin.data.Container;
import ru.extas.model.product.ProdHirePurchase;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список продуктов рассрочки
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:33
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdHirePurchaseGrid extends ExtaGrid<ProdHirePurchase> {

    public ProdHirePurchaseGrid() {
        super(ProdHirePurchase.class);
    }

    @Override
    public ExtaEditForm<ProdHirePurchase> createEditForm(final ProdHirePurchase prod, final boolean isInsert) {
        return new ProdHirePurchaseEditForm(prod);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdHirePurchaseDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDbContainer<ProdHirePurchase> container = new ExtaDbContainer<>(ProdHirePurchase.class);
		container.sort(new Object[]{"createdDate"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового продукта"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке продукт"));

		return actions;
	}

}
