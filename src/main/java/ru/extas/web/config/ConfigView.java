/**
 *
 */
package ru.extas.web.config;

import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.server.users.PermissionService;
import ru.extas.web.commons.ExtaAbstractView;

import static ru.extas.server.ServiceLocator.lookup;


/**
 * Реализует экран настроек CRM
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ConfigView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;

    private final static Logger logger = LoggerFactory.getLogger(ConfigView.class);

    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");
        final Button updateBtn = new Button("Обновить права доступа", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDataBase();
            }
        });
        final Component title = new Label("Скоро будет реализовано...");
        title.setSizeUndefined();
        title.addStyleName("h1");
        title.addStyleName("icon-wrench-1");
        HorizontalLayout l = new HorizontalLayout(title);
        l.addComponent(updateBtn);
        l.setSizeFull();
        l.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        return l;
    }

    private void updateDataBase() {
        lookup(PermissionService.class).permitAllOwnObjects();
    }

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Настройки");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }

}
