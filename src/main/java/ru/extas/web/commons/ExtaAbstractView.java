/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый абстрактный класс для классов раздела
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ExtaAbstractView extends VerticalLayout implements View {

    private static final long serialVersionUID = -9143359275908526515L;
    private final static Logger logger = LoggerFactory.getLogger(ExtaAbstractView.class);
    private VerticalLayout content;

    /**
     * <p>Constructor for ExtaAbstractView.</p>
     */
    protected ExtaAbstractView() {
        super();
    }

    /**
     * @param children
     */
    private ExtaAbstractView(final Component... children) {
        super(children);
    }

    /** {@inheritDoc} */
    @Override
    public void enter(ViewChangeEvent event) {
        logger.info("Entering view {}...", event.getViewName());
        setSizeFull();
        addStyleName("base-view");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth(100, Unit.PERCENTAGE);
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Component title = getTitle();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        Button helpBtn = new Button(Fontello.HELP_1);
        helpBtn.setDescription("Контекстная справка");
        // notify.addStyleName("borderless");
        helpBtn.addStyleName("icon-only");
        helpBtn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                // TODO: Implement context help
                NotificationUtil.show("Не реализовано пока");
            }
        });
        top.addComponent(helpBtn);
        top.setComponentAlignment(helpBtn, Alignment.MIDDLE_LEFT);

        content = new VerticalLayout();
        content.setMargin(true);
        content.setSizeFull();
        content.setSpacing(true);
        addComponent(content);
        setExpandRatio(content, 5);

        replaceContent(getContent());

    }

    protected void replaceContent(Component component){
        content.removeAllComponents();
        content.addComponent(component);
    }

    /**
     * <p>getContent.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    protected abstract Component getContent();

    /**
     * <p>getTitle.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    protected abstract Component getTitle();

}
