/**
 *
 */
package ru.extas.web.info;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaTheme;


/**
 * Реализует экран информационных материалов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class InfoView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;

    private final static Logger logger = LoggerFactory.getLogger(InfoView.class);

    /** {@inheritDoc} */
    @Override
    protected Component createContent() {
        logger.debug("Creating view content...");

        return new InfoFilesGrid();
    }

    /** {@inheritDoc} */
    @Override
    protected Component createTitle() {
        final Component title = new Label("Информационные материалы");
        title.setSizeUndefined();
        title.addStyleName(ExtaTheme.VIEW_TITLE);
        return title;
    }

}
