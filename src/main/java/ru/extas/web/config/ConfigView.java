/**
 *
 */
package ru.extas.web.config;

import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.server.ContactService;
import ru.extas.web.commons.ExtaAbstractView;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует экран настроек CRM
 *
 * @author Valery Orlov
 */
public class ConfigView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;

    private final Logger logger = LoggerFactory.getLogger(ConfigView.class);

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getContent()
     */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");
        final Button updateBtn = new Button("Обновить базу", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDataBase();
            }
        });
        final Component title = new Label("Скоро будет реализовано...");
        title.setSizeUndefined();
        title.addStyleName("h1");
        title.addStyleName("icon-wrench-1");
        HorizontalLayout l = new HorizontalLayout(title, updateBtn);
        l.setSizeFull();
        l.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        return l;
    }

    private void updateDataBase() {
        ContactService service = lookup(ContactService.class);
        service.updateMissingType();
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getTitle()
     */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Настройки");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }

}
