package ru.extas.web.motor;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import ru.extas.model.motor.MotorModel;
import ru.extas.server.motor.MotorModelRepository;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by Valery on 04.06.2014.
 */
public class MotorModelEditForm extends AbstractEditForm<MotorModel> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("code")
    private EditField codeField;

    public MotorModelEditForm(String caption, BeanItem<MotorModel> newObj) {
        super(caption, newObj);
    }

    @Override
    protected void initObject(MotorModel obj) {

    }

    @Override
    protected void saveObject(MotorModel obj) {
        MotorModel loc = lookup(MotorModelRepository.class).save(obj);
        Notification.show("Модель техники сохранена", Notification.Type.TRAY_NOTIFICATION);

    }

    @Override
    protected void checkBeforeSave(MotorModel obj) {

    }

    @Override
    protected ComponentContainer createEditFields(MotorModel obj) {
        final FormLayout form = new FormLayout();

        nameField = new EditField("Название модели", "Введите название модели техники");
        nameField.setColumns(30);
        nameField.setRequired(true);
        form.addComponent(nameField);

        codeField = new EditField("Код модели", "Введите кодовое название модели техники");
        codeField.setColumns(30);
        form.addComponent(codeField);

        return form;
    }
}
