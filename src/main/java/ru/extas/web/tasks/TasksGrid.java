package ru.extas.web.tasks;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.joda.time.LocalDate;
import ru.extas.model.security.UserRole;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaBeanContainer;
import ru.extas.web.commons.converters.StringToDateTimeConverter;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
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
public class TasksGrid extends ExtaGrid<Task> {
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
	public TasksGrid(final Period period) {
		super(Task.class);
		this.period = period;
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
		final ExtaBeanContainer<Task> dataSource = new ExtaBeanContainer<>(Task.class);
		fillDataContainer(dataSource);
		return dataSource;
	}

	private void fillDataContainer(final ExtaBeanContainer<Task> dataSource) {
		final List<Task> tasks = queryForTasksToShow();
		dataSource.removeAllItems();
		dataSource.addAll(tasks);
	}

	private List<Task> queryForTasksToShow() {
		final TaskService taskService = lookup(TaskService.class);
		final TaskQuery query = taskService.createTaskQuery();
		switch (period) {
			case TODAY:
				query.taskDueBefore(LocalDate.now().plusDays(1).toDate());
				break;
			case WEEK:
				query.taskDueBefore(LocalDate.now().plusWeeks(1).toDate());
				break;
			case MONTH:
				query.taskDueBefore(LocalDate.now().plusMonths(1).toDate());
				break;
			case ALL:
				break;
		}
		final UserManagementService userService = lookup(UserManagementService.class);
		if (userService.isCurUserHasRole(UserRole.USER)) {
			final String currentUser = userService.getCurrentUserLogin();
			query.taskAssignee(currentUser);
		}
		query.orderByTaskPriority().desc().orderByTaskDueDate().asc();
		return query.list();
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

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

		actions.add(new EditObjectAction("Открыть", "Открыть выделенную в списке задачу"));

		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса в рамках текущуе задачи", Fontello.SITEMAP) {
			@Override
			public void fire(final Set itemIds) {
				final BeanItem<Task> curObj = (BeanItem<Task>) getFirstItem(itemIds);
				// Показать статус выполнения процесса
				final BPStatusForm statusForm = new BPStatusForm(curObj.getBean().getProcessInstanceId());
				statusForm.showModal();
			}
		});

		return actions;
	}


	/** {@inheritDoc} */
	@Override
	protected void refreshContainer() {
		fillDataContainer((ExtaBeanContainer<Task>) container);
	}

    @Override
    public ExtaEditForm<Task> createEditForm(final Task task, final boolean isInsert) {
        return new TaskEditForm(task);
    }

    @Override
    protected CustomTable.ColumnGenerator createDetailColumnGenerator(final UIAction defAction) {
        return new CustomTable.ColumnGenerator() {

            private StringToDateTimeConverter dtConverter;
            {
                dtConverter = lookup(StringToDateTimeConverter.class);
                dtConverter.setPattern("EEE, dd MMM, HH:mm");
            }

            @Override
            public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
                final Item item = getItem(itemId);
                final Task task = GridItem.extractBean(item);

                final Button titleLink = new Button();
                titleLink.addStyleName(ExtaTheme.BUTTON_LINK);
                titleLink.setCaption(task.getName());
                titleLink.setDescription(defAction.getDescription());
                titleLink.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                titleLink.addClickListener(event -> defAction.fire(newHashSet(itemId)));

                final VerticalLayout panel = new VerticalLayout();
                panel.addComponent(titleLink);

                final Label desc = new Label(task.getDescription());
                panel.addComponent(desc);

                final Label dueTime = new Label(item.getItemProperty("dueDate"));
                dueTime.setConverter(dtConverter);
                panel.addComponent(dueTime);

                return panel;
            }
        };
    }

}
