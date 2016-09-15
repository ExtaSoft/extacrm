/**
 *
 */
package ru.extas.web.settings;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.server.security.PermissionService;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;

import static ru.extas.server.ServiceLocator.lookup;


/**
 * Реализует экран настроек CRM
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SettingsView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;

    private final static Logger logger = LoggerFactory.getLogger(SettingsView.class);

    /** {@inheritDoc} */
    @Override
    protected Component createContent() {
        logger.debug("Creating view content...");

        final TabSheet settingsSheet = new TabSheet();
        settingsSheet.setSizeFull();
        settingsSheet.addTab(new MainSettingsForm(), "Общие настройки", Fontello.WRENCH_1);

        return settingsSheet;
    }

    private void updateDataBase() {
        lookup(PermissionService.class).permitAllOwnObjects();
    }

    /** {@inheritDoc} */
    @Override
    protected Component createTitle() {
        final Component title = new Label("Настройки");
        title.setSizeUndefined();
        title.addStyleName(ExtaTheme.VIEW_TITLE);
        return title;
    }

}
