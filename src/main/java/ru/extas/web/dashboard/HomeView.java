/**
 *
 */
package ru.extas.web.dashboard;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;

/**
 * Реализует домашний экран CRM
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class HomeView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;
    private final static Logger logger = LoggerFactory.getLogger(HomeView.class);

    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.info("Creating view content...");
        final Component title = new Label("Скоро будет реализовано...");
        title.setSizeUndefined();
        title.addStyleName("h1");
        title.addStyleName("icon-wrench-1");
        HorizontalLayout l = new HorizontalLayout(title);
        l.setSizeFull();
        l.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        return l;
    }

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Рабочий стол");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }

}
