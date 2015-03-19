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
 *         Time: 16:34
 */
public class SalesChartByMotor extends VerticalLayout {

    /**
     * Constructs an empty VerticalLayout.
     */
    public SalesChartByMotor() {

        addComponent(createTypeChart());
        addComponent(createBrandChart());
    }

    private Chart createTypeChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Продажи техники по типам");
        conf.setSubTitle("Общее количество проданной техники по типам");

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
        Expression<Long> mTypeCountEx = cb.count(mTypePath);

        cq.multiselect(mTypePath, mTypeCountEx);
        cq.where(cb.equal(saleStatus, Sale.Status.FINISHED));
        cq.groupBy(mTypePath);

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Единицы техники");

        for (Tuple t : tq.getResultList()) {
            String mType = t.get(mTypePath);
            Long mBrandCount = t.get(mTypeCountEx);
            series.add(new DataSeriesItem(mType, mBrandCount));
        }
        conf.addSeries(series);
        return chart;
    }

    private Chart createBrandChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Продажи техники по брендам");
        conf.setSubTitle("Общее количество проданной техники по брендам");

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
        Path<String> mBrandPath = root.get(Sale_.motorBrand);
        Expression<Long> mBrandCountEx = cb.count(mBrandPath);

        cq.multiselect(mBrandPath, mBrandCountEx);
        cq.where(cb.equal(saleStatus, Sale.Status.FINISHED));
        cq.groupBy(mBrandPath);

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Единицы техники");

        for (Tuple t : tq.getResultList()) {
            String mBrand = t.get(mBrandPath);
            Long mBrandCount = t.get(mBrandCountEx);
            series.add(new DataSeriesItem(mBrand, mBrandCount));
        }
        conf.addSeries(series);
        return chart;
    }
}
