package ru.extas.web.tasks;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import ru.extas.model.Lead;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static com.google.gwt.thirdparty.guava.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 */
public class TaskEditForm extends AbstractEditForm<Task> {

    private static final long serialVersionUID = 9510268415882116L;
    private boolean taskCompleted = false;

    // Компоненты редактирования
    // Имя клиента
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("description")
    private TextArea descriptionField;
    @PropertyId("dueDate")
    private PopupDateField dueDateField;
    @PropertyId("owner")
    private EditField ownerField;
    @PropertyId("assignee")
    private EditField assigneeField;

    public TaskEditForm(final String caption, final BeanItem<Task> obj) {
        super(caption, obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Task obj) {
        VerticalLayout formsContainer = new VerticalLayout();
        final FormLayout form = new FormLayout();

        FormService formService = lookup(FormService.class);
        RuntimeService runtimeService = lookup(RuntimeService.class);


        TaskFormData taskData = formService.getTaskFormData(obj.getId());
        List<FormProperty> formProps = taskData.getFormProperties();
        Optional<FormProperty> result = Iterators.tryFind(formProps.iterator(), new Predicate<FormProperty>() {
            @Override
            public boolean apply(FormProperty input) {
                return input.getId().equals("result");
            }
        });
        HorizontalLayout finishToolBar = new HorizontalLayout();
        finishToolBar.setSpacing(true);
        if (result.isPresent()) {
            // Кнопки завершения задачи
            FormType resultType = result.get().getType();
            Map<String, String> resultValues = (Map<String, String>) resultType.getInformation("values");
            for (Map.Entry<String, String> resultValue : resultValues.entrySet()) {
                Button btn = new Button(resultValue.getValue(), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        String curValue = (String) event.getButton().getData();
                        Map<String, String> submitFormProps = newHashMap();
                        submitFormProps.put("result", curValue);
                        lookup(FormService.class).submitTaskFormData(obj.getId(), submitFormProps);
                        // Закрыть окно
                        taskCompleted = true;
                        close();
                    }
                });
                btn.setData(resultValue.getKey());
                btn.setDescription(MessageFormat.format("Завершить задачу с результатом: \"{0}\"", resultValue.getValue()));
                finishToolBar.addComponent(btn);
            }
        } else {
            Button btn = new Button("Завершить", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    lookup(TaskService.class).complete(obj.getId());
                    // Закрыть окно
                    taskCompleted = true;
                    close();
                }
            });
            finishToolBar.addComponent(btn);
        }
        formsContainer.addComponent(new Panel("Завершить задачу", finishToolBar));

        nameField = new EditField("Название", "Название задачи");
        nameField.setRequired(true);
        nameField.setRequiredError("Название не может быть пустым.");
        nameField.setWidth(25, Unit.EM);
        form.addComponent(nameField);

        descriptionField = new TextArea("Описание", "Подробное описание задачи");
        descriptionField.setNullRepresentation("");
        descriptionField.setInputPrompt("Подробное описание задачи...");
        descriptionField.setWidth(25, Unit.EM);
        form.addComponent(descriptionField);

        dueDateField = new PopupDateField("Дата завершения");
        dueDateField.setWidth(25, Unit.EM);
        dueDateField.setInputPrompt("ДД.ММ.ГГГГ ЧЧ:ММ");
        dueDateField.setRequiredError(String.format("Поле '%s' не может быть пустым", "Дата завершения"));
        dueDateField.setDateFormat("dd.MM.yyyy HH:mm");
        dueDateField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ  ЧЧ:ММ");
        dueDateField.setResolution(Resolution.MINUTE);
        form.addComponent(dueDateField);

        ownerField = new EditField("Владелец");
        ownerField.setDescription("Владелец задачи");
        ownerField.setWidth(25, Unit.EM);
        form.addComponent(ownerField);

        assigneeField = new EditField("Ответственный", "Ответственный за выполнение задачи");
        assigneeField.setWidth(25, Unit.EM);
        form.addComponent(assigneeField);

//        statusField = new ComboBox("Состояние");
//        form.addComponent(statusField);
//
//        resultField = new ComboBox("Результат выполнения");
//        form.addComponent(resultField);

        formsContainer.addComponent(form);

        Map<String, Object> processVariables = runtimeService.getVariables(obj.getProcessInstanceId());
        if (processVariables.containsKey("lead")) {
            Lead lead = (Lead) processVariables.get("lead");

            FormLayout leadPanel = new FormLayout();
            BeanItem<Lead> leadBeanItem = new BeanItem<>(lead);
            leadBeanItem.expandProperty("client", "name");
            leadBeanItem.expandProperty("vendor", "name");
            FieldGroup leadFieldGroup = new FieldGroup(leadBeanItem);
            leadFieldGroup.setReadOnly(true);

            EditField contactNameField = new EditField("Клиент");
            contactNameField.setColumns(25);
            leadFieldGroup.bind(contactNameField, "client.name");
            leadPanel.addComponent(contactNameField);

            EditField cellPhoneField = new EditField("Телефон");
            cellPhoneField.setWidth(13, Unit.EM);
            leadFieldGroup.bind(cellPhoneField, "contactPhone");
            leadPanel.addComponent(cellPhoneField);

            EditField contactEmailField = new EditField("E-Mail");
            contactEmailField.setWidth(20, Unit.EM);
            leadFieldGroup.bind(contactEmailField, "contactEmail");
            leadPanel.addComponent(contactEmailField);

            EditField regionField = new EditField("Регион");
            regionField.setDescription("Укажите регион услуги");
            regionField.setWidth(18, Unit.EM);
            leadFieldGroup.bind(regionField, "region");
            leadPanel.addComponent(regionField);

            EditField motorTypeField = new EditField("Тип техники");
            motorTypeField.setWidth(13, Unit.EM);
            leadFieldGroup.bind(motorTypeField, "motorType");
            leadPanel.addComponent(motorTypeField);

            EditField motorBrandField = new EditField("Марка техники");
            motorBrandField.setWidth(13, Unit.EM);
            leadFieldGroup.bind(motorBrandField, "motorBrand");
            leadPanel.addComponent(motorBrandField);

            EditField motorModelField = new EditField("Модель техники", "Введите модель техники");
            motorModelField.setColumns(15);
            leadFieldGroup.bind(motorModelField, "motorModel");
            leadPanel.addComponent(motorModelField);

            EditField motorPriceField = new EditField("Цена техники");
            leadFieldGroup.bind(motorPriceField, "motorPrice");
            leadPanel.addComponent(motorPriceField);

            EditField pointOfSaleField = new EditField("Мотосалон");
            pointOfSaleField.setWidth(25, Unit.EM);
            leadFieldGroup.bind(pointOfSaleField, "vendor.name");
            leadPanel.addComponent(pointOfSaleField);

            TextArea commentField = new TextArea("Комментарий");
            commentField.setColumns(25);
            commentField.setNullRepresentation("");
            leadFieldGroup.bind(commentField, "comment");
            leadPanel.addComponent(commentField);

            formsContainer.addComponent(new Panel("Данные лида", leadPanel));
        }

        if (processVariables.containsKey("sale")) {
            // Возможно следует показывать продажу
        }

        return formsContainer;
    }

    @Override
    public void attach() {
        super.attach();    //To change body of overridden methods use File | Settings | File Templates.
        assigneeField.setReadOnly(true);
        ownerField.setReadOnly(true);
    }

    /*
         * (non-Javadoc)
         *
         * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
         * AbstractExtaObject)
         */
    @Override
    protected void initObject(final Task obj) {
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Task obj) {
        TaskService taskService = lookup(TaskService.class);
        taskService.saveTask(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void checkBeforeSave(final Task obj) {
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }
}
