package ru.extas.web.product;

import com.vaadin.data.Container;
import ru.extas.model.sale.ProdInstallments;
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
public class ProdInstallmentsGrid extends ExtaGrid<ProdInstallments> {

    public ProdInstallmentsGrid() {
        super(ProdInstallments.class);
    }

    @Override
    public ExtaEditForm<ProdInstallments> createEditForm(final ProdInstallments prodInstallments, final boolean isInsert) {
        return new ProdInstallmentsEditForm(prodInstallments);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdInstallmentsDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDbContainer<ProdInstallments> container = new ExtaDbContainer<>(ProdInstallments.class);
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
