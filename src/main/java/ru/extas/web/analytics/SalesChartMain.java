package ru.extas.web.analytics;

import com.google.common.collect.HashBasedTable;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import org.joda.time.DateTime;
import org.vaadin.viritin.fields.CaptionGenerator;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.web.util.ComponentUtil;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 19.03.2015
 *         Time: 16:28
 */
public class SalesChartMain extends AbstractSalesChart {

    private Chart chart;
    private Chart flowChart;
    private Chart cancelReasonsChart;

    @Override
    protected void addChartContent() {
        // Визуализация
        addSalePie();
        addSaleFlow();
        addCancelReasonsChart();
    }

    private void addCancelReasonsChart() {
        cancelReasonsChart = new Chart(ChartType.PIE);
        cancelReasonsChart.setWidth("100%");
        //cancelReasonsChart.setHeight("300px");

        // Modify the default configuration a bit
        final Configuration conf = cancelReasonsChart.getConfiguration();
        conf.setTitle("Отмененные продажи за период");
        conf.setSubTitle("Причины отмены продаж");
        conf.getLegend().setEnabled(false); // Disable legend

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final DataLabels dataLabels = new DataLabels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        addComponent(cancelReasonsChart);
    }

    private void addSaleFlow() {
        flowChart = new Chart(ChartType.AREA);
        flowChart.setSizeFull();

        // Modify the default configuration a bit
        final Configuration conf = flowChart.getConfiguration();
        conf.setTitle("Динамика продаж");
        conf.setSubTitle("Динамика продаж по месяцам");

        final Legend legend = new Legend();
        legend.setShadow(true);
        conf.setLegend(legend);

        final PlotOptionsArea plotOptions = new PlotOptionsArea();
        plotOptions.setStacking(Stacking.NORMAL);
        conf.setPlotOptions(plotOptions);

        addComponent(flowChart);
    }

    private void addSalePie() {
        chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");

        // Modify the default configuration a bit
        final Configuration conf = chart.getConfiguration();
        conf.setTitle("Продажи за период");
        conf.setSubTitle("Общее количество продаж по статусам");
        conf.getLegend().setEnabled(false); // Disable legend

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final DataLabels dataLabels = new DataLabels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        addComponent(chart);
    }

    @Override
    protected void updateChartData() {
        updateSalePieData();
        updateSaleFlowData();
        updateAgreedRejectedData();
    }

    private void updateAgreedRejectedData() {
        final Configuration conf = cancelReasonsChart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.CancelReason> reasonPath = root.get(Sale_.cancelReason);
        final Expression<Long> saleCount = cb.count(reasonPath);

        cq.multiselect(reasonPath, saleCount);
        cq.where(cb.equal(root.get(Sale_.status), Sale.Status.CANCELED));
        cq.groupBy(reasonPath);
//        cq.orderBy(cb.desc(reasonPath));

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Продажи");

        final CaptionGenerator<Sale.CancelReason> captionGenerator = ComponentUtil.getEnumCaptionGenerator(Sale.CancelReason.class);
        for (final Tuple t : tq.getResultList()) {
            final Sale.CancelReason reason = t.get(reasonPath);
            final Long countL = t.get(saleCount);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            item.setName(captionGenerator.getCaption(reason));
            if (reason == Sale.CancelReason.VENDOR_REJECTED) {
                item.setSliced(true);
                item.setSelected(true);
//                item.setColor(SolidColor.LIGHTGREEN);
            }
            series.add(item);
        }
        conf.addSeries(series);
        cancelReasonsChart.drawChart();
    }

    private void updateSaleFlowData() {
        final Configuration conf = flowChart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Status> saleStatus = root.get(Sale_.status);
        final Path<DateTime> createDatePath = root.get(Sale_.createdDate);
        final Expression<Integer> yearEx = cb.function("YEAR", Integer.class, createDatePath);
        final Expression<Integer> monthEx = cb.function("MONTH", Integer.class, createDatePath);
        final Expression<Long> saleCount = cb.count(saleStatus);

        cq.multiselect(saleStatus, yearEx, monthEx, saleCount);
        cq.groupBy(saleStatus, yearEx, monthEx);

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);

        final HashBasedTable<Sale.Status, LocalDate, Long> dataTable = HashBasedTable.create();
        for (final Tuple t : tq.getResultList()) {
            final Sale.Status status = t.get(saleStatus);
            final Integer year = t.get(yearEx);
            final Integer month = t.get(monthEx);
            final Long count = t.get(saleCount);
            dataTable.put(status, LocalDate.of(year, month, 1), count);
        }

        final YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Продажи");
        conf.removeyAxes();
        conf.addyAxis(y);

        final List<LocalDate> periodSet = newArrayList(dataTable.columnKeySet().stream().sorted().iterator());
        final XAxis x = new XAxis();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM YYYY", lookup(Locale.class));
        x.setCategories(newArrayList(periodSet.stream().map(d -> d.format(formatter)).iterator())
                .toArray(new String[periodSet.size()]));
        conf.removexAxes();
        conf.addxAxis(x);

        final ListSeries openedSeries = new ListSeries("Открытые");
        final PlotOptionsArea openedPlot = new PlotOptionsArea();
        openedPlot.setFillColor(new SolidColor(48, 143, 239, .5));
        openedSeries.setPlotOptions(openedPlot);

        final ListSeries finishedSeries = new ListSeries("Завершенные");
        final PlotOptionsArea closedPlot = new PlotOptionsArea();
        closedPlot.setFillColor(new SolidColor(151, 222, 88, .5));
        finishedSeries.setPlotOptions(closedPlot);

        final ListSeries canceledSeries = new ListSeries("Отмененные");
        final PlotOptionsArea canceledPlot = new PlotOptionsArea();
        canceledPlot.setFillColor(new SolidColor(235, 100, 100, .5));
        canceledSeries.setPlotOptions(canceledPlot);

        for (final LocalDate period : periodSet) {
            openedSeries.addData(dataTable.get(Sale.Status.NEW, period));
            finishedSeries.addData(dataTable.get(Sale.Status.FINISHED, period));
            canceledSeries.addData(dataTable.get(Sale.Status.CANCELED, period));
        }
        conf.addSeries(openedSeries);
        conf.addSeries(canceledSeries);
        conf.addSeries(finishedSeries);
        flowChart.drawChart();

    }

    private void updateSalePieData() {
        final Configuration conf = chart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Status> saleStatus = root.get(Sale_.status);
        final Expression<Long> saleCount = cb.count(saleStatus);

        cq.multiselect(saleStatus, saleCount);
        cq.groupBy(saleStatus);
//        cq.orderBy(cb.desc(saleStatus));

        applyFilters(cb, cq, root);

        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Продажи");

        for (final Tuple t : tq.getResultList()) {
            final Sale.Status statusEn = t.get(saleStatus);
            final Long countL = t.get(saleCount);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            if (statusEn == Sale.Status.NEW) {
                item.setName("Открытые");
                item.setColor(new SolidColor("#308FEF"));
            } else if (statusEn == Sale.Status.FINISHED) {
                item.setName("Завершенные");
                item.setColor(new SolidColor("#97DE58"));
                item.setSliced(true);
                item.setSelected(true);
            } else {
                item.setName("Отмененные");
                item.setColor(new SolidColor("#EB6464"));
            }
            series.add(item);
        }
        conf.addSeries(series);
        chart.drawChart();
    }

}
