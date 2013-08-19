/**
 *
 */
package ru.extas.web.users;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует экран управления пользователями и правами доступа
 *
 * @author Valery Orlov
 */
public class UsersView extends ExtaAbstractView {

    private static final long serialVersionUID = -1272779672761523416L;
    private final Logger logger = LoggerFactory.getLogger(UsersView.class);

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getContent()
     */
    @Override
    protected Component getContent() {
        logger.info("Creating view content...");

        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Create tab content dynamically when tab is selected
        tabsheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(final SelectedTabChangeEvent event) {
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

    List<TabInfo> getTabComponentsInfo() {
        final ArrayList<TabInfo> ret = newArrayList();
        ret.add(new AbstractTabInfo("Пользователи") {
            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent() {
                return new UsersGrid();
            }
        });
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.ExtaAbstractView#getTitle()
     */
    @Override
    protected Component getTitle() {
        final Component title = new Label("Пользователи");
        title.setSizeUndefined();
        title.addStyleName("h1");
        return title;
    }

}
