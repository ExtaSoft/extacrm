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
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorModelEditForm extends AbstractEditForm<MotorModel> {

    @PropertyId("type")
    private MotorTypeSelect typeField;
    @PropertyId("brand")
    private MotorBrandSelect brandField;
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("code")
    private EditField codeField;

    /**
     * <p>Constructor for MotorModelEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param newObj a {@link com.vaadin.data.util.BeanItem} object.
     */
    public MotorModelEditForm(String caption, BeanItem<MotorModel> newObj) {
        super(caption, newObj);
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(MotorModel obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(MotorModel obj) {
        MotorModel loc = lookup(MotorModelRepository.class).save(obj);
        Notification.show("Модель техники сохранена", Notification.Type.TRAY_NOTIFICATION);

    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(MotorModel obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(MotorModel obj) {
        final FormLayout form = new FormLayout();

        typeField = new MotorTypeSelect();
        typeField.setRequired(true);
        form.addComponent(typeField);

        brandField = new MotorBrandSelect();
        brandField.setRequired(true);
        brandField.linkToType(typeField);
        form.addComponent(brandField);

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
