package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.sale.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

public class SalesChartByProduct extends AbstractSalesChart {

    private Chart chart;

    @Override
    protected void addChartContent() {
        chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");
        //chart.setHeight("300px");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Продукты в успешных продажах");
        conf.setSubTitle("Распределение продуктов в успешных продажах");


        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        addComponent(chart);
    }

    @Override
    protected void updateChartData() {
        Configuration conf = chart.getConfiguration();
        conf.setSeries(newArrayList());

        EntityManager em = lookup(EntityManager.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Sale> root = cq.from(Sale.class);
        final Path<Sale.Result> resultPath = root.get(Sale_.result);
        final ListJoin<Sale, ProductInSale> productInSaleJoin = root.join(Sale_.productInSales);
        final Path<ProductInSale.State> productInSaleState = productInSaleJoin.get(ProductInSale_.state);
        final Join<ProductInSale, Product> saleProductJoin = productInSaleJoin.join(ProductInSale_.product);
        Expression<Long> saleCount = cb.count(resultPath);

        final Expression<Class<? extends Product>> proTypeExpr = saleProductJoin.type();
        cq.multiselect(proTypeExpr, saleCount);
        cq.where(cb.and(cb.equal(resultPath, Sale.Result.SUCCESSFUL),
                cb.equal(productInSaleState, ProductInSale.State.AGREED),
                cb.notEqual(proTypeExpr, ProdInsurance.class)));
        cq.groupBy(proTypeExpr);

        applyFilters(cb, cq, root);
        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Продажи");

        for (Tuple t : tq.getResultList()) {
            Class<? extends Product> prodType = t.get(proTypeExpr);
            final Long countL = t.get(saleCount);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            if (prodType == ProdCredit.class) {
                item.setName("Кредит");
                item.setSliced(true);
                item.setSelected(true);
            } else if (prodType == ProdInstallments.class) {
                item.setName("Рассрочка");
            } // TODO: Добавить лизинг
            series.add(item);
        }
        conf.addSeries(series);
        chart.drawChart();
    }

}
