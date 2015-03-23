package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import ru.extas.utils.SupplierSer;


public class LazyTabSheet extends TabSheet {

    public LazyTabSheet() {
        addSelectedTabChangeListener(new LazyTabChangeListener());
    }


    public static class LazyTab extends CustomComponent {
        private final boolean loadOnce;
        private final SupplierSer<Component> contentSupplier;
        private boolean inited = false;

        public LazyTab(final SupplierSer<Component> contentSupplier) {
            this(contentSupplier, true);
        }


        public LazyTab(final SupplierSer<Component> contentSupplier, final boolean loadOnce) {
            this.loadOnce = loadOnce;
            this.contentSupplier = contentSupplier;
        }


        public final void refresh() {
            if(!inited || !loadOnce) {
                setCompositionRoot(contentSupplier.get());
                inited = true;
            }
        }
    }


    private static class LazyTabChangeListener implements SelectedTabChangeListener {
        @Override
        public void selectedTabChange(final SelectedTabChangeEvent event) {
            final Component selectedTab = event.getTabSheet().getSelectedTab();
            if (selectedTab instanceof LazyTab) {
                ((LazyTab) selectedTab).refresh();
            }
        }
    }

}
