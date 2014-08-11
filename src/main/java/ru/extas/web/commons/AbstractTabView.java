package ru.extas.web.commons;

import com.vaadin.ui.*;
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
 * @since 0.3
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

    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");
        final TabSheet tabsheet = new TabSheet();
        tabsheet.addStyleName("framed");
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
                //UI.getCurrent().getNavigator().navigateTo(info.getDomain().getName());
                UI.getCurrent().getPage().setUriFragment("!" + info.getDomain().getName(), false);
            }
        });

        String uriFragment = UI.getCurrent().getPage().getUriFragment();
        if (uriFragment.startsWith("!"))
            uriFragment = uriFragment.substring(1);
        // Создаем закладки в соответствии с описанием
        for (final TabInfo info : getTabComponentsInfo()) {
            final VerticalLayout viewTab = new VerticalLayout();
            viewTab.setSizeFull();
            viewTab.setData(info);
            TabSheet.Tab tab = tabsheet.addTab(viewTab, info.getCaption());
            if (info.getDomain().getName().equals(uriFragment)) {
                tabsheet.setSelectedTab(tab);
            }
        }

        return tabsheet;
    }

    /**
     * <p>getTabComponentsInfo.</p>
     *
     * @return a {@link java.util.List} object.
     */
    abstract protected List<TabInfo> getTabComponentsInfo();

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label(titleCaption);
        title.setSizeUndefined();
        title.addStyleName("view-title");
        return title;
    }
}
