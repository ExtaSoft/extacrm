package ru.extas.web.users;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import ru.extas.model.security.UserGroup;
import ru.extas.server.security.UserGroupRegistry;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.motor.MotorBrandMultiselect;
import ru.extas.web.reference.RegionMultiselect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода редактирования группы польователей
 *
 * @author Valery Orlov
 *         Date: 21.06.2014
 *         Time: 15:36
 * @version $Id: $Id
 * @since 0.5.0
 */
public class UserGroupEditForm extends AbstractEditForm<UserGroup> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("description")
    private TextArea descriptionField;

    @PropertyId("permitRegions")
    private RegionMultiselect regionsField;
    @PropertyId("permitBrands")
    private MotorBrandMultiselect brandsField;

    @PropertyId("permissions")
    private ExtaPermissionField permissionsField;

    /**
     * <p>Constructor for UserGroupEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param obj a {@link com.vaadin.data.util.BeanItem} object.
     */
    public UserGroupEditForm(String caption, BeanItem<UserGroup> obj) {
        super(caption, obj);
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(UserGroup obj) {
        if (obj.getId() == null) {
            // Инициализируем новый объект
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(UserGroup obj) {
        final UserGroupRegistry groupRegistry = lookup(UserGroupRegistry.class);
        groupRegistry.save(obj);
        Notification.show("Группа сохранена", Notification.Type.TRAY_NOTIFICATION);
    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(UserGroup obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(UserGroup obj) {
        final FormLayout form = new FormLayout();

        nameField = new EditField("Название");
        nameField.setImmediate(true);
        nameField.setDescription("Введите название группы пользователей");
        nameField.setRequired(true);
        nameField.setRequiredError("Название группы пользователем не может быть пустым. Необходимо ввести название.");
        nameField.setColumns(30);
        form.addComponent(nameField);

        descriptionField = new TextArea("Описание");
        descriptionField.setDescription("Введите описание группы пользователей.");
        descriptionField.setInputPrompt("Описание группы пользователей");
        descriptionField.setNullRepresentation("");
        descriptionField.setColumns(30);
        descriptionField.setRows(3);
        form.addComponent(descriptionField);

        brandsField = new MotorBrandMultiselect("Доступные бренды");
        form.addComponent(brandsField);

        regionsField = new RegionMultiselect("Доступные регионы");
        form.addComponent(regionsField);

        permissionsField = new ExtaPermissionField(obj);
        permissionsField.setCaption("Правила доступа группы");
        form.addComponent(permissionsField);

        return form;
    }

}
