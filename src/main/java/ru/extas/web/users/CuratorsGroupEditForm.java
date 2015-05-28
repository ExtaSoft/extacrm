package ru.extas.web.users;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.security.CuratorsGroupRegistry;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.contacts.employee.EmployeeMultySelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода редактирования группы кураторов
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

    @PropertyId("curators")
    private EmployeeMultySelect employeesField;

    public CuratorsGroupEditForm(final CuratorsGroup curatorsGroup) {
        super(curatorsGroup.isNew() ?
        "Ввод новой группы кураторов" :
        "Редактирование группы кураторов", curatorsGroup);

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

        employeesField = new EmployeeMultySelect("Члены группы");
        employeesField.setCompanySupplier(() -> lookup(CompanyRepository.class).findEACompany());
        employeesField.setWidth(100, Unit.PERCENTAGE);
        employeesField.setHeight(370, Unit.PIXELS);
        form.addComponent(employeesField);

        return form;
    }

}
