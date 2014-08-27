/**
 *
 */
package ru.extas.web.tasks;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует раздел задач
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class TasksView extends SubdomainView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for TasksView.</p>
     */
    public TasksView() {
        super("Задачи");
    }


    /** {@inheritDoc} */
    @Override
    protected List<SubdomainInfo> getSubdomainInfo() {
        final ArrayList<SubdomainInfo> ret = newArrayList();
        ret.add(new AbstractSubdomainInfo("На сегодня", ExtaDomain.TASKS_TODAY) {
            @Override
            public ExtaGrid createGrid() {
                return new TasksGrid(TasksGrid.Period.TODAY);
            }
        });
        ret.add(new AbstractSubdomainInfo("На неделю", ExtaDomain.TASKS_WEEK) {
            @Override
            public ExtaGrid createGrid() {
                return new TasksGrid(TasksGrid.Period.WEEK);
            }
        });
        ret.add(new AbstractSubdomainInfo("На месяц", ExtaDomain.TASKS_MONTH) {
            @Override
            public ExtaGrid createGrid() {
                return new TasksGrid(TasksGrid.Period.MONTH);
            }
        });
        ret.add(new AbstractSubdomainInfo("Все", ExtaDomain.TASKS_ALL) {
            @Override
            public ExtaGrid createGrid() {
                return new TasksGrid(TasksGrid.Period.ALL);
            }
        });
        return ret;
    }
}
