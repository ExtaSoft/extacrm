package ru.extas.web.tasks;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.joda.time.LocalDate;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.StringToDateTimeConverter;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>TasksGrid class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 * @version $Id: $Id
 * @since 0.3
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

	/**
	 * <p>Constructor for TasksGrid.</p>
	 *
	 * @param period a {@link ru.extas.web.tasks.TasksGrid.Period} object.
	 */
	public TasksGrid(Period period) {
		super(false);
		this.period = period;
		initialize();
	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new TaskDataDecl();
	}

	/** {@inheritDoc} */
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
		final UserManagementService userService = lookup(UserManagementService.class);
		if (userService.isCurUserHasRole(UserRole.USER)) {
			String currentUser = userService.getCurrentUserLogin();
			query.taskAssignee(currentUser);
		}
		query.orderByTaskPriority().desc().orderByDueDate().asc();
		return query.list();
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

//        actions.add(new UIAction("Новый", "Ввод новой задачи", Fontello.DOC_NEW) {
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

		actions.add(new DefaultAction("Открыть", "Открыть выделенную в списке задачу", Fontello.EDIT_3) {
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

		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса в рамках текущуе задачи", Fontello.SITEMAP) {
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

	/** {@inheritDoc} */
	@Override
	protected void refreshContainer() {
		fillDataContainer((BeanItemContainer<Task>) container);
	}

    @Override
    protected CustomTable.ColumnGenerator createDetailColumnGenerator(final UIAction defAction) {
        return new CustomTable.ColumnGenerator() {

            private StringToDateConverter dtConverter = new StringToDateTimeConverter("EEE, dd MMM, HH:mm");

            @Override
            public Object generateCell(CustomTable source, final Object itemId, Object columnId) {
                Item item = source.getItem(itemId);
                Task task = GridItem.extractBean(item);

                Button titleLink = new Button();
                titleLink.addStyleName("link");
                titleLink.setCaption(task.getName());
                titleLink.setDescription(defAction.getDescription());
                titleLink.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                titleLink.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        defAction.fire(itemId);
                    }
                });

                VerticalLayout panel = new VerticalLayout();
                panel.addComponent(titleLink);

                Label desc = new Label(task.getDescription());
                panel.addComponent(desc);

                Label dueTime = new Label(item.getItemProperty("dueDate"));
                dueTime.setConverter(dtConverter);
                panel.addComponent(dueTime);

                return panel;
            }
        };
    }

}
