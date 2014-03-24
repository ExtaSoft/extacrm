package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.sale.ProdInsurance;
import ru.extas.web.commons.*;

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
public class ProdInsuranceGrid extends ExtaGrid {

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

		actions.add(new UIAction("Новый", "Ввод нового страхового продукта", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<ProdInsurance> newObj = new BeanItem<>(new ProdInsurance());

				final ProdInsuranceEditForm editWin = new ProdInsuranceEditForm("Новая страховая программа", newObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке страховой продукт", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<ProdInsurance> curObj = new GridItem<>(table.getItem(itemId));

				final ProdInsuranceEditForm editWin = new ProdInsuranceEditForm("Редактировать страховую программу", curObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainerItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});

		return actions;
	}

}
