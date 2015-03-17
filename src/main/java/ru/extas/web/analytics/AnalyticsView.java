package ru.extas.web.analytics;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaTheme;

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
