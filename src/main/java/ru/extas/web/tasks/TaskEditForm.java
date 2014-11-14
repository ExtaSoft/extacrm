package ru.extas.web.tasks;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import ru.extas.model.contacts.Person;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.model.security.UserProfile;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.contacts.person.PersonField;
import ru.extas.web.lead.LeadField;
import ru.extas.web.sale.SaleField;
import ru.extas.web.users.LoginToUserNameConverter;
import ru.extas.web.users.UserProfileSelect;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.gwt.thirdparty.guava.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class TaskEditForm extends ExtaEditForm<Task> {

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

    private UserProfileSelect profileSelect;

    private VerticalLayout formsContainer;
    private final boolean canAssigne;

    public TaskEditForm(final Task task) {
        super(isNullOrEmpty(task.getId()) ?
                "Ввод новой задачи в систему" :
                "Редактирование задачи", task);
        // Может ли пользователь менять ответственного
        final UserManagementService userService = lookup(UserManagementService.class);
        canAssigne = userService.isCurUserHasRole(UserRole.ADMIN)/* || userService.isCurUserHasRole(UserRole.MANAGER)*/;
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields() {
        formsContainer = new VerticalLayout();
        //formsContainer.setSpacing(true);

        final FormService formService = lookup(FormService.class);

        final TaskFormData taskData = formService.getTaskFormData(getEntity().getId());
        final List<FormProperty> formProps = taskData.getFormProperties();
        final Optional<FormProperty> result = Iterators.tryFind(formProps.iterator(), input -> input.getId().equals("result"));
        final HorizontalLayout finishToolBar = new HorizontalLayout();
        finishToolBar.setSpacing(true);
        finishToolBar.setMargin(true);
        if (result.isPresent()) {
            // Кнопки завершения задачи
            final FormType resultType = result.get().getType();
            final Map<String, String> resultValues = (Map<String, String>) resultType.getInformation("values");
            for (final Map.Entry<String, String> resultValue : resultValues.entrySet()) {
                final Button btn = new Button(resultValue.getValue(), event -> {
                    final String curValue = (String) event.getButton().getData();
                    completeTask(curValue, getEntity());
                });
                btn.setData(resultValue.getKey());
                btn.setDescription(MessageFormat.format("Завершить задачу с результатом: \"{0}\"", resultValue.getValue()));
                finishToolBar.addComponent(btn);
            }
        } else {
            final Button btn = new Button("Завершить", event -> completeTask(null, getEntity()));
            finishToolBar.addComponent(btn);
        }
        formsContainer.addComponent(new Panel("Завершить задачу", finishToolBar));

        final FormLayout form = new ExtaFormLayout();

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

        // Поле для биндинга данных
        assigneeField = new EditField("Ответственный", "Ответственный за выполнение задачи");
        assigneeField.setWidth(25, Unit.EM);
        form.addComponent(assigneeField);

        if (canAssigne) {
            assigneeField.setVisible(false);
            // Поле для выбора пользователя
            profileSelect = new UserProfileSelect("Ответственный", "Ответственный за выполнение задачи");
            profileSelect.setWidth(25, Unit.EM);
            profileSelect.addValueChangeListener(event -> {
                final UserProfile profile = (UserProfile) profileSelect.getConvertedValue();
                if (profile != null) {
                    assigneeField.setValue(profile.getLogin());
                }
            });
            if (getEntity().getAssignee() != null) {
                final UserProfile profile = lookup(UserManagementService.class).findUserByLogin(getEntity().getAssignee());
                profileSelect.setConvertedValue(profile);
            }
            form.addComponent(profileSelect);
        } else {
            assigneeField.setVisible(true);
            assigneeField.setConverter(lookup(LoginToUserNameConverter.class));
        }

        ownerField = new EditField("Владелец", "Владелец (автор) задачи");
        ownerField.setWidth(25, Unit.EM);
        ownerField.setConverter(lookup(LoginToUserNameConverter.class));
        form.addComponent(ownerField);

        final String processId = getEntity().getProcessInstanceId();
        // Клиент (берется из лида): имя, телефон, почта.
        form.addComponent(createClientContent(processId));
        // Лид процесса: имя клиента, тип лида (кредит, страховка, рассрочка).
        form.addComponent(createLeadContent(processId));
        // Продажа процесса: наименование продажи. Возможно нужно сделать возможносьть создать продажу, если она еще не создана в рамкех БП.
        form.addComponent(createSaleContent(processId));

        formsContainer.addComponent(form);

        return formsContainer;
    }

    private Component createClientContent(final String processId) {
        // Запрос данных
        final Person person = queryPerson(processId);

        if (person != null) {
            final PersonField personField = new PersonField("Клиент");
            personField.setPropertyDataSource(new ObjectProperty(person));
            return personField;
        } else {
            final Label label = new Label("Нет связанного с процессом лида, информация о клиенте недоступна.");
            label.setCaption("Клиент");
            return label;
        }
    }

    private Person queryPerson(final String processId) {
        final Lead lead = queryLead(processId);
        return lead != null ? lead.getClient() : null;
    }

    /**
     * Продажа процесса: наименование продажи.
     * Возможно нужно сделать возможносьть создать продажу, если она еще не создана в рамкех БП.
     *
     * @return созданный контент
     */
    private Component createSaleContent(final String processId) {

        // Запрос данных
        final Sale sale = querySale(processId);

        if (sale != null) {
            final SaleField saleField = new SaleField("Продажа");
            saleField.setPropertyDataSource(new ObjectProperty(sale));
            return saleField;
        } else {
            final Label label = new Label("Нет связанной с процессом продажи.");
            label.setCaption("Продажа");
            return label;
        }
    }

    /**
     * Лид процесса: имя клиента, тип лида (кредит, страховка, рассрочка).
     *
     * @return созданный контент
     */
    private Component createLeadContent(final String processId) {
        // Запрос данных
        final Lead lead = queryLead(processId);

        if (lead != null) {
            final LeadField leadField = new LeadField("Лид");
            leadField.setPropertyDataSource(new ObjectProperty(lead));
            return leadField;
        } else {
            final Label label = new Label("Нет связанного с процессом лида.");
            label.setCaption("Лид");
            return label;
        }
    }

    private Lead queryLead(final String processId) {
        final RuntimeService runtimeService = lookup(RuntimeService.class);
        final Map<String, Object> processVariables = runtimeService.getVariables(processId);
        Lead lead = null;
        if (processVariables.containsKey("lead")) {
            lead = (Lead) processVariables.get("lead");
        }
        return lead;
    }

    private Sale querySale(final String processId) {
        final RuntimeService runtimeService = lookup(RuntimeService.class);
        final Map<String, Object> processVariables = runtimeService.getVariables(processId);
        Sale sale = null;
        if (processVariables.containsKey("sale")) {
            sale = (Sale) processVariables.get("sale");
        }
        return sale;
    }


    private void completeTask(final String result, final Task task) {
        if (result != null) {
            final Map<String, String> submitFormProps = newHashMap();
            submitFormProps.put("result", result);
            lookup(FormService.class).submitTaskFormData(task.getId(), submitFormProps);
        } else {
            lookup(TaskService.class).complete(task.getId());
        }
        // Закрыть окно
        taskCompleted = true;
        NotificationUtil.showSuccess("Задача выполнена");
        closeForm();
        // Показать статус выполнения процесса
        final BPStatusForm statusForm = new BPStatusForm(task.getProcessInstanceId());
        statusForm.showModal();
    }

    /** {@inheritDoc} */
    @Override
    public void attach() {
        super.attach();
        ownerField.setReadOnly(true);
        if (!canAssigne)
            assigneeField.setReadOnly(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void initEntity(final Task task) {
    }

    /** {@inheritDoc} */
    @Override
    protected Task saveEntity(final Task savingTask) {
        final TaskService taskService = lookup(TaskService.class);
        final Task task = taskService.createTaskQuery().taskId(savingTask.getId()).singleResult();

        task.setName(savingTask.getName());
        task.setDescription(savingTask.getDescription());
        task.setDueDate(savingTask.getDueDate());
        task.setOwner(savingTask.getOwner());
        task.setAssignee(savingTask.getAssignee());

        taskService.saveTask(task);
        NotificationUtil.showSuccess("Задача сохранена");
        return task;
    }

    /**
     * <p>isTaskCompleted.</p>
     *
     * @return a boolean.
     */
    public boolean isTaskCompleted() {
        return taskCompleted;
    }
}
