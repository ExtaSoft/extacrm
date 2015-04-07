package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.vaadin.viritin.fields.CaptionGenerator;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.web.util.ComponentUtil;

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
        conf.setSubTitle("Продажи по причинам отмены");
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
        final Path<Sale.CancelReason> reasonPath = root.get(Sale_.cancelReason);
        final Expression<Long> saleCount = cb.count(reasonPath);

        cq.multiselect(reasonPath, saleCount);
        cq.where(cb.equal(root.get(Sale_.status), Sale.Status.CANCELED));
        cq.groupBy(reasonPath);
//        cq.orderBy(cb.desc(reasonPath));

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Продажи");

        CaptionGenerator<Sale.CancelReason> captionGenerator = ComponentUtil.getEnumCaptionGenerator(Sale.CancelReason.class);
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
        chart.drawChart();
    }

    @Override
    protected void updateChartData() {
        updateAgreedRejectedData();
    }
}
