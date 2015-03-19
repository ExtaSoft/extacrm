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
        analyticsSheet.addTab(new SalesChartMain(), "Продажи");
        analyticsSheet.addTab(new SalesChartByBanks(), "Банки");
        analyticsSheet.addTab(new AgreedRejectedSalesChart(), "Одобрения/Отказы");
        analyticsSheet.addTab(new SalesChartByProduct(), "Продукты");
        analyticsSheet.addTab(new SalesChartByMotor(), "Техника");

        return analyticsSheet;
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
