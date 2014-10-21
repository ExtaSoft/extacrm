package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import java.util.function.Supplier;

/**
 * Таб, позволяющий создание контента каждой вкладки, только перед ее отображением
 *
 * @author Valery Orlov
 *         Date: 20.10.2014
 *         Time: 18:57
 */
public class LazyTabSheet extends TabSheet {

    public LazyTabSheet() {
        addSelectedTabChangeListener(event -> {
            final Component selectedTab = getSelectedTab();
            if (selectedTab instanceof TabUI) {
                final TabUI tabUI = (TabUI) selectedTab;
                tabUI.show();
            }
        });
    }

    public void addLazyTab(String caption, Supplier<? extends Component> contentSuplier) {
        addTab(new TabUI(contentSuplier), caption);
    }

    private class TabUI extends VerticalLayout {

        private final Supplier<? extends Component> contentSuplier;

        public TabUI(Supplier<? extends Component> contentSuplier) {
            this.contentSuplier = contentSuplier;
        }

        public void show() {
            removeAllComponents();
            addComponent(contentSuplier.get());
        }
    }
}
