/**
 *
 */
package ru.extas.web.loans;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;

/**
 * Реализует экран кредитной деятельности
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class LoansView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;
    private final static Logger logger = LoggerFactory.getLogger(LoansView.class);

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getContent()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getTitle()
     */
    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Кредитование");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }

}
