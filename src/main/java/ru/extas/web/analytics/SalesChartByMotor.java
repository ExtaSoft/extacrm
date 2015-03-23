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
        final Configuration conf = typeChart.getConfiguration();
        conf.setTitle("Продажи техники по типам");
        conf.setSubTitle("Общее количество проданной техники по типам");

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        return typeChart;
    }

    private void updateTypeData() {
        final Configuration conf = typeChart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Status> saleStatus = root.get(Sale_.status);
        final Path<String> mTypePath = root.get(Sale_.motorType);
        final Expression<Long> mTypeCountEx = cb.count(mTypePath);

        cq.multiselect(mTypePath, mTypeCountEx);
        cq.where(cb.equal(saleStatus, Sale.Status.FINISHED));
        cq.groupBy(mTypePath);

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Единицы техники");

        for (final Tuple t : tq.getResultList()) {
            final String mType = t.get(mTypePath);
            final Long mBrandCount = t.get(mTypeCountEx);
            series.add(new DataSeriesItem(mType, mBrandCount));
        }
        conf.addSeries(series);
        typeChart.drawChart();
    }

    private Chart createBrandChart() {
        brandChart = new Chart(ChartType.PIE);
        brandChart.setWidth("100%");

        // Modify the default configuration a bit
        final Configuration conf = brandChart.getConfiguration();
        conf.setTitle("Продажи техники по брендам");
        conf.setSubTitle("Общее количество проданной техники по брендам");

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        return brandChart;
    }

    private void updateBrandData() {
        final Configuration conf = brandChart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Status> saleStatus = root.get(Sale_.status);
        final Path<String> mBrandPath = root.get(Sale_.motorBrand);
        final Expression<Long> mBrandCountEx = cb.count(mBrandPath);

        cq.multiselect(mBrandPath, mBrandCountEx);
        cq.where(cb.equal(saleStatus, Sale.Status.FINISHED));
        cq.groupBy(mBrandPath);

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Единицы техники");

        for (final Tuple t : tq.getResultList()) {
            final String mBrand = t.get(mBrandPath);
            final Long mBrandCount = t.get(mBrandCountEx);
            series.add(new DataSeriesItem(mBrand, mBrandCount));
        }
        conf.addSeries(series);
        brandChart.drawChart();
    }
}
