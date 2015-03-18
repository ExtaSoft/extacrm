package ru.extas.web.analytics;

import com.google.common.collect.HashBasedTable;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Company_;
import ru.extas.model.sale.*;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaTheme;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.Set;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует экран аналитики
 *
 * @author Valery Orlov
 *         Date: 17.03.2015
 *         Time: 19:18
 */
public class AnalyticsView extends ExtaAbstractView {

    private final static Logger logger = LoggerFactory.getLogger(AnalyticsView.class);

    /**
     * <p>createContent.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    @Override
    protected Component createContent() {
        logger.info("Creating view content...");

        final TabSheet analyticsSheet = new TabSheet();
        analyticsSheet.setSizeFull();
        analyticsSheet.addTab(createSalesChartMain(), "Продажи");
        analyticsSheet.addTab(createSalesChartByBanks(), "Банки");
        analyticsSheet.addTab(createSalesChartMain(), "Одобрения/Отказы");
        analyticsSheet.addTab(createSalesChartMain(), "Продукты");
        analyticsSheet.addTab(createSalesChartMain(), "Техника");

        return analyticsSheet;
    }

    private VerticalLayout createSalesChartMain() {
        final VerticalLayout layout = new VerticalLayout();
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
        CriteriaQuery<Tuple> cq= cb.createTupleQuery();

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
            }
            else if (statusEn == Sale.Status.FINISHED) {
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

        layout.addComponent(chart);
        return layout;
    }

    private VerticalLayout createSalesChartByBanks() {
        final VerticalLayout layout = new VerticalLayout();

        layout.addComponent(createAllRequestChart());
        layout.addComponent(createSalesChartByBankStatus());

        return layout;
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
        ListSeries listSeries = new ListSeries();
        EntityManager em = lookup(EntityManager.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Sale> root = cq.from(Sale.class);
        Path<Sale.Status> saleStatus = root.get(Sale_.status);
        Join<Product, Company> bank = root.join(Sale_.productInSales).join(ProductInSale_.product).join(Product_.vendor);
        Path<String> bankName = bank.get(Company_.name);
        Expression<Long> bankCount = cb.count(bank);

        cq.multiselect(bankName, bankCount, saleStatus);
        cq.groupBy(bank.get(Company_.id), saleStatus);

        TypedQuery<Tuple> tq = em.createQuery(cq);

        HashBasedTable<Sale.Status, String, Long> dataTable = HashBasedTable.create();
        for (Tuple t : tq.getResultList()) {
            final String name = t.get(bankName);
            final Sale.Status stat = t.get(saleStatus);
            final Long count = t.get(bankCount);
            dataTable.put(stat, name, count);
        }
        Set<String> bankSet = dataTable.columnKeySet();
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
            openedSeries.addData(dataTable.get(Sale.Status.NEW, bankItem));
            closedSeries.addData(dataTable.get(Sale.Status.FINISHED, bankItem));
            rejectedSeries.addData(dataTable.get(Sale.Status.CANCELED, bankItem));
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
        CriteriaQuery<Tuple> cq= cb.createTupleQuery();

        Root<Sale> root = cq.from(Sale.class);
        Join<Product, Company> bank = root.join(Sale_.productInSales).join(ProductInSale_.product).join(Product_.vendor);
        Path<String> bankName = bank.get(Company_.name);
        Expression<Long> bankCount = cb.count(bank);

        cq.multiselect(bankName, bankCount);
        cq.groupBy(bank);
//        cq.orderBy(cb.desc(saleStatus));

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

    /**
     * <p>createTitle.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    @Override
    protected Component createTitle() {
        final Component title = new Label("Аналитика");
        title.setSizeUndefined();
        title.addStyleName(ExtaTheme.VIEW_TITLE);
        return title;
    }
}
