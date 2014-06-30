package ru.extas.web.motor;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import ru.extas.model.motor.MotorType;
import ru.extas.server.motor.MotorTypeRepository;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by Valery on 04.06.2014.
 */
public class MotorTypeEditForm extends AbstractEditForm<MotorType> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("brands")
    private MotorBrandObjMultiselect brandsField;

    public MotorTypeEditForm(String caption, BeanItem<MotorType> newObj) {
        super(caption, newObj);
    }

    @Override
    protected void initObject(MotorType obj) {

    }

    @Override
    protected void saveObject(MotorType obj) {
        MotorType loc = lookup(MotorTypeRepository.class).save(obj);
        Notification.show("Тип техники сохранен", Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    protected void checkBeforeSave(MotorType obj) {

    }

    @Override
    protected ComponentContainer createEditFields(MotorType obj) {
        final FormLayout form = new FormLayout();

        nameField = new EditField("Название типа техники", "Введите название типа техники");
        nameField.setColumns(20);
        nameField.setRequired(true);
        form.addComponent(nameField);

        brandsField = new MotorBrandObjMultiselect("Доступные бренды");
        form.addComponent(brandsField);

        return form;
    }
}
