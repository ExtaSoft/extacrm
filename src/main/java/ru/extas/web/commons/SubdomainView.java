package ru.extas.web.commons;

import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

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

    private TabSheet tabsheet;
    private final List<TabSheet.Tab> tabs = newArrayList();

    /**
     * <p>Constructor for AbstractTabView.</p>
     *
     * @param titleCaption a {@link java.lang.String} object.
     */
    protected SubdomainView(String titleCaption) {
        this.titleCaption = titleCaption;
        tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Create tab content dynamically when tab is selected
        tabsheet.addSelectedTabChangeListener(event -> {
            final SubdomainUI subdomainUI = (SubdomainUI) tabsheet.getSelectedTab();
            subdomainUI.show();
        });
        // Создаем закладки в соответствии с описанием
        getSubdomainInfo().forEach(info -> tabs.add(tabsheet.addTab(new SubdomainUI(info), info.getCaption())));
    }

    private String titleCaption;

    private class SubdomainUI extends VerticalLayout {

        private final SubdomainInfo info;
        private ExtaGrid grid;

        public SubdomainUI(SubdomainInfo info) {
            this.info = info;
            setSizeFull();
        }

        public void show() {
            if (grid == null) initGrid();
            else grid.refreshContainer();

            final ExtaUri uri = new ExtaUri();//info.getDomain(), ExtaUri.Mode.GRID, null);

            if(uri.getDomain() == info.getDomain()) {
                if(uri.getMode() == ExtaUri.Mode.GRID) {
                    if (!isNullOrEmpty(uri.getId())) {
                        grid.selectObject(uri.getId());
                    }
                } else if(uri.getMode() == ExtaUri.Mode.NEW) {
                    grid.doEditNewObject(null);
                } else if(uri.getMode() == ExtaUri.Mode.EDIT) {
                    if (!isNullOrEmpty(uri.getId())) {
                        EntityManager em = lookup(EntityManager.class);
                        Object obj = em.find(grid.getEntityClass(), uri.getId());
                        if (obj != null) {
                            grid.doEditObject(obj);
                        } else {
                            NotificationUtil.showWarning("Немогу открыть форму редактирования",
                                    "Ненайден объект редактирования. Возможно, неверный URL, или объект был удален.");
                        }
                    } else
                        NotificationUtil.showWarning("Немогу открыть форму редактирования",
                                "Ненайден объект редактирования. Возможно, неверный URL.");
                }
            } else {
                NavigationUtils.setUriFragment(new ExtaUri(info.getDomain(), ExtaUri.Mode.GRID, null));
            }
        }

        public void initGrid() {
            grid = info.createGrid();
            grid.setSizeFull();
            addComponent(grid);
            ExtaGrid.FormService defaultService = grid.getFormService();
            grid.setFormService(new ExtaGrid.DefaultFormService(grid) {
                @Override
                public void open4Edit(ExtaEditForm form) {
                    final ExtaUri uri = new ExtaUri(info.getDomain(), ExtaUri.Mode.EDIT, form.getObjectId().toString());
                    NavigationUtils.setUriFragment(uri);
                    form.addCloseFormListener(event -> {
                        uri.setMode(ExtaUri.Mode.GRID);
                        NavigationUtils.setUriFragment(uri);
                    });
                    defaultService.open4Edit(form);
                }

                @Override
                public void open4Insert(ExtaEditForm form) {
                    final ExtaUri uri = new ExtaUri(info.getDomain(), ExtaUri.Mode.NEW, null);
                    NavigationUtils.setUriFragment(uri);
                    form.addCloseFormListener(event -> {
                        uri.setMode(ExtaUri.Mode.GRID);
                        final Object objectId = form.getObjectId();
                        if(objectId != null) {
                            uri.setId(objectId.toString());
                            grid.selectObject(objectId);
                        }
                        NavigationUtils.setUriFragment(uri);
                    });
                    defaultService.open4Insert(form);
                }
            });
        }

        public SubdomainInfo getInfo() {
            return info;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component getContent() {
        logger.debug("Creating view content...");

        final ExtaUri uri = new ExtaUri();
        TabSheet.Tab tabCandidat = getTab4Uri(uri).orElse(tabs.get(0));

        // Делаем текущей нужную закладку
        tabsheet.setSelectedTab(tabCandidat);

        return tabsheet;
    }

    private Optional<TabSheet.Tab> getTab4Uri(ExtaUri uri) {
        return tabs.stream()
                .filter(tab -> ((SubdomainUI) tab.getComponent()).getInfo().getDomain() == uri.getDomain())
                .findFirst();
    }

    /**
     * <p>getSubdomainInfo.</p>
     *
     * @return a {@link java.util.List} object.
     */
    abstract protected List<SubdomainInfo> getSubdomainInfo();

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component getTitle() {
        final Component title = new Label(titleCaption);
        title.setSizeUndefined();
        title.addStyleName("view-title");
        return title;
    }
}
