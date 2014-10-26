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
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ExtaAbstractView extends VerticalLayout implements View {

    private static final long serialVersionUID = -9143359275908526515L;
    private final static Logger logger = LoggerFactory.getLogger(ExtaAbstractView.class);
    protected VerticalLayout content;

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
    public void enter(final ViewChangeEvent event) {
        logger.info("Entering view {}...", event.getViewName());
        setSizeFull();
        addStyleName(ExtaTheme.BASE_VIEW);

        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth(100, Unit.PERCENTAGE);
        top.setSpacing(true);
        top.addStyleName(ExtaTheme.TOOLBAR);
        addComponent(top);
        final Component title = createTitle();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        final Button helpBtn = new Button(Fontello.HELP_1);
        helpBtn.setDescription("Контекстная справка");
        helpBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        helpBtn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
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

        final Component contentComponent = createContent();
        if(contentComponent != null)
            content.addComponent(contentComponent);

    }

    /**
     * <p>createContent.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    protected abstract Component createContent();

    /**
     * <p>createTitle.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    protected abstract Component createTitle();

}
