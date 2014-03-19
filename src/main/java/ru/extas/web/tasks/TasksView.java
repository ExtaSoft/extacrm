/**
 *
 */
package ru.extas.web.tasks;

import com.vaadin.ui.Component;
import ru.extas.security.ExtaDomain;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует раздел задач
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class TasksView extends AbstractTabView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for TasksView.</p>
     */
    public TasksView() {
        super("Задачи");
    }


    /** {@inheritDoc} */
    @Override
    protected List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("На сегодня", ExtaDomain.TASKS_TODAY) {
            @Override
            public Component createComponent() {
                return new TasksGrid(TasksGrid.Period.TODAY);
            }
        });
        ret.add(new AbstractTabInfo("На неделю", ExtaDomain.TASKS_WEEK) {
            @Override
            public Component createComponent() {
                return new TasksGrid(TasksGrid.Period.WEEK);
            }
        });
        ret.add(new AbstractTabInfo("На месяц", ExtaDomain.TASKS_MONTH) {
            @Override
            public Component createComponent() {
                return new TasksGrid(TasksGrid.Period.MONTH);
            }
        });
        ret.add(new AbstractTabInfo("Все", ExtaDomain.TASKS_ALL) {
            @Override
            public Component createComponent() {
                return new TasksGrid(TasksGrid.Period.ALL);
            }
        });
        return ret;
    }
}
