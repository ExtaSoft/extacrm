package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.Component;
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
 *         Time: 16:34
 */
public class SalesChartByMotor extends VerticalLayout {

    /**
     * Constructs an empty VerticalLayout.
     */
    public SalesChartByMotor() {
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
        Path<String> mTypePath = root.get(Sale_.motorType);
        Path<String> mBrandPath = root.get(Sale_.motorBrand);
        Expression<Long> mBrandCountEx = cb.count(mBrandPath);

        cq.multiselect(mTypePath, mBrandPath, mBrandCountEx);
        cq.where(cb.equal(saleStatus, Sale.Status.FINISHED));
        cq.groupBy(mTypePath, mBrandPath);

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Продажи");

        for (Tuple t : tq.getResultList()) {
            String mType = t.get(mTypePath);
            String mBrand = t.get(mBrandPath);
            Long mBrandCount = t.get(mBrandCountEx);
//            final DataSeriesItem item = new DataSeriesItem();
//            item.setY(countL);
//            series.add(item);
        }
        conf.addSeries(series);

        addComponent(chart);
    }
}
