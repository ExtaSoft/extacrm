package ru.extas.web.motor;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import ru.extas.model.motor.MotorBrand;
import ru.extas.server.motor.MotorBrandRepository;
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
public class MotorBrandEditForm extends ExtaEditForm<MotorBrand> {
    @PropertyId("name")
    private EditField nameField;

    @PropertyId("brandTypes")
    private MotorTypeObjMultiselect typesField;

    public MotorBrandEditForm(MotorBrand motorBrand) {
        super(motorBrand.isNew() ? "Новый бренд" : "Редактировать бренд", new BeanItem(motorBrand));
    }


    /** {@inheritDoc} */
    @Override
    protected void initObject(MotorBrand obj) {
    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(MotorBrand obj) {
        MotorBrand loc = lookup(MotorBrandRepository.class).save(obj);
        NotificationUtil.showSuccess("Марка сохранена");
    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(MotorBrand obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(MotorBrand obj) {
        final FormLayout form = new ExtaFormLayout();

        nameField = new EditField("Название марки техники", "Введите название марки техники");
        nameField.setColumns(20);
        nameField.setRequired(true);
        form.addComponent(nameField);

        typesField = new MotorTypeObjMultiselect("Выпускаемая техника");
        form.addComponent(typesField);

        return form;
    }
}
