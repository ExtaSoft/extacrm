package ru.extas.web.motor;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.motor.MotorModel;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список моделей техники
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:32
 * @version $Id: $Id
 */
public class MotorModelGrid extends ExtaGrid {


	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new MotorModelDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<MotorModel> container = new ExtaDataContainer<>(MotorModel.class);
		container.sort(new Object[]{"name"}, new boolean[]{true});
		container.addNestedContainerProperty("type.name");
		container.addNestedContainerProperty("brand.name");
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод новой модели техники", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<MotorModel> newObj = new BeanItem<>(new MotorModel());

				final MotorModelEditForm editWin = new MotorModelEditForm("Новая модель техники", newObj);
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

		actions.add(new DefaultAction("Изменить", "Редактировать выделенную в списке модельтехники", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<MotorModel> curObj = new GridItem<>(table.getItem(itemId));

				final MotorModelEditForm editWin = new MotorModelEditForm("Редактировать модель техники", curObj);
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
