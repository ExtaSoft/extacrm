package ru.extas.web.users;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import org.springframework.cache.CacheManager;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.server.security.CuratorsGroupRegistry;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;

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
public class CuratorsGroupEditForm extends ExtaEditForm<CuratorsGroup> {

    @PropertyId("name")
    private EditField nameField;

    @PropertyId("description")
    private TextArea descriptionField;

    public CuratorsGroupEditForm(final CuratorsGroup curatorsGroup) {
        super(curatorsGroup.isNew() ?
        "Ввод новой группы пользователей" :
        "Редактирование группы", curatorsGroup);

        setWinWidth(1000, Unit.PIXELS);
        setWinHeight(600, Unit.PIXELS);
    }

    /** {@inheritDoc} */
    @Override
    protected void initEntity(final CuratorsGroup group) {
        if (group.isNew()) {
            // Инициализируем новый объект
        }
    }

    /** {@inheritDoc} */
    @Override
    protected CuratorsGroup saveEntity(CuratorsGroup group) {
        final CuratorsGroupRegistry groupRegistry = lookup(CuratorsGroupRegistry.class);
        group = groupRegistry.save(group);
        lookup("cacheManager", CacheManager.class).getCache("userByLogin").clear();
        NotificationUtil.showSuccess("Группа сохранена");
        return group;
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

        nameField = new EditField("Название");
        nameField.setImmediate(true);
        nameField.setDescription("Введите название группы пользователей");
        nameField.setRequired(true);
        nameField.setRequiredError("Название группы пользователем не может быть пустым. Необходимо ввести название.");
        nameField.setColumns(30);
        form.addComponent(nameField);

        descriptionField = new TextArea("Описание");
        descriptionField.setImmediate(true);
        descriptionField.setDescription("Введите описание группы пользователей.");
        descriptionField.setInputPrompt("Описание группы пользователей");
        descriptionField.setNullRepresentation("");
        descriptionField.setRows(2);
        form.addComponent(descriptionField);

        return form;
    }

}
