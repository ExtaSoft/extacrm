package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 19.03.2015
 *         Time: 16:28
 */
public class SalesChartMain extends VerticalLayout {

    /**
     * Constructs an empty VerticalLayout.
     */
    public SalesChartMain() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");
        //chart.setHeight("300px");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Продажи за период");
        conf.setSubTitle("Общее количество продаж по статусам");
        conf.getLegend().setEnabled(true); // Disable legend


        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        // The data
        EntityManager em = lookup(EntityManager.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Status> saleStatus = root.get(Sale_.status);
        Expression<Long> saleCount = cb.count(saleStatus);

        cq.multiselect(saleStatus, saleCount);
        cq.groupBy(saleStatus);
//        cq.orderBy(cb.desc(saleStatus));

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Продажи");

        for (Tuple t : tq.getResultList()) {
            final Sale.Status statusEn = t.get(saleStatus);
            final Long countL = t.get(saleCount);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            if (statusEn == Sale.Status.NEW) {
                item.setName("Открытые");
//                item.setColor(SolidColor.LIGHTBLUE);
            } else if (statusEn == Sale.Status.FINISHED) {
                item.setName("Завершенные");
                item.setSliced(true);
                item.setSelected(true);
//                item.setColor(SolidColor.LIGHTGREEN);
            } else {
                item.setName("Отмененные");
//                item.setColor(SolidColor.ORANGERED);
            }
            series.add(item);
        }
        conf.addSeries(series);

        addComponent(chart);
    }
}
