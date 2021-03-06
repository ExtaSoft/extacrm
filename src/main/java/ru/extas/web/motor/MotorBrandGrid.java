package ru.extas.web.motor;

import com.vaadin.data.Container;
import ru.extas.model.motor.MotorBrand;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;

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
public class MotorBrandGrid extends ExtaGrid<MotorBrand> {


    public MotorBrandGrid() {
        super(MotorBrand.class);
    }

    @Override
    public ExtaEditForm<MotorBrand> createEditForm(final MotorBrand motorBrand, final boolean isInsert) {
        return new MotorBrandEditForm(motorBrand);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new MotorBrandDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDbContainer<MotorBrand> container = new ExtaDbContainer<>(MotorBrand.class);
		container.sort(new Object[]{"name"}, new boolean[]{true});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового бренда техники"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке бренд техники"));

		return actions;
	}


}
