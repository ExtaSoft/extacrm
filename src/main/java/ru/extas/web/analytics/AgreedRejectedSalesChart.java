package ru.extas.web.analytics;

import com.google.common.collect.HashBasedTable;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.vaadin.viritin.fields.CaptionGenerator;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Company_;
import ru.extas.model.sale.*;
import ru.extas.web.util.ComponentUtil;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 19.03.2015
 *         Time: 16:30
 */
public class AgreedRejectedSalesChart extends AbstractSalesChart {

    private Chart cancelReasonsChart;
    private Chart chartByBankStatus;
    private Chart allReqChart;

    @Override
    protected void addChartContent() {
        addComponent(createAllRequestChart());
        addComponent(createSalesChartByBankStatus());
        addCancelReasonsChart();
    }

    private void addCancelReasonsChart() {
        cancelReasonsChart = new Chart(ChartType.PIE);
        cancelReasonsChart.setWidth("100%");
        //cancelReasonsChart.setHeight("300px");

        // Modify the default configuration a bit
        final Configuration conf = cancelReasonsChart.getConfiguration();
        conf.setTitle("Причины отмены продаж");
        conf.setSubTitle("Отмененные продажи за период");
        conf.getLegend().setEnabled(false); // Disable legend

        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        addComponent(cancelReasonsChart);
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
        cancelReasonsChart.drawChart();
    }

    @Override
    protected void updateChartData() {
        updateAllReqData();
        updateDataByBankStatus();
        updateAgreedRejectedData();
    }

    private Chart createSalesChartByBankStatus() {
        chartByBankStatus = new Chart(ChartType.COLUMN);
        chartByBankStatus.setSizeFull();

        // Modify the default configuration a bit
        final Configuration conf = chartByBankStatus.getConfiguration();
        conf.setTitle("Заявки по банкам");
        conf.setSubTitle("Общее количество заявок по статусам рассмотрения");

        final Legend legend = new Legend();
        legend.setShadow(true);
        conf.setLegend(legend);

        return chartByBankStatus;
    }

    private void updateDataByBankStatus() {
        final Configuration conf = chartByBankStatus.getConfiguration();
        conf.setSeries(newArrayList());

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
        cq.where(
                cb.and(
                        cb.notEqual(saleProductJoin.type(), ProdInsurance.class),
                        cb.notEqual(productInSaleState, ProductInSale.State.NEW)));
        cq.groupBy(bank.get(Company_.id), productInSaleState);

        applyFilters(cb, cq, root);
        final TypedQuery<Tuple> tq = em.createQuery(cq);

        final HashBasedTable<ProductInSale.State, String, Long> dataTable = HashBasedTable.create();
        for (final Tuple t : tq.getResultList()) {
            final String name = t.get(bankName);
            final ProductInSale.State stat = t.get(productInSaleState);
            final Long count = t.get(bankCount);
            dataTable.put(stat, name, count);
        }
        final Set<String> bankSet = dataTable.columnKeySet();
        final YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Заявки");
        conf.removeyAxes();
        conf.addyAxis(y);
        final XAxis x = new XAxis();
        x.setCategories(bankSet.toArray(new String[bankSet.size()]));
        conf.removexAxes();
        conf.addxAxis(x);
        final ListSeries inProgressSeries = new ListSeries("На рассмотрении");
        final ListSeries agreedSeries = new ListSeries("Одобрен");
        final ListSeries rejectedSeries = new ListSeries("Отклонен");
        final ListSeries soldOutSeries = new ListSeries("Сделка оформлена");
        for (final String bankItem : bankSet) {
            inProgressSeries.addData(dataTable.get(ProductInSale.State.IN_PROGRESS, bankItem));
            agreedSeries.addData(dataTable.get(ProductInSale.State.AGREED, bankItem));
            rejectedSeries.addData(dataTable.get(ProductInSale.State.REJECTED, bankItem));
            soldOutSeries.addData(dataTable.get(ProductInSale.State.REJECTED, bankItem));
        }
        conf.addSeries(inProgressSeries);
        conf.addSeries(agreedSeries);
        conf.addSeries(rejectedSeries);
        chartByBankStatus.drawChart();
    }

    private Chart createAllRequestChart() {
        allReqChart = new Chart(ChartType.PIE);
        allReqChart.setSizeFull();

        // Modify the default configuration a bit
        final Configuration conf = allReqChart.getConfiguration();
        conf.setTitle("Заявки по банкам");
        conf.setSubTitle("Общее количество заявок");
        conf.getLegend().setEnabled(true); // Disable legend


        final PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        final Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage.toFixed(2) +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        return allReqChart;
    }

    private void updateAllReqData() {
        final Configuration conf = allReqChart.getConfiguration();
        conf.setSeries(newArrayList());

        final EntityManager em = lookup(EntityManager.class);
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        final Root<Sale> root = cq.from(Sale.class);
        final Join<ProductInSale, Product> saleProductJoin = root.join(Sale_.productInSales).join(ProductInSale_.product);
        final Join<Product, Company> bank = saleProductJoin.join(Product_.vendor);
        final Path<String> bankName = bank.get(Company_.name);
        final Expression<Long> bankCount = cb.count(bank);

        cq.multiselect(bankName, bankCount);
        cq.where(cb.notEqual(saleProductJoin.type(), ProdInsurance.class));
        cq.groupBy(bank);

        applyFilters(cb, cq, root);

        final TypedQuery<Tuple> tq = em.createQuery(cq);
        final DataSeries series = new DataSeries("Заявки за период");

        for (final Tuple t : tq.getResultList()) {
            final String name = t.get(bankName);
            final Long count = t.get(bankCount);
            final DataSeriesItem item = new DataSeriesItem(name, count);
            series.add(item);
        }
        conf.addSeries(series);
        allReqChart.drawChart();
    }
}
