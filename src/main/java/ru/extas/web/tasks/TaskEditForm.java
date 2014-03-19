package ru.extas.web.tasks;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
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
import ru.extas.model.Lead;
import ru.extas.model.Person;
import ru.extas.model.Sale;
import ru.extas.model.UserProfile;
import ru.extas.security.UserRole;
import ru.extas.server.UserManagementService;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.PersonField;
import ru.extas.web.lead.LeadField;
import ru.extas.web.sale.SaleField;
import ru.extas.web.users.LoginToUserNameConverter;
import ru.extas.web.users.UserProfileSelect;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static com.google.gwt.thirdparty.guava.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
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

	private UserProfileSelect profileSelect;

	private VerticalLayout formsContainer;
	private final boolean canAssigne;

	/**
	 * <p>Constructor for TaskEditForm.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param obj a {@link com.vaadin.data.util.BeanItem} object.
	 */
	public TaskEditForm(final String caption, final BeanItem<Task> obj) {
		super(caption);

		// Может ли пользователь менять ответственного
		UserManagementService userService = lookup(UserManagementService.class);
		canAssigne = userService.isCurUserHasRole(UserRole.ADMIN) || userService.isCurUserHasRole(UserRole.MANAGER);

		initForm(obj);
	}

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final Task obj) {
	    formsContainer = new VerticalLayout();
	    //formsContainer.setSpacing(true);

	    FormService formService = lookup(FormService.class);

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
	    finishToolBar.setMargin(true);
	    if (result.isPresent()) {
            // Кнопки завершения задачи
            FormType resultType = result.get().getType();
            Map<String, String> resultValues = (Map<String, String>) resultType.getInformation("values");
            for (Map.Entry<String, String> resultValue : resultValues.entrySet()) {
                Button btn = new Button(resultValue.getValue(), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        String curValue = (String) event.getButton().getData();
	                    completeTask(curValue, obj);
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
	                completeTask(null, obj);
                }
            });
            finishToolBar.addComponent(btn);
        }
        formsContainer.addComponent(new Panel("Завершить задачу", finishToolBar));

	    final FormLayout form = new FormLayout();

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
		    profileSelect.addValueChangeListener(new Property.ValueChangeListener() {
			    @Override
			    public void valueChange(final Property.ValueChangeEvent event) {
				    UserProfile profile = (UserProfile) profileSelect.getConvertedValue();
				    if (profile != null) {
					    assigneeField.setValue(profile.getLogin());
				    }
			    }
		    });
		    if (obj.getAssignee() != null) {
			    UserProfile profile = lookup(UserManagementService.class).findUserByLogin(obj.getAssignee());
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

	    final String processId = obj.getProcessInstanceId();
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
			PersonField personField = new PersonField("Клиент");
			personField.setPropertyDataSource(new ObjectProperty(person));
			return personField;
		} else {
			final Label label = new Label("Нет связанного с процессом лида, информация о клиенте недоступна.");
			label.setCaption("Клиент");
			return label;
		}
	}

	private Person queryPerson(final String processId) {
		Lead lead = queryLead(processId);
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
			SaleField saleField = new SaleField("Продажа");
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
			LeadField leadField = new LeadField("Лид");
			leadField.setPropertyDataSource(new ObjectProperty(lead));
			return leadField;
		} else {
			final Label label = new Label("Нет связанного с процессом лида.");
			label.setCaption("Лид");
			return label;
		}
	}

	private Lead queryLead(final String processId) {
		RuntimeService runtimeService = lookup(RuntimeService.class);
		Map<String, Object> processVariables = runtimeService.getVariables(processId);
		Lead lead = null;
		if (processVariables.containsKey("lead")) {
			lead = (Lead) processVariables.get("lead");
		}
		return lead;
	}

	private Sale querySale(final String processId) {
		RuntimeService runtimeService = lookup(RuntimeService.class);
		Map<String, Object> processVariables = runtimeService.getVariables(processId);
		Sale sale = null;
		if (processVariables.containsKey("sale")) {
			sale = (Sale) processVariables.get("sale");
		}
		return sale;
	}


	private void completeTask(final String result, final Task obj) {
		if (result != null) {
			Map<String, String> submitFormProps = newHashMap();
			submitFormProps.put("result", result);
			lookup(FormService.class).submitTaskFormData(obj.getId(), submitFormProps);
		} else {
			lookup(TaskService.class).complete(obj.getId());
		}
		// Закрыть окно
		taskCompleted = true;
		Notification.show("Задача выполнена", Notification.Type.TRAY_NOTIFICATION);
		close();
		// Показать статус выполнения процесса
		BPStatusForm statusForm = new BPStatusForm(obj.getProcessInstanceId());
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

	/*
         * (non-Javadoc)
         *
         * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
         * AbstractExtaObject)
         */
    /** {@inheritDoc} */
    @Override
    protected void initObject(final Task obj) {
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    /** {@inheritDoc} */
    @Override
    protected void saveObject(final Task obj) {
        TaskService taskService = lookup(TaskService.class);
        Task task = taskService.createTaskQuery().taskId(obj.getId()).singleResult();

        task.setName(obj.getName());
        task.setDescription(obj.getDescription());
        task.setDueDate(obj.getDueDate());
        task.setOwner(obj.getOwner());
        task.setAssignee(obj.getAssignee());

        taskService.saveTask(task);
	    Notification.show("Задача сохранена", Notification.Type.TRAY_NOTIFICATION);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
     * AbstractExtaObject)
     */
    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final Task obj) {
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
