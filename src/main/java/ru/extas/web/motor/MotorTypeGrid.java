package ru.extas.web.motor;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.motor.MotorType;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список типов техники
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:32
 */
public class MotorTypeGrid extends ExtaGrid {


	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new MotorTypeDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<MotorType> container = new ExtaDataContainer<>(MotorType.class);
		container.sort(new Object[]{"name"}, new boolean[]{true});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового типа техники", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<MotorType> newObj = new BeanItem<>(new MotorType());

				final MotorTypeEditForm editWin = new MotorTypeEditForm("Новый тип техники", newObj);
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
				final BeanItem<MotorType> curObj = new GridItem<>(table.getItem(itemId));

				final MotorTypeEditForm editWin = new MotorTypeEditForm("Редактировать тип техники", curObj);
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
