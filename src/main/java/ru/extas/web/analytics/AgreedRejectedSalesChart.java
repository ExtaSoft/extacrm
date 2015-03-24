package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 19.03.2015
 *         Time: 16:30
 */
public class AgreedRejectedSalesChart extends AbstractSalesChart {

    private Chart chart;

    @Override
    protected void addChartContent() {
        chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");
        //chart.setHeight("300px");

        // Modify the default configuration a bit
        final Configuration conf = chart.getConfiguration();
        conf.setTitle("Отмененные продажи за период");
        conf.setSubTitle("Отмены по инициторам");
        conf.getLegend().setEnabled(true); // Disable legend

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        addComponent(chart);
    }

    private void updateAgreedRejectedData() {
        final Configuration conf = chart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Result> resultPath = root.get(Sale_.result);
        final Expression<Long> saleCount = cb.count(resultPath);

        cq.multiselect(resultPath, saleCount);
        cq.where(resultPath.in(Sale.Result.CLIENT_REJECTED, Sale.Result.VENDOR_REJECTED));
        cq.groupBy(resultPath);
//        cq.orderBy(cb.desc(resultPath));

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Продажи");

        for (final Tuple t : tq.getResultList()) {
            final Sale.Result result = t.get(resultPath);
            final Long countL = t.get(saleCount);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            if (result == Sale.Result.CLIENT_REJECTED) {
                item.setName("Отказ клиента");
//                item.setColor(SolidColor.LIGHTBLUE);
            } else if (result == Sale.Result.VENDOR_REJECTED) {
                item.setName("Отказ контрагента (банка, лизинговой компании и т.п.)");
                item.setSliced(true);
                item.setSelected(true);
//                item.setColor(SolidColor.LIGHTGREEN);
            }
            series.add(item);
        }
        conf.addSeries(series);
        chart.drawChart();
    }

    @Override
    protected void updateChartData() {
        updateAgreedRejectedData();
    }
}
