/**
 *
 */
package ru.extas.web.dashboard;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.ExtaTheme;
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component createContent() {
        logger.info("Creating view content...");

        final VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSizeFull();
        verticalSplitPanel.setSplitPosition(65);

        final TabSheet saleSheet = new TabSheet();
        saleSheet.setSizeFull();
        saleSheet.addTab(new LeadsGrid(Lead.Status.NEW), "Новые Лиды", Fontello.INBOX_ALT);
        saleSheet.addTab(new SalesGrid(ExtaDomain.SALES_OPENED), "Мои Продажи", Fontello.DOLLAR);
        verticalSplitPanel.setFirstComponent(saleSheet);

        final HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        horizontalSplitPanel.setSizeFull();
        horizontalSplitPanel.setSplitPosition(50);

        final TabSheet sheet = new TabSheet();
        sheet.setSizeFull();

        sheet.addTab(getChart(), "Мои Продажи", Fontello.CHART_LINE);
        sheet.addTab(new Label("Рейтинг"), "Рейтинг", Fontello.CHART_BAR_1);
        horizontalSplitPanel.setFirstComponent(sheet);

        final TasksGrid tasksGrid = new TasksGrid(TasksGrid.Period.TODAY);
        tasksGrid.addAttachListener(e -> {
            tasksGrid.setToolbarVisible(false);
            tasksGrid.setMode(ExtaGrid.Mode.DETAIL_LIST);
        });
        final Panel taskPanel = new Panel("Задачи");
        taskPanel.setSizeFull();
        taskPanel.setContent(tasksGrid);
        horizontalSplitPanel.setSecondComponent(taskPanel);
        verticalSplitPanel.setSecondComponent(horizontalSplitPanel);

        return verticalSplitPanel;
    }

    protected Component getChart() {
        final Chart chart = new Chart();
        chart.setSizeFull();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.LINE);
//        configuration.getChart().setMarginRight(130);
//        configuration.getChart().setMarginBottom(25);

        configuration.getTitle().setText("Подажи страховок и кредитов");
//        configuration.getSubTitle().setText("Source: WorldClimate.com");

        configuration.getxAxis().setCategories("Янв", "Фев.", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Нов", "Дек");

        final Axis yAxis = configuration.getyAxis();
//        yAxis.setMin(-5d);
        yAxis.setTitle(new Title("Количество (шт.)"));
        yAxis.getTitle().setVerticalAlign(VerticalAlign.HIGH);

        configuration
                .getTooltip()
                .setFormatter("''+ this.series.name +' ' + this.x +': '+ this.y +'шт.'");
        final PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setDataLabels(new Labels(true));
        configuration.setPlotOptions(plotOptions);

        final Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setHorizontalAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
//        legend.setX(-10d);
//        legend.setY(100d);
//        legend.setBorderWidth(0);

        ListSeries ls = new ListSeries();
        ls.setName("Кредиты");
        ls.setData(70, 69, 95, 145, 182, 215, 252, 265, 233, 183, 139, 96);
        configuration.addSeries(ls);
        ls = new ListSeries();
        ls.setName("Страховки");
        ls.setData(39, 42, 57, 85, 119, 152, 170, 166, 142, 103, 66, 48);
        configuration.addSeries(ls);

        chart.drawChart(configuration);
        return chart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component createTitle() {
        final Component title = new Label("Рабочий стол");
        title.setSizeUndefined();
        title.addStyleName(ExtaTheme.VIEW_TITLE);
        return title;
    }

}
