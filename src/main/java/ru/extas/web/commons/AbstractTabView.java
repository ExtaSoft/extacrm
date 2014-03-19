package ru.extas.web.commons;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.component.TabInfo;

import java.util.List;

/**
 * <p>Abstract AbstractTabView class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:00
 * @version $Id: $Id
 */
public abstract class AbstractTabView extends ExtaAbstractView {
    private final static Logger logger = LoggerFactory.getLogger(AbstractTabView.class);

    /**
     * <p>Constructor for AbstractTabView.</p>
     *
     * @param titleCaption a {@link java.lang.String} object.
     */
    protected AbstractTabView(String titleCaption) {
        this.titleCaption = titleCaption;
    }

    private String titleCaption;

    /*
         * (non-Javadoc)
         *
         * @see ru.extas.web.ExtaAbstractView#getContent()
         */
    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Create tab content dynamically when tab is selected
        tabsheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(final TabSheet.SelectedTabChangeEvent event) {
                // Find the tabsheet
                final TabSheet tabsheet = event.getTabSheet();
                // Find the tab (here we know it's a layout)
                final VerticalLayout tab = (VerticalLayout) tabsheet.getSelectedTab();

                // if (tab.getComponentCount() == 0) {
                // Инициализируем содержимое закладки
                final TabInfo info = (TabInfo) tab.getData();

                tab.removeAllComponents();
                final Component tabContent = info.createComponent();
                tabContent.setSizeFull();
                tab.addComponent(tabContent);
                // } else
                // tab.getComponent(0).markAsDirtyRecursive();
            }
        });

        // Создаем закладки в соответствии с описанием
        for (final TabInfo info : getTabComponentsInfo()) {
            final VerticalLayout viewTab = new VerticalLayout();
            viewTab.setSizeFull();
            viewTab.setData(info);
            tabsheet.addTab(viewTab, info.getCaption());
        }

        return tabsheet;
    }

    /**
     * <p>getTabComponentsInfo.</p>
     *
     * @return a {@link java.util.List} object.
     */
    abstract protected List<TabInfo> getTabComponentsInfo();

    /*
         * (non-Javadoc)
         *
         * @see ru.extas.web.ExtaAbstractView#getTitle()
         */
    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label(titleCaption);
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }
}
