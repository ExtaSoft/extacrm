/**
 *
 */
package ru.extas.web.dashboard;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.Fontello;
import ru.extas.web.lead.LeadsGrid;
import ru.extas.web.sale.SalesGrid;
import ru.extas.web.tasks.TasksGrid;

/**
 * Реализует домашний экран CRM
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class HomeView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;
    private final static Logger logger = LoggerFactory.getLogger(HomeView.class);

    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.info("Creating view content...");

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSizeFull();
        verticalSplitPanel.setSplitPosition(70);

        TabSheet sheet = new TabSheet();
        sheet.setSizeFull();
        sheet.addStyleName("framed");
        sheet.addTab(new LeadsGrid(Lead.Status.NEW), "Лиды", Fontello.INBOX_ALT);
        sheet.addTab(new SalesGrid(ExtaDomain.SALES_OPENED), "Продажи", Fontello.DOLLAR);
        verticalSplitPanel.setFirstComponent(sheet);

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.setSizeFull();
        horizontalSplitPanel.setSplitPosition(50);
        horizontalSplitPanel.setFirstComponent(new Label("График"));
        horizontalSplitPanel.setSecondComponent(new TasksGrid(TasksGrid.Period.TODAY));
        verticalSplitPanel.setSecondComponent(horizontalSplitPanel);

        return verticalSplitPanel;
    }

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Рабочий стол");
        title.setSizeUndefined();
        title.addStyleName("view-title");
        return title;
    }

}
