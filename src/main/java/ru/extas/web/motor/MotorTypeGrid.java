package ru.extas.web.motor;

import com.vaadin.data.Container;
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
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorTypeGrid extends ExtaGrid<MotorType> {

    public MotorTypeGrid() {
        super(MotorType.class);
    }

    @Override
    public ExtaEditForm<MotorType> createEditForm(final MotorType motorType, final boolean isInsert) {
        return new MotorTypeEditForm(motorType);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new MotorTypeDataDecl();
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaJpaContainer<MotorType> container = new ExtaJpaContainer<>(MotorType.class);
		container.sort(new Object[]{"name"}, new boolean[]{true});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового типа техники"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке страховой продукт"));

		return actions;
	}


}
