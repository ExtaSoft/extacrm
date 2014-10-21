package ru.extas.web.commons;

import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * <p>Abstract ExtaGrid class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 17:48
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ExtaGrid<TEntity> extends CustomComponent {
    private static final long serialVersionUID = 2299363623807745654L;

    /**
     * Constant <code>OVERALL_COLUMN="OverallColumn"</code>
     */
    public static final String OVERALL_COLUMN = "OverallColumn";

    private final Class<TEntity> entityClass;
    private FormService formService;
    protected FilterTable table;
    protected Container container;
    private List<UIAction> actions;
    private GridDataDecl dataDecl;
    private Mode currentMode;
    private boolean toolbarVisible = true;
    private List<MenuBar.MenuItem> needCurrentMenu;
    private MenuBar.MenuItem tableModeBtn;
    private MenuBar.MenuItem detailModeBtn;

    public void selectObject(final Object objectId) {
        if (table != null && objectId != null && table.containsId(objectId))
            table.select(objectId);
    }

    public enum Mode {
        TABLE,
        DETAIL_LIST
    }

    public interface FormService extends Serializable {

        void open4Edit(ExtaEditForm form);

        void open4Insert(ExtaEditForm form);

    }

    public static class ModalPopupFormService implements FormService {
        private final ExtaGrid grid;

        public ModalPopupFormService(final ExtaGrid grid) {
            this.grid = grid;
        }

        @Override
        public void open4Edit(final ExtaEditForm form) {
            form.addCloseFormListener(event -> {
                if (form.isSaved()) {
                    grid.refreshContainerItem(form.getObjectId());
                }
                grid.selectObject(form.getObjectId());
            });
            FormUtils.showModalWin(form);
        }

        @Override
        public void open4Insert(final ExtaEditForm form) {
            form.addCloseFormListener(event -> {
                if (form.isSaved()) {
                    grid.refreshContainer();
                    grid.selectObject(form.getObjectId());
                }
            });
            FormUtils.showModalWin(form);
        }
    }

    /**
     * <p>Constructor for ExtaGrid.</p>
     *
     * @param initNow a boolean.
     */
    public ExtaGrid(final Class<TEntity> entityClass, final boolean initNow) {
        this.entityClass = entityClass;
        formService = new ModalPopupFormService(this);
        if (initNow)
            initialize();
    }

    /**
     * <p>Constructor for ExtaGrid.</p>
     */
    public ExtaGrid(final Class<TEntity> entityClass) {
        this(entityClass, true);
    }

    public FormService getFormService() {
        return formService;
    }

    public void setFormService(final FormService formService) {
        this.formService = formService;
    }

    public Class<TEntity> getEntityClass() {
        return entityClass;
    }

    public TEntity createEntity() {
        try {
            return entityClass.newInstance();
        } catch (final Throwable t) {
            throw Throwables.propagate(t);
        }
    }

    public TEntity getSelectedEntity() {
        if (table != null) {
            final Object itemId = table.getValue();
            if (itemId != null) {
                final Item item = table.getItem(itemId);
                if (item != null)
                    return GridItem.extractBean(item);
            }
        }
        return null;
    }

    public abstract ExtaEditForm<TEntity> createEditForm(TEntity entity, boolean isInsert);

    public void doEditObject(final TEntity entity) {

        final ExtaEditForm<TEntity> form = createEditForm(entity, false);
        if(!form.isReadOnly())
            form.setReadOnly(!GridUtils.isPermitEdit(container, entity));
        formService.open4Edit(form);
    }

    public void doEditNewObject(final TEntity init) {
        final TEntity entity = init == null ? createEntity() : init;

        if (GridUtils.isPermitInsert(container)) {
            final ExtaEditForm<TEntity> form = createEditForm(entity, true);
            formService.open4Insert(form);
        } else
            NotificationUtil.showWarning("Ввод новых объектов запрещен администратором!");
    }

    /**
     * <p>initialize.</p>
     */
    protected void initialize() {
        // Запрос данных
        container = createContainer();
        // Действия в таблице
        actions = createActions();
        // Описатель данных
        dataDecl = createDataDecl();

        initContent(Mode.TABLE);
    }

    private void initContent(final Mode mode) {
        currentMode = mode;
        setSizeFull();

        final GridLayout panel = new GridLayout(2, 2);
//        panel.addStyleName(toolbarVisible ? "grid-panel" : "grid-panel-notools");
        panel.setSizeFull();

        panel.setRowExpandRatio(0, 0);
        panel.setRowExpandRatio(1, 1);
        panel.setColumnExpandRatio(0, 1);
        panel.setColumnExpandRatio(1, 0);
        panel.setMargin(true);

        if (toolbarVisible) {
            panel.setSpacing(true);
            // Формируем тулбар
            final MenuBar commandBar = createGridToolbar(mode);
            panel.addComponent(commandBar, 0, 0);
            panel.setComponentAlignment(commandBar, Alignment.TOP_LEFT);

            // Переключение режима таблицы
            final MenuBar modeSwitchBar = new MenuBar();
            modeSwitchBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            final MenuBar.MenuItem tableFilterBtn = modeSwitchBar.addItem("", Fontello.FILTER, selectedItem -> table.setFilterBarVisible(selectedItem.isChecked()));
            tableFilterBtn.setDescription("Показать строку фильтра таблицы");
            tableFilterBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            tableFilterBtn.setCheckable(true);

            final MenuBar.Command modeCommand = selectedItem -> {
                if (selectedItem == tableModeBtn && currentMode == Mode.DETAIL_LIST) {
                    setMode(Mode.TABLE);
                } else if (currentMode == Mode.TABLE) {
                    setMode(Mode.DETAIL_LIST);
                }
            };
            tableModeBtn = modeSwitchBar.addItem("", Fontello.TABLE, modeCommand);
            tableModeBtn.setDescription("Нажмите чтобы переключить список в стандартный табличный режим");
            tableModeBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            tableModeBtn.setCheckable(true);

            detailModeBtn = modeSwitchBar.addItem("", Fontello.LIST_ALT, modeCommand);
            detailModeBtn.setDescription("Нажмите чтобы переключить список в детализированный режим");
            detailModeBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            detailModeBtn.setCheckable(true);

            if (currentMode == Mode.TABLE) {
                tableModeBtn.setChecked(true);
                detailModeBtn.setChecked(false);
            } else {
                detailModeBtn.setChecked(true);
                tableModeBtn.setChecked(false);
            }
            panel.addComponent(modeSwitchBar, 1, 0);
            panel.setComponentAlignment(modeSwitchBar, Alignment.TOP_RIGHT);
        }

        // Таблица
        initTable(mode);
        panel.addComponent(table, 0, 1, 1, 1);

        setCompositionRoot(panel);
    }

    public void setMode(final Mode mode) {
        initContent(mode);
        detailModeBtn.setChecked(mode == Mode.DETAIL_LIST);
        tableModeBtn.setChecked(mode == Mode.TABLE);
    }

    public Mode getMode() {
        return currentMode;
    }

    public boolean isToolbarVisible() {
        return toolbarVisible;
    }

    public void setToolbarVisible(final boolean toolbarVisible) {
        this.toolbarVisible = toolbarVisible;
    }

    private MenuBar createGridToolbar(final Mode mode) {
        final MenuBar commandBar = new MenuBar();
        commandBar.setAutoOpen(true);
        commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
        commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
//        commandBar.focus();

        needCurrentMenu = newArrayList();
        for (final UIAction action : actions) {
            final MenuBar.MenuItem menuItem = commandBar.addItem(action.getName(), action.getIcon(), null);
            fillGridTollbarItem(action, menuItem);
        }
        return commandBar;
    }

    private void fillGridTollbarItem(final UIAction action, final MenuBar.MenuItem menuItem) {
        menuItem.setDescription(action.getDescription());

        if (action instanceof UIActionGroup) {
            final List<UIAction> actionsGroup = ((UIActionGroup) action).getActionsGroup();
            for (final UIAction subAction : actionsGroup) {
                fillGridTollbarItem(subAction, menuItem.addItem(subAction.getName(), subAction.getIcon(), null));
            }
        } else {
            final MenuBar.Command command = selectedItem -> {
                if (action instanceof ItemAction) {
                    final Object item = table.getValue();
                    refreshContainerItem(item);
                    action.fire(checkNotNull(item, "No selected row"));
                } else
                    action.fire(null);

            };
            menuItem.setCommand(command);
        }
        if (action instanceof ItemAction) {
            needCurrentMenu.add(menuItem);
            menuItem.setEnabled(false);
        }
        if (action instanceof ExtaGrid.NewObjectAction) {
            menuItem.setEnabled(GridUtils.isPermitInsert(container));
        }
    }

    /**
     * Полноценная инициализация колонок таблицы
     *
     * @param mode a {@link ru.extas.web.commons.ExtaGrid.Mode} object.
     */
    protected void initTable(final Mode mode) {

        // Создаем таблицу
        table = new FilterTable();
        // Общие настройки таблицы
        table.setContainerDataSource(container);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
        table.setSizeFull();
        table.setSizeFull();

        // Ищем действие по умолчанию
        final UIAction defAction = getDefaultAction(actions);
        if (defAction != null) {
            table.addActionHandler(new Action.Handler() {
                @Override
                public Action[] getActions(final Object target, final Object sender) {
                    final Action[] actionsArr = new Action[actions.size()];
                    int i = 0;
                    for (final UIAction a : actions) {
                        actionsArr[i] = new Action(a.getName());
                        i++;
                    }
                    return actionsArr;
                }

                @Override
                public void handleAction(final Action action, final Object sender, final Object target) {
                    final UIAction firedAction = Iterables.find(actions, input -> input.getName().equals(action.getCaption()));
                    firedAction.fire(table.getValue());
                }
            });
        }

        if (mode == Mode.DETAIL_LIST) {
            initDetailTable(defAction);
        } else { // Classic table
            // Настройка столбцов таблицы
            table.setColumnHeaderMode(CustomTable.ColumnHeaderMode.EXPLICIT);
            //table.removecolutable.getVisibleColumns()
            fullInitTable(table, dataDecl);
            table.addItemClickListener(event -> {
                if (event.isDoubleClick())
                    defAction.fire(event.getItemId());
            });
            for (final MenuBar.MenuItem btn : needCurrentMenu)
                btn.setVisible(true);
            // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
            table.addValueChangeListener(event -> {
                final boolean enableBtb = event.getProperty().getValue() != null;
                for (final MenuBar.MenuItem btn : needCurrentMenu)
                    btn.setEnabled(enableBtb);
            });

        }
    }

    private void initDetailTable(final UIAction defAction) {
        table.setColumnHeaderMode(CustomTable.ColumnHeaderMode.HIDDEN);
        table.addGeneratedColumn(OVERALL_COLUMN, createDetailColumnGenerator(defAction));
        table.setColumnHeader(OVERALL_COLUMN, "Общая информация");
        table.setVisibleColumns(OVERALL_COLUMN);
        for (final MenuBar.MenuItem btn : needCurrentMenu)
            btn.setVisible(false);
    }

    protected CustomTable.ColumnGenerator createDetailColumnGenerator(final UIAction defAction) {
        return new DefaultDetailGenerator(defAction);
    }

    private HorizontalLayout createItemToolbar(final Object itemId) {
        final HorizontalLayout actionToolbar = new HorizontalLayout();
        actionToolbar.addStyleName(ExtaTheme.ITEM_TOOLBAR);
        actionToolbar.setSpacing(true);
        actionToolbar.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        for (final UIAction a : actions)
            if (a instanceof ItemAction && !(a instanceof DefaultAction)) {
                final Component command = a.createButton();
                if (command instanceof Button) {
                    ((Button) command).addClickListener(event -> a.fire(itemId));
                }
                actionToolbar.addComponent(command);
            }
        return actionToolbar;
    }

    private UIAction getDefaultAction(final List<UIAction> actions) {
        UIAction defAction = null;
        for (final UIAction a : actions)
            if (a instanceof DefaultAction) {
                defAction = a;
                break;
            }
        return defAction;
    }

    /**
     * <p>refreshContainer.</p>
     */
    protected void refreshContainer() {
        final Object itemId = table.getValue();
        if (container instanceof ExtaDataContainer)
            ((ExtaDataContainer) container).refresh();
        else if (container instanceof RefreshBeanContainer)
            ((RefreshBeanContainer) container).refreshItems();
        table.setValue(itemId);
    }

    /**
     * <p>refreshContainerItem.</p>
     *
     * @param itemId a {@link java.lang.Object} object.
     */
    protected void refreshContainerItem(final Object itemId) {
        if (container instanceof ExtaDataContainer)
            ((ExtaDataContainer) container).refreshItem(itemId);
        else if (container instanceof RefreshBeanContainer)
            ((RefreshBeanContainer) container).refreshItems();
    }

    public void adjustGridHeight() {
        table.setPageLength(table.size());
    }

    /**
     * <p>createDataDecl.</p>
     *
     * @return a {@link ru.extas.web.commons.GridDataDecl} object.
     */
    protected abstract GridDataDecl createDataDecl();

    /**
     * <p>createContainer.</p>
     *
     * @return a {@link com.vaadin.data.Container} object.
     */
    protected abstract Container createContainer();

    /**
     * <p>createActions.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected abstract List<UIAction> createActions();

    private class DefaultDetailGenerator implements CustomTable.ColumnGenerator {
        private final UIAction defAction;

        public DefaultDetailGenerator(final UIAction defAction) {
            this.defAction = defAction;
        }

        @Override
        public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
            final Item item = source.getItem(itemId);

            final Iterator<DataDeclMapping> mapIterator = dataDecl.getMappings().iterator();
            DataDeclMapping titleMap = mapIterator.next();
            if (titleMap.getPropName().equals("id"))
                titleMap = mapIterator.next();
            final VerticalLayout panel = new VerticalLayout();

            // Основная строка данных
            final AbstractComponent titleComp;
            if (defAction == null) {
                final Label titleLbl = new Label(item.getItemProperty(titleMap.getPropName()));
                if (titleMap.getConverter() != null)
                    titleLbl.setConverter(titleMap.getConverter());
                titleLbl.setDescription(titleMap.getCaption());
                titleComp = titleLbl;
            } else {
                final Button titleLink = new Button();
                titleLink.addStyleName(ExtaTheme.BUTTON_LINK);
                titleLink.setCaption((String) item.getItemProperty(titleMap.getPropName()).getValue());
                titleLink.setDescription(defAction.getDescription());
                titleLink.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                titleLink.addClickListener(event -> defAction.fire(itemId));
                titleComp = titleLink;
            }
            titleComp.setImmediate(true);
            titleComp.addStyleName(ExtaTheme.MAIN_ITEM_TEXT);
            panel.addComponent(titleComp);
//                final HorizontalLayout header = new HorizontalLayout(titleComp, createItemToolbar(itemId));
//                header.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
//                panel.addComponent(header);

            // Дополнительные данные
            final HorizontalLayout details = new HorizontalLayout();
            details.setSpacing(true);
            while (mapIterator.hasNext()) {
                final DataDeclMapping prop = mapIterator.next();
                if (!prop.isCollapsed()) {
                    final Label detail = new Label(item.getItemProperty(prop.getPropName()));
                    detail.addStyleName(ExtaTheme.LABEL_H3);
                    detail.setDescription(prop.getCaption());
                    details.addComponent(detail);
                }
            }
            panel.addComponent(details);

            // Кнопки действий
            final HorizontalLayout actionToolbar = createItemToolbar(itemId);
            if (actionToolbar.getComponentCount() > 0)
                panel.addComponent(actionToolbar);

            // Forward clicks on the layout as selection
            // in the table
            panel.addLayoutClickListener(event -> {
                source.select(itemId);
                if (event.isDoubleClick())
                    defAction.fire(itemId);
            });
            panel.setImmediate(true);

            return panel;
        }
    }

    protected class NewObjectAction extends UIAction {
        public NewObjectAction(final String caption, final String description, final Fontello icon) {
            super(caption, description, icon);
        }

        public NewObjectAction(final String caption, final String description) {
            super(caption, description, Fontello.DOC_NEW);
        }

        @Override
        public void fire(final Object itemId) {
            doEditNewObject(null);
        }
    }

    protected class EditObjectAction extends DefaultAction {
        public EditObjectAction(final String caption, final String description, final Fontello icon) {
            super(caption, description, icon);
        }

        public EditObjectAction(final String caption, final String description) {
            super(caption, description, Fontello.EDIT_3);
        }

        @Override
        public void fire(final Object itemId) {
            final TEntity entity = GridItem.extractBean(table.getItem(itemId));
            doEditObject(entity);
        }
    }
}
