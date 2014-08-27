package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import ru.extas.model.sale.ProdInsurance;
import ru.extas.web.commons.*;
import ru.extas.web.commons.AbstractEditForm;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список страховых продуктов
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:33
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdInsuranceGrid extends ExtaGrid<ProdInsurance> {

    public ProdInsuranceGrid() {
        super(ProdInsurance.class);
    }

    @Override
    public AbstractEditForm<ProdInsurance> createEditForm(ProdInsurance prodInsurance) {
        return new ProdInsuranceEditForm(prodInsurance);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdInsuranceDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<ProdInsurance> container = new ExtaDataContainer<>(ProdInsurance.class);
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
