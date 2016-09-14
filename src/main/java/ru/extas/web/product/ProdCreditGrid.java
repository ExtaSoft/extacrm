package ru.extas.web.product;

import com.vaadin.data.Container;
import ru.extas.model.product.ProdCredit;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список кредитных продуктов
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:32
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCreditGrid extends ExtaGrid<ProdCredit> {


    public ProdCreditGrid() {
        super(ProdCredit.class);
    }

    @Override
    public ExtaEditForm<ProdCredit> createEditForm(final ProdCredit prodCredit, final boolean isInsert) {
        return new ProdCreditEditForm(prodCredit);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdCreditDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDbContainer<ProdCredit> container = new ExtaDbContainer<>(ProdCredit.class);
		container.sort(new Object[]{"createdDate"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового страхового продукта"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке страховой продукт"));

		return actions;
	}


}
