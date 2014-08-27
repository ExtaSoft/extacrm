package ru.extas.web.commons;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class SubdomainView extends ExtaAbstractView {
    private final static Logger logger = LoggerFactory.getLogger(SubdomainView.class);

    /**
     * <p>Constructor for AbstractTabView.</p>
     *
     * @param titleCaption a {@link java.lang.String} object.
     */
    protected SubdomainView(String titleCaption) {
        this.titleCaption = titleCaption;
    }

    private String titleCaption;

    /** {@inheritDoc} */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");

        final ExtaUri uri = new ExtaUri();

        if(uri.getMode() == ExtaUri.Mode.GRID) {
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
                    final SubdomainInfo info = (SubdomainInfo) tab.getData();

                    tab.removeAllComponents();
                    final ExtaGrid tabGrid = info.createGrid();
                    tabGrid.setSizeFull();
                    tab.addComponent(tabGrid);

                    UI.getCurrent().getPage().setUriFragment("!" + info.getDomain().getName(), false);
                }
            });


            // Создаем закладки в соответствии с описанием
            for (final SubdomainInfo info : getSubdomainInfo()) {
                final VerticalLayout viewTab = new VerticalLayout();
                viewTab.setSizeFull();
                viewTab.setData(info);
                TabSheet.Tab tab = tabsheet.addTab(viewTab, info.getCaption());
                if (info.getDomain() == uri.getDomain()) {
                    tabsheet.setSelectedTab(tab);
                }
            }
            return tabsheet;
        } else {
            SubdomainInfo info = Iterables.tryFind(getSubdomainInfo(), new Predicate<SubdomainInfo>() {
                @Override
                public boolean apply(SubdomainInfo input) {
                    return input.getDomain() == uri.getDomain();
                }
            }).orNull();

            return null;//info.createForm(uri.getId());
        }
    }

    /**
     * <p>getSubdomainInfo.</p>
     *
     * @return a {@link java.util.List} object.
     */
    abstract protected List<SubdomainInfo> getSubdomainInfo();

    /** {@inheritDoc} */
    @Override
    protected Component getTitle() {
        final Component title = new Label(titleCaption);
        title.setSizeUndefined();
        title.addStyleName("view-title");
        return title;
    }
}
