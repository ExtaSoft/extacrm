package ru.extas.web.product;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.ProdInsurance;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список страховых продуктов
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:33
 */
public class ProdInsuranceGrid extends ExtaGrid {

	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdInsuranceDataDecl();
	}

	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<ProdInsurance> container = new ExtaDataContainer<>(ProdInsurance.class);
		container.sort(new Object[]{"createdAt"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

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
							((JPAContainer) container).refresh();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке страховой продукт", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<ProdInsurance> curObj = new BeanItem<>(((EntityItem<ProdInsurance>) table.getItem(itemId)).getEntity());

				final ProdInsuranceEditForm editWin = new ProdInsuranceEditForm("Редактировать страховую программу", curObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							((JPAContainer) container).refreshItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});

		return actions;
	}

}
