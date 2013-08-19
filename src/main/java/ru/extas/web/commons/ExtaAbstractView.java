/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый абстрактный класс для классов раздела
 *
 * @author Valery Orlov
 */
public abstract class ExtaAbstractView extends VerticalLayout implements View {

    private static final long serialVersionUID = -9143359275908526515L;
    private final Logger logger = LoggerFactory.getLogger(ExtaAbstractView.class);

    /**
     *
     */
    protected ExtaAbstractView() {
        super();
    }

    /**
     * @param children
     */
    private ExtaAbstractView(Component... children) {
        super(children);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        logger.info("Entering view {}...", event.getViewName());
        setSizeFull();
        addStyleName("base-view");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Component title = getTitle();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        Button helpBtn = new Button();
        helpBtn.setDescription("Контекстная справка");
        // notify.addStyleName("borderless");
        helpBtn.addStyleName("notifications");
        helpBtn.addStyleName("icon-only");
        helpBtn.addStyleName("icon-help-1");
        helpBtn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                // TODO: Implement context help
                Notification.show("Не реализовано пока");
                // new InsuranceRepositoryJdo().fillRegistry();
            }
        });
        top.addComponent(helpBtn);
        top.setComponentAlignment(helpBtn, Alignment.MIDDLE_LEFT);

        VerticalLayout row = new VerticalLayout();
        row.setMargin(true);
        row.setSizeFull();
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 2);

        row.addComponent(getContent());

    }

    /**
     * @return
     */
    protected abstract Component getContent();

    /**
     * @return
     */
    protected abstract Component getTitle();

}