package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
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
public class ProdCreditGrid extends ExtaGrid {


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

		actions.add(new UIAction("Новый", "Ввод нового страхового продукта", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<ProdCredit> newObj = new BeanItem<>(new ProdCredit());

				final ProdCreditEditForm editWin = new ProdCreditEditForm("Новый продукт", newObj);
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
				final BeanItem<ProdCredit> curObj = new GridItem<>(table.getItem(itemId));

				final ProdCreditEditForm editWin = new ProdCreditEditForm("Редактировать продукт", curObj);
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
