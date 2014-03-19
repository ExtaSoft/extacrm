package ru.extas.web.bpm;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import ru.extas.web.commons.window.CloseOnlylWindow;
import ru.extas.web.tasks.TaskEditForm;
import ru.extas.web.users.LoginToUserNameConverter;

import java.text.SimpleDateFormat;
import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Панель отображающая текущий статус бизнес процесса.
 * Цель панели - сделать выполнение БП более прозрачным для пользователя.
 *
 * @author Valery Orlov
 *         Date: 29.01.14
 *         Time: 14:01
 * @version $Id: $Id
 */
public class BPStatusForm extends CloseOnlylWindow {

	private final VerticalLayout content;

	/**
	 * <p>Constructor for BPStatusForm.</p>
	 *
	 * @param processId a {@link java.lang.String} object.
	 */
	public BPStatusForm(final String processId) {
		super("Статус выполнения бизнес процесса");

		content = new VerticalLayout();
		content.setSpacing(true);

		// Последняя выполненная задача: название, время начала, время завершения, ответственный.
		content.addComponent(createLastTaskContent(processId));
		// Следующая задача по процессу: название, время начала, время завершения, ответственный.
		content.addComponent(createNextTaskContent(processId));
		// Прочие документы процесса.
		//content.addComponent(createDocsContent());
		// История выполнения БП. Список завершенных задач с указанием времени создания, времени завершения, ответственного.
		content.addComponent(createHistoryContent(processId));

		setContent(content);
	}


	/**
	 * История выполнения БП.
	 * Список завершенных задач с указанием времени создания, времени завершения, ответственного.
	 *
	 * @return созданный контент
	 */
	private Component createHistoryContent(final String processId) {
		// Получение данных
		HistoryService historyService = lookup(HistoryService.class);
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processId)
				.finished()
				.orderByHistoricTaskInstanceEndTime()
				.desc();
		List<HistoricTaskInstance> resultList = query.list();

		Panel panel = new Panel("История выполнения БП");
		if (!resultList.isEmpty()) {
			Container tableContainer = CollectionContainer.fromBeans(resultList);
			Table table = new Table("Завершенные задачи", tableContainer);
			table.setHeight(10, Unit.EM);
			table.setVisibleColumns("name", "startTime", "endTime", "assignee", "description");
			table.setColumnHeader("name", "Имя задачи");
			table.setColumnHeader("startTime", "Старт задачи");
			table.setColumnHeader("endTime", "Завершение задачи");
			table.setColumnHeader("assignee", "Ответственный");
			table.setConverter("assignee", lookup(LoginToUserNameConverter.class));
			table.setColumnHeader("description", "Описание задачи");
			panel.setContent(table);
		} else {
			panel.setContent(new Label("Нет истории задач для бизнес процесса (Первая задача?)"));
		}

		return panel;
	}

	/**
	 * Следующая задача по процессу: название, время начала, время завершения, ответственный.
	 *
	 * @return созданный контент
	 */
	private Component createNextTaskContent(final String processId) {
		// Запрос данных
		final Task task = queryNextTask(processId);

		Panel panel = new Panel("Следующая (активная) задача");
		VerticalLayout container = new VerticalLayout();
		container.setSpacing(true);
		container.addStyleName("bordered-items");

		if (task != null) {
			final Button.ClickListener openTaskFormListener = new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					TaskEditForm form = new TaskEditForm("Просмотр/редактирование задачи", new BeanItem<>(task));
					form.showModal();
				}
			};
			// Открытие формы ввода/редактирования
			Button openBtn = new Button("Нажмите для просмотра/редактирования задачи...", openTaskFormListener);
			openBtn.addStyleName("link");
			container.addComponent(openBtn);

			HorizontalLayout fieldsContainer = new HorizontalLayout();
			fieldsContainer.setSpacing(true);
			// Имя задачи
			Label nameField = new Label(task.getName());
			nameField.setCaption("Имя задачи");
			fieldsContainer.addComponent(nameField);
			// Старт задачи
			final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
			Label startField = new Label(dateFormat.format(task.getCreateTime()));
			startField.setCaption("Старт задачи");
			fieldsContainer.addComponent(startField);
			// Завершение задачи
			Label endField = new Label(dateFormat.format(task.getDueDate()));
			endField.setCaption("Завершение задачи");
			fieldsContainer.addComponent(endField);
			// Ответственный
			Label ownerField = new Label(new ObjectProperty(task.getAssignee()));
			ownerField.setCaption("Ответственный");
			ownerField.setConverter(lookup(LoginToUserNameConverter.class));
			fieldsContainer.addComponent(ownerField);

			container.addComponent(fieldsContainer);
		} else {
			container.addComponent(new Label("Нет следующей задачи."));
		}

		panel.setContent(container);
		return panel;
	}

	private Task queryNextTask(final String processId) {
		TaskService taskService = lookup(TaskService.class);
		TaskQuery query = taskService.createTaskQuery();
		final List<Task> taskList = query.processInstanceId(processId).list();
		return taskList.isEmpty() ? null : taskList.get(0);
	}

	/**
	 * Последняя выполненная задача: название, время начала, время завершения, ответственный.
	 *
	 * @return созданный контент
	 */
	private Component createLastTaskContent(final String processId) {
		// Запрос данных
		HistoricTaskInstance task = queryLastTask(processId);

		Panel panel = new Panel("Последняя завершенная задача");
		HorizontalLayout container = new HorizontalLayout();
		container.setSpacing(true);
		container.addStyleName("bordered-items");

		if (task != null) {
			// Имя задачи
			Label nameField = new Label(task.getName());
			nameField.setCaption("Имя задачи");
			container.addComponent(nameField);
			// Старт задачи
			final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
			Label startField = new Label(dateFormat.format(task.getStartTime()));
			startField.setCaption("Старт задачи");
			container.addComponent(startField);
			// Завершение задачи
			Label endField = new Label(dateFormat.format(task.getEndTime()));
			endField.setCaption("Завершение задачи");
			container.addComponent(endField);
			// Ответственный
			Label ownerField = new Label(new ObjectProperty(task.getAssignee()));
			ownerField.setCaption("Ответственный");
			ownerField.setConverter(lookup(LoginToUserNameConverter.class));
			container.addComponent(ownerField);
		} else {
			container.addComponent(new Label("Нет последней выполненой задачи."));
		}

		container.setReadOnly(true);
		panel.setContent(container);
		return panel;
	}

	private HistoricTaskInstance queryLastTask(final String processId) {
		HistoryService historyService = lookup(HistoryService.class);
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processId)
				.finished()
				.orderByHistoricTaskInstanceEndTime()
				.desc();
		List<HistoricTaskInstance> resultList = query.list();
		return resultList.isEmpty() ? null : resultList.get(0);
	}


}
