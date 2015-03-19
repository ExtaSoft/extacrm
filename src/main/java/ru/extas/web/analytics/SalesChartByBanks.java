package ru.extas.web.analytics;

import com.google.common.collect.HashBasedTable;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Company_;
import ru.extas.model.sale.*;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Set;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 19.03.2015
 *         Time: 16:32
 */
public class SalesChartByBanks extends VerticalLayout {

    /**
     * Constructs an empty VerticalLayout.
     */
    public SalesChartByBanks() {

        addComponent(createAllRequestChart());
        addComponent(createSalesChartByBankStatus());
    }

    private Chart createSalesChartByBankStatus() {
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setSizeFull();

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Заявки по банкам");
        conf.setSubTitle("Общее количество заявок по статусам рассмотрения");

        Legend legend = new Legend();
//        legend.setLayout(LayoutDirection.VERTICAL);
//        legend.setBackgroundColor("#FFFFFF");
//        legend.setHorizontalAlign(HorizontalAlign.LEFT);
//        legend.setVerticalAlign(VerticalAlign.TOP);
//        legend.setX(100);
//        legend.setY(70);
//        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        // The data
        final ListSeries listSeries = new ListSeries();
        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final ListJoin<Sale, ProductInSale> productInSaleJoin = root.join(Sale_.productInSales);
        final Path<ProductInSale.State> productInSaleState = productInSaleJoin.get(ProductInSale_.state);
        final Join<ProductInSale, Product> saleProductJoin = productInSaleJoin.join(ProductInSale_.product);
        final Join<Product, Company> bank = saleProductJoin.join(Product_.vendor);
        final Path<String> bankName = bank.get(Company_.name);
        final Expression<Long> bankCount = cb.count(bank);

        cq.multiselect(bankName, bankCount, productInSaleState);
        cq.where(cb.notEqual(saleProductJoin.type(), ProdInsurance.class));
        cq.groupBy(bank.get(Company_.id), productInSaleState);

        final TypedQuery<Tuple> tq = em.createQuery(cq);

        final HashBasedTable<ProductInSale.State, String, Long> dataTable = HashBasedTable.create();
        for (Tuple t : tq.getResultList()) {
            final String name = t.get(bankName);
            final ProductInSale.State stat = t.get(productInSaleState);
            final Long count = t.get(bankCount);
            dataTable.put(stat, name, count);
        }
        final Set<String> bankSet = dataTable.columnKeySet();
        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Заявки");
        conf.addyAxis(y);
        XAxis x = new XAxis();
        x.setCategories(bankSet.toArray(new String[bankSet.size()]));
        conf.addxAxis(x);
        ListSeries openedSeries = new ListSeries("На рассмотрении");
        ListSeries closedSeries = new ListSeries("Одобренные");
        ListSeries rejectedSeries = new ListSeries("Отклоненные");
        for (String bankItem : bankSet) {
            openedSeries.addData(dataTable.get(ProductInSale.State.IN_PROGRESS, bankItem));
            closedSeries.addData(dataTable.get(ProductInSale.State.AGREED, bankItem));
            rejectedSeries.addData(dataTable.get(ProductInSale.State.REJECTED, bankItem));
        }
        conf.addSeries(openedSeries);
        conf.addSeries(closedSeries);
        conf.addSeries(rejectedSeries);
        return chart;
    }

    private Chart createAllRequestChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setSizeFull();

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Заявки по банкам");
        conf.setSubTitle("Общее количество заявок");
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
        final Join<ProductInSale, Product> saleProductJoin = root.join(Sale_.productInSales).join(ProductInSale_.product);
        Join<Product, Company> bank = saleProductJoin.join(Product_.vendor);
        Path<String> bankName = bank.get(Company_.name);
        Expression<Long> bankCount = cb.count(bank);

        cq.multiselect(bankName, bankCount);
        cq.where(cb.notEqual(saleProductJoin.type(), ProdInsurance.class));
        cq.groupBy(bank);

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Заявки за период");

        for (Tuple t : tq.getResultList()) {
            final String name = t.get(bankName);
            final Long count = t.get(bankCount);
            final DataSeriesItem item = new DataSeriesItem(name, count);
            series.add(item);
        }
        conf.addSeries(series);
        return chart;
    }

}
