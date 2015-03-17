package ru.extas.web.analytics;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaTheme;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

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

        final VerticalLayout layout = new VerticalLayout();
        Chart chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");
        //chart.setHeight("300px");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Продажи");
        conf.setSubTitle("Количество продаж по статусам");
        conf.getLegend().setEnabled(false); // Disable legend


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
        final Path<Sale.Status> status = root.get(Sale_.status);
        Expression<Long> count = cb.count(status);

        cq.multiselect(status, count);
        cq.groupBy(status);
//        cq.orderBy(cb.desc(status));

        TypedQuery<Tuple> tq = em.createQuery(cq);
        DataSeries series = new DataSeries("Продажи");

        for (Tuple t : tq.getResultList()) {
            final Sale.Status statusEn = t.get(status);
            final Long countL = t.get(count);
            final DataSeriesItem item = new DataSeriesItem();
            item.setY(countL);
            if (statusEn == Sale.Status.NEW)
                item.setName("Открытые");
            else if (statusEn == Sale.Status.FINISHED) {
                item.setName("Завершенные");
                item.setSliced(true);
                item.setSelected(true);
            } else
                item.setName("Отмененные");
            series.add(item);
        }
        conf.addSeries(series);

        layout.addComponent(chart);
        return layout;
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
