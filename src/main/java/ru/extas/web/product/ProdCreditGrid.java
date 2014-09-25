package ru.extas.web.product;

import com.vaadin.data.Container;
import ru.extas.model.sale.ProdCredit;
import ru.extas.web.commons.*;

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
    public ExtaEditForm<ProdCredit> createEditForm(ProdCredit prodCredit, boolean isInsert) {
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
		final ExtaDataContainer<ProdCredit> container = new ExtaDataContainer<>(ProdCredit.class);
		container.sort(new Object[]{"createdAt"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового страхового продукта"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке страховой продукт"));

		return actions;
	}


}
