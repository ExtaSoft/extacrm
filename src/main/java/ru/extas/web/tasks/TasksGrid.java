package ru.extas.web.tasks;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Window;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.joda.time.LocalDate;
import ru.extas.model.Lead;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.*;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 */
public class TasksGrid extends ExtaGrid {
	private static final long serialVersionUID = 4876073256421755574L;
	private final Period period;

	public enum Period {
		TODAY,
		WEEK,
		MONTH,
		ALL;
	}

	public TasksGrid(Period period) {
		super(false);
		this.period = period;
		initialize();
	}

	@Override
	protected GridDataDecl createDataDecl() {
		return new TaskDataDecl();
	}

	@Override
	protected void initTable(Mode mode) {
		super.initTable(mode);
		table.addGeneratedColumn("clientName", new CustomTable.ColumnGenerator() {
			@Override
			public Object generateCell(CustomTable source, Object itemId, Object columnId) {
				String clientName = null;
				Task task = ((BeanItem<Task>) container.getItem(itemId)).getBean();
				RuntimeService runtimeService = lookup(RuntimeService.class);
				Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
				if (processVariables.containsKey("lead")) {
					Lead lead = (Lead) processVariables.get("lead");
					clientName = lead.getContactName();
				}
				return clientName;
			}
		});
		table.setColumnHeader("clientName", "Клиент");
		table.addGeneratedColumn("dealerName", new CustomTable.ColumnGenerator() {
			@Override
			public Object generateCell(CustomTable source, Object itemId, Object columnId) {
				String clientName = null;
				Task task = ((BeanItem<Task>) container.getItem(itemId)).getBean();
				RuntimeService runtimeService = lookup(RuntimeService.class);
				Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
				if (processVariables.containsKey("lead")) {
					Lead lead = (Lead) processVariables.get("lead");
					clientName = lead.getPointOfSale();
				}
				return clientName;
			}
		});
		table.setColumnHeader("dealerName", "Мотосалон");
	}

	@Override
	protected Container createContainer() {
		// Запрос данных
		BeanItemContainer<Task> dataSource = new BeanItemContainer<>(Task.class);
		fillDataContainer(dataSource);
		return dataSource;
	}

	private void fillDataContainer(BeanItemContainer<Task> dataSource) {
		List<Task> tasks = queryForTasksToShow();
		dataSource.removeAllItems();
		dataSource.addAll(tasks);
	}

	private List<Task> queryForTasksToShow() {
		TaskService taskService = lookup(TaskService.class);
		TaskQuery query = taskService.createTaskQuery();
		switch (period) {
			case TODAY:
				query.dueBefore(LocalDate.now().plusDays(1).toDate());
				break;
			case WEEK:
				query.dueBefore(LocalDate.now().plusWeeks(1).toDate());
				break;
			case MONTH:
				query.dueBefore(LocalDate.now().plusMonths(1).toDate());
				break;
			case ALL:
				break;
		}
		query.orderByTaskPriority().desc().orderByDueDate().asc();
		return query.list();
	}

	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

//        actions.add(new UIAction("Новый", "Ввод новой задачи", "icon-doc-new") {
//            @Override
//            public void fire(Object itemId) {
//                TaskService taskService = lookup(TaskService.class);
//                final BeanItem<Task> newObj = new BeanItem<>(taskService.newTask());
//
//                final TaskEditForm editWin = new TaskEditForm("Ввод новой задачи в систему", newObj);
//                editWin.addCloseListener(new Window.CloseListener() {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public void windowClose(final Window.CloseEvent e) {
//                        if (editWin.isSaved()) {
//                            //((JPAContainer) container).refresh();
//                            Notification.show("Задача сохранена", Notification.Type.TRAY_NOTIFICATION);
//                        }
//                    }
//                });
//                editWin.showModal();
//            }
//        });

		actions.add(new DefaultAction("Открыть", "Открыть выделенную в списке задачу", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<Task> curObj = (BeanItem<Task>) table.getItem(itemId);

				final TaskEditForm editWin = new TaskEditForm("Редактирование задачи", curObj);
				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							//((JPAContainer) container).refreshItem(itemId);
						}
						if (editWin.isTaskCompleted()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса в рамках текущуе задачи", "icon-sitemap") {
			@Override
			public void fire(Object itemId) {
				final BeanItem<Task> curObj = (BeanItem<Task>) table.getItem(itemId);
				// Показать статус выполнения процесса
				BPStatusForm statusForm = new BPStatusForm(curObj.getBean().getProcessInstanceId());
				statusForm.showModal();
			}
		});

		return actions;
	}

	@Override
	protected void refreshContainer() {
		fillDataContainer((BeanItemContainer<Task>) container);
	}

}
