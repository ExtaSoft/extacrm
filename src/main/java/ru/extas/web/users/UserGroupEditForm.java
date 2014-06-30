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

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода редактирования группы польователей
 *
 * @author Valery Orlov
 *         Date: 21.06.2014
 *         Time: 15:36
 */
public class UserGroupEditForm extends AbstractEditForm<UserGroup> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("description")
    private TextArea descriptionField;

    @PropertyId("permissions")
    private ExtaPermissionField permissionsField;

    public UserGroupEditForm(String caption, BeanItem<UserGroup> obj) {
        super(caption, obj);
    }

    @Override
    protected void initObject(UserGroup obj) {
        if (obj.getId() == null) {
            // Инициализируем новый объект
        }
    }

    @Override
    protected void saveObject(UserGroup obj) {
        final UserGroupRegistry groupRegistry = lookup(UserGroupRegistry.class);
        groupRegistry.save(obj);
        Notification.show("Группа сохранена", Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    protected void checkBeforeSave(UserGroup obj) {

    }

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

        permissionsField = new ExtaPermissionField(obj);
        permissionsField.setCaption("Правила доступа группы");
        form.addComponent(permissionsField);

        return form;
    }

}
