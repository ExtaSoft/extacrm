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
 *         Time: 16:34
 */
public class SalesChartByMotor extends AbstractSalesChart {


    private Chart typeChart;
    private Chart brandChart;

    @Override
    protected void addChartContent() {
        addComponent(createTypeChart());
        addComponent(createBrandChart());
    }

    @Override
    protected void updateChartData() {
        updateTypeData();
        updateBrandData();
    }

    private Chart createTypeChart() {
        typeChart = new Chart(ChartType.PIE);
        typeChart.setWidth("100%");

        // Modify the default configuration a bit
        Configuration conf = typeChart.getConfiguration();
        conf.setTitle("Продажи техники по типам");
        conf.setSubTitle("Общее количество проданной техники по типам");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        return typeChart;
    }

    private void updateTypeData() {
        Configuration conf = typeChart.getConfiguration();
        conf.setSeries(newArrayList());

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

        applyFilters(cb, cq, root);
        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Единицы техники");

        for (Tuple t : tq.getResultList()) {
            String mType = t.get(mTypePath);
            Long mBrandCount = t.get(mTypeCountEx);
            series.add(new DataSeriesItem(mType, mBrandCount));
        }
        conf.addSeries(series);
        typeChart.drawChart();
    }

    private Chart createBrandChart() {
        brandChart = new Chart(ChartType.PIE);
        brandChart.setWidth("100%");

        // Modify the default configuration a bit
        Configuration conf = brandChart.getConfiguration();
        conf.setTitle("Продажи техники по брендам");
        conf.setSubTitle("Общее количество проданной техники по брендам");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        return brandChart;
    }

    private void updateBrandData() {
        Configuration conf = brandChart.getConfiguration();
        conf.setSeries(newArrayList());

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

        applyFilters(cb, cq, root);
        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Единицы техники");

        for (Tuple t : tq.getResultList()) {
            String mBrand = t.get(mBrandPath);
            Long mBrandCount = t.get(mBrandCountEx);
            series.add(new DataSeriesItem(mBrand, mBrandCount));
        }
        conf.addSeries(series);
        brandChart.drawChart();
    }
}
