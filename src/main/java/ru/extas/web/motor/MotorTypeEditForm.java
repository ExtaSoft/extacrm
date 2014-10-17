package ru.extas.web.motor;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import ru.extas.model.motor.MotorType;
import ru.extas.server.motor.MotorTypeRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by Valery on 04.06.2014.
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorTypeEditForm extends ExtaEditForm<MotorType> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("brands")
    private MotorBrandObjMultiselect brandsField;

    public MotorTypeEditForm(final MotorType motorType) {
        super(motorType.isNew() ? "Новый тип техники" : "Редактировать тип техники", new BeanItem(motorType));
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final MotorType obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected MotorType saveObject(final MotorType obj) {
        final MotorType loc = lookup(MotorTypeRepository.class).save(obj);
        NotificationUtil.showSuccess("Тип техники сохранен");
        return loc;
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final MotorType obj) {
        final FormLayout form = new ExtaFormLayout();

        nameField = new EditField("Название типа техники", "Введите название типа техники");
        nameField.setColumns(20);
        nameField.setRequired(true);
        form.addComponent(nameField);

        brandsField = new MotorBrandObjMultiselect("Доступные бренды");
        form.addComponent(brandsField);

        return form;
    }
}
