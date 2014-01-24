package ru.extas.web.product;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import ru.extas.model.ProdInstallments;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список продуктов рассрочки
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:33
 */
public class ProdInstallmentsGrid extends ExtaGrid {

	@Override
	protected GridDataDecl createDataDecl() {
		return new ProdInstallmentsDataDecl();
	}

	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<ProdInstallments> container = new ExtaDataContainer<>(ProdInstallments.class);
		container.sort(new Object[]{"createdAt"}, new boolean[]{false});
		container.addNestedContainerProperty("vendor.name");
		return container;
	}

	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового продукта", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<ProdInstallments> newObj = new BeanItem<>(new ProdInstallments());

				final ProdInstallmentsEditForm editWin = new ProdInstallmentsEditForm("Новый продукт \"Рассрочка\"", newObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							((JPAContainer) container).refresh();
							Notification.show("Продукт сохранен", Notification.Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке продукт", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<ProdInstallments> curObj = new BeanItem<>(((EntityItem<ProdInstallments>) table.getItem(itemId)).getEntity());

				final ProdInstallmentsEditForm editWin = new ProdInstallmentsEditForm("Редактировать продукт", curObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							((JPAContainer) container).refreshItem(itemId);
							Notification.show("Продукт сохранен", Notification.Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});

		return actions;
	}

}
