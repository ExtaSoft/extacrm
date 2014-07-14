package ru.extas.web.motor;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.motor.MotorBrand;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Список брендов техники
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 20:32
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorBrandGrid extends ExtaGrid {


	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new MotorBrandDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<MotorBrand> container = new ExtaDataContainer<>(MotorBrand.class);
		container.sort(new Object[]{"name"}, new boolean[]{true});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового бренда техники", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<MotorBrand> newObj = new BeanItem<>(new MotorBrand());

				final MotorBrandEditForm editWin = new MotorBrandEditForm("Новый бренд", newObj);
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

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке бренд техники", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<MotorBrand> curObj = new GridItem<>(table.getItem(itemId));

				final MotorBrandEditForm editWin = new MotorBrandEditForm("Редактировать бренд", curObj);
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
