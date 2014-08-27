package ru.extas.web.motor;

import com.vaadin.data.Container;
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
 * @since 0.5.0
 */
public class MotorModelGrid extends ExtaGrid<MotorModel> {


    public MotorModelGrid() {
        super(MotorModel.class);
    }

    @Override
    public ExtaEditForm<MotorModel> createEditForm(MotorModel motorModel) {
        return new MotorModelEditForm(motorModel);
    }

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
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод новой модели техники"));
		actions.add(new EditObjectAction("Изменить", "Редактировать выделенную в списке модель техники"));

		return actions;
	}

}
