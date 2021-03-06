package ru.extas.web.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.vaadin.addon.tableexport.CustomTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Between;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.FilterTableStateHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableState;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.model.common.ArchivedObject;
import ru.extas.model.common.AuditedObject_;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.model.security.SecuredObject;
import ru.extas.model.security.UserRole;
import ru.extas.model.settings.UserGridState;
import ru.extas.server.common.ArchiveService;
import ru.extas.server.security.UserManagementService;
import ru.extas.server.settings.UserGridStateService;
import ru.extas.web.commons.component.PastDateIntervalField;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.RefreshBeanContainer;
import ru.extas.web.commons.window.DownloadFileWindow;
import ru.extas.web.users.SecuritySettingsForm;
import ru.extas.web.users.UserProfileFilterGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newHashSetWithExpectedSize;
import static ru.extas.server.ServiceLocator.lookup;
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
public abstract class ExtaGrid<TEntity> extends CustomComponent implements ExtaGridFilterPanel.IFilterGrid {
    private static final long serialVersionUID = 2299363623807745654L;
    private final static Logger logger = LoggerFactory.getLogger(ExtaGrid.class);

    /**
     * Constant <code>OVERALL_COLUMN="OverallColumn"</code>
     */
    public static final String OVERALL_COLUMN = "OverallColumn";
    public static final String STANDARD_PROFILE_NAME = "Стандартный";

    private final Class<TEntity> entityClass;
    private FormService formService;
    protected FilterTable table;
    protected Container container;
    private List<UIAction> actions;
    private GridDataDecl dataDecl;
    private Mode currentMode;
    private boolean toolbarVisible = true;
    private final List<MenuBar.MenuItem> needCurrentMenu = newArrayList();
    private final List<MenuBar.MenuItem> disallowInReadOnlyMenu = newArrayList();
    private MenuBar.MenuItem tableModeBtn;
    private MenuBar.MenuItem detailModeBtn;
    private Panel filterPanel;
    private GridLayout rootPanel;
    private boolean filterVisible = false;
    private final String gridIdSuffix;

    public String getGridIdSuffix() {
        return gridIdSuffix;
    }

    public void selectObject(final Object objectId) {
        if (table != null && objectId != null && table.containsId(objectId)) {
            table.unselect(objectId);
            table.select(objectId);
        }
    }

    public String getGridId() {
        return Joiner.on('.')
                .skipNulls()
                .join(getClass().getName(), gridIdSuffix);
    }

    public Container getContainer() {
        return container;
    }

    public void setFilterVisible(final boolean filterVisible) {
        this.filterVisible = filterVisible;
        showTableFilter(filterVisible);
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
                    grid.refreshContainerEntity(form.getEntity());
                }
                grid.selectEntity(form.getEntity());
            });
            FormUtils.showModalWin(form);
        }

        @Override
        public void open4Insert(final ExtaEditForm form) {
            form.addCloseFormListener(event -> {
                if (form.isSaved()) {
                    grid.refreshContainer();
                    grid.selectEntity(form.getEntity());
                }
            });
            FormUtils.showModalWin(form);
        }
    }

    public void selectEntity(final TEntity entity) {
        Object itemId = entity;
        if (container instanceof ExtaDbContainer)
            itemId = ((ExtaDbContainer) container).getEntityItemId((IdentifiedObject) entity);
        selectObject(itemId);
    }

    protected void refreshContainerEntity(final TEntity entity) {
        Object itemId = entity;
        if (container instanceof ExtaDbContainer)
            itemId = ((ExtaDbContainer) container).getEntityItemId((IdentifiedObject) entity);
        refreshContainerItem(itemId);
    }

    /**
     * <p>Constructor for ExtaGrid.</p>
     */
    public ExtaGrid(final Class<TEntity> entityClass) {
        this(entityClass, null);
    }

    public ExtaGrid(Class<TEntity> entityClass, String gridIdSuffix) {
        this.entityClass = entityClass;
        this.gridIdSuffix = gridIdSuffix;

        formService = new ModalPopupFormService(this);
        // Must set a dummy root in constructor
        setCompositionRoot(new Label(""));

        addAttachListener(e -> initialize());
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

    public Set<TEntity> getSelectedEntities() {
        final Set itemIds = getSelectedItemIds();
        return getEntities(itemIds);
    }

    public Set<TEntity> getRefreshedEntities(final Set itemIds) {
        refreshContainerItems(itemIds);
        return getEntities(itemIds);
    }

    public Set<TEntity> getEntities(final Set itemIds) {
        final Set<TEntity> entities = newHashSetWithExpectedSize(itemIds.size());
        itemIds.forEach(id -> entities.add(getEntity(id)));
        return entities;
    }

    public TEntity getEntity(final Object itemId) {
        return GridItem.extractBean(getItem(itemId));
    }

    public TEntity getFirstSelectedEntity() {
        final Set itemIds = getSelectedItemIds();
        return getFirstEntity(itemIds);
    }

    public Item getFirstItem(final Set itemIds) {
        return (Item) itemIds.stream().findFirst().map(id -> getItem(id)).orElse(null);
    }

    public TEntity getFirstEntity(final Set itemIds) {
        return (TEntity) itemIds.stream().findFirst().map(id -> getEntity(id)).orElse(null);
    }

    public TEntity getFirstRefreshedEntity(final Set itemIds) {
        return (TEntity) itemIds.stream().findFirst().map(id -> {
            refreshContainerItem(id);
            return getEntity(id);
        }).orElse(null);
    }

    public Set getSelectedItemIds() {
        if (table != null) {
            final Object tableValue = table.getValue();
            if (tableValue != null) {
                if (tableValue instanceof Set)
                    return (Set) tableValue;
                else
                    return newHashSet(tableValue);
            }
        }
        return newHashSet();
    }

    public Set<Item> getSelectedItems() {
        final Set itemIds = getSelectedItemIds();
        return getItems(itemIds);
    }

    public Set<Item> getItems(final Set itemIds) {
        final Set<Item> items = newHashSetWithExpectedSize(itemIds.size());
        itemIds.forEach(i -> items.add(getItem(i)));
        return items;
    }

    public Item getItem(final Object itemId) {
        return table.getItem(itemId);
    }

    public abstract ExtaEditForm<TEntity> createEditForm(TEntity entity, boolean isInsert);

    public void doEditObject(final TEntity entity) {

        final ExtaEditForm<TEntity> form = createEditForm(entity, false);
        form.setReadOnly(isReadOnly());
        if (!form.isReadOnly())
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

    public FilterTable getTable() {
        return table;
    }

    /**
     * <p>initialize.</p>
     */
    private void initialize() {
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

        rootPanel = new GridLayout(2, 3);
        rootPanel.setHideEmptyRowsAndColumns(true);
        rootPanel.setSizeFull();

        rootPanel.setRowExpandRatio(0, 0);
        rootPanel.setRowExpandRatio(1, 0);
        rootPanel.setRowExpandRatio(2, 1);
        rootPanel.setColumnExpandRatio(0, 1);
        rootPanel.setColumnExpandRatio(1, 0);
        rootPanel.setMargin(true);
        rootPanel.setSpacing(true);

        if (toolbarVisible) {
            // Формируем тулбар
            final MenuBar commandBar = createGridToolbar(mode);
            rootPanel.addComponent(commandBar, 0, 0);
            rootPanel.setComponentAlignment(commandBar, Alignment.TOP_LEFT);

            // Переключение режима таблицы
            final MenuBar modeSwitchBar = new MenuBar();
            modeSwitchBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            final MenuBar.MenuItem tableFilterBtn = modeSwitchBar.addItem("", Fontello.FILTER, selectedItem -> {
                showTableFilter(selectedItem.isChecked());
            });
            tableFilterBtn.setDescription("Показать строку фильтра таблицы");
            tableFilterBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            tableFilterBtn.setCheckable(true);
            tableFilterBtn.setChecked(filterVisible);

            final MenuBar.MenuItem refreshBtn = modeSwitchBar.addItem("", FontAwesome.REFRESH, s -> refreshContainer());
            refreshBtn.setDescription("Обновить данные в таблице");
            refreshBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);

            final boolean isAdmin = lookup(UserManagementService.class).isCurUserHasRole(UserRole.ADMIN);
            if (isAdmin) {
                if (SecuredObject.class.isAssignableFrom(entityClass)) {
                    final MenuBar.MenuItem accessBtn = modeSwitchBar.addItem("", Fontello.LOCK, s -> {
                        final Set itemIds = getSelectedItemIds();
                        refreshContainerItems(itemIds);
                        final SecuredObject securedObject = (SecuredObject) getFirstSelectedEntity();
                        final SecuritySettingsForm form = new SecuritySettingsForm("Настройки доступа объекта...", securedObject);
                        FormUtils.showModalWin(form);
                    });
                    accessBtn.setDescription("Показать настройки доступа для выделенного объекта");
                    accessBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
                    needCurrentMenu.add(accessBtn);
                    accessBtn.setEnabled(false);
                }

                if (isArchiveEnabled()) {
                    final MenuBar.MenuItem archiveMenu = modeSwitchBar.addItem("", FontAwesome.ARCHIVE, null);
                    archiveMenu.setDescription("Меню действий с архивом, позволяет управлять архивными записями");
                    archiveMenu.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);

                    final MenuBar.MenuItem showArchiveMenu = archiveMenu.addItem("Показать архивные записи", FontAwesome.HISTORY, null);
                    showArchiveMenu.setCommand(c -> {
                        if (c.isChecked()) {
                            showArchiveMenu.setText("Скрыть архивные записи");
                            ((ArchivedContainer) container).setArchiveExcluded(false);
                        } else {
                            showArchiveMenu.setText("Показать архивные записи");
                            ((ArchivedContainer) container).setArchiveExcluded(true);
                        }
                    });

                    showArchiveMenu.setDescription("Отображает или скрывает архивные записи в таблице");
                    showArchiveMenu.setCheckable(true);

                    final MenuBar.MenuItem toArchiveMenu = archiveMenu.addItem("Отправить в архив", FontAwesome.DOWNLOAD, c -> {
                        final Set<ArchivedObject> selectedEntities = (Set<ArchivedObject>) getSelectedEntities();
                        ConfirmDialog.show(UI.getCurrent(),
                                "Подтвердите действие...",
                                MessageFormat.format(
                                        "Вы уверены, что хотите преместить выделенные записи ({0}) в архив? " +
                                                "Архивные записи будут скрыты для всех пользователей. " +
                                                "Только администратор сможет в дальнейшем работать с этими записями. " +
                                                "Нажмите 'Да', чтобы выполнить архивирование.",
                                        selectedEntities.size()),
                                "Да", "Нет", () -> {
                                    lookup(ArchiveService.class).archive(selectedEntities);
                                    refreshContainer();
                                    NotificationUtil.showSuccess("Записи перенесены в архив");
                                });
                    });
                    toArchiveMenu.setDescription("Убрать выделенные записи в архив");
                    needCurrentMenu.add(toArchiveMenu);
                    toArchiveMenu.setEnabled(false);

                    final MenuBar.MenuItem fromArchiveMenu = archiveMenu.addItem("Достать из архива", FontAwesome.UPLOAD, c -> {
                        final Set<ArchivedObject> selectedEntities = (Set<ArchivedObject>) getSelectedEntities();
                        ConfirmDialog.show(UI.getCurrent(),
                                "Подтвердите действие...",
                                MessageFormat.format(
                                        "Вы уверены, что хотите извлечь выделенные записи ({0}) из архива? " +
                                                "Архивные записи будут извлечены из архива и доступны для пользователей. " +
                                                "Нажмите 'Да', чтобы извлечь записи из архива.",
                                        selectedEntities.size()),
                                "Да", "Нет", () -> {
                                    lookup(ArchiveService.class).extract(selectedEntities);
                                    refreshContainer();
                                    NotificationUtil.showSuccess("Записи возвращены из архива");
                                });
                    });
                    fromArchiveMenu.setDescription("Извлечь выделенные записи из архива");
                    needCurrentMenu.add(fromArchiveMenu);
                    fromArchiveMenu.setEnabled(false);
                }

                final MenuBar.MenuItem exportBtn = modeSwitchBar.addItem("", Fontello.FILE_EXCEL, s -> {
                    final CustomTableHolder tableHolder = new CustomTableHolder(table);
                    final ExcelExport excelExport = new MyExcelExport(tableHolder);
                    excelExport.excludeCollapsedColumns();
                    excelExport.setReportTitle(entityClass.getSimpleName());
                    final String fileName = MessageFormat.format("{1} {0}.xls",
                            new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()),
                            entityClass.getSimpleName());
                    excelExport.setExportFileName(fileName);
                    excelExport.convertTable();
                    try {
                        final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
                        excelExport.getWorkbook().write(outDoc);
                        new DownloadFileWindow(outDoc.toByteArray(), fileName).showModal();
                    } catch (final IOException e) {
                        logger.error("Converting to XLS failed with IOException ", e);
                        throw Throwables.propagate(e);
                    }
                });
                exportBtn.setDescription("Экспортировать данные таблицы в MS Excel");
                exportBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            }

//            final MenuBar.Command modeCommand = selectedItem -> {
//                if (selectedItem == tableModeBtn && currentMode == Mode.DETAIL_LIST) {
//                    setMode(Mode.TABLE);
//                } else if (currentMode == Mode.TABLE) {
//                    setMode(Mode.DETAIL_LIST);
//                }
//            };
//            tableModeBtn = modeSwitchBar.addItem("", Fontello.TABLE, modeCommand);
//            tableModeBtn.setDescription("Нажмите чтобы переключить список в стандартный табличный режим");
//            tableModeBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
//            tableModeBtn.setCheckable(true);
//
//            detailModeBtn = modeSwitchBar.addItem("", Fontello.LIST_ALT, modeCommand);
//            detailModeBtn.setDescription("Нажмите чтобы переключить список в детализированный режим");
//            detailModeBtn.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
//            detailModeBtn.setCheckable(true);
//
//            if (currentMode == Mode.TABLE) {
//                tableModeBtn.setChecked(true);
//                detailModeBtn.setChecked(false);
//            } else {
//                detailModeBtn.setChecked(true);
//                tableModeBtn.setChecked(false);
//            }
            rootPanel.addComponent(modeSwitchBar, 1, 0);
            rootPanel.setComponentAlignment(modeSwitchBar, Alignment.TOP_RIGHT);
        }

        // Таблица
        initTable(mode);
        rootPanel.addComponent(table, 0, 2, 1, 2);

        setCompositionRoot(rootPanel);
        setFilterVisible(filterVisible);
    }

    private void showTableFilter(final boolean filterVisible) {
        if (table != null) {
            if (filterPanel == null)
                createFilterPanel();
            filterPanel.setVisible(filterVisible);
        }
    }

    private void createFilterPanel() {
        // Панель фильтра
        if (filterPanel == null) {
            filterPanel = new ExtaGridFilterPanel(this);
            filterPanel.setVisible(false);
            rootPanel.addComponent(filterPanel, 0, 1, 1, 1);
        }
    }

    @Override
    public Object[] getColumns() {
        return table.getVisibleColumns();
    }

    @Override
    public boolean isFilteredColumn(final Object columnId) {
        return table.getColumnIdToFilterMap().containsKey(columnId);
    }

    @Override
    public Component getColumnComponent(final Object columnId) {
        return table.getColumnIdToFilterMap().get(columnId);
    }

    @Override
    public String getColumnHeader(final Object columnId) {
        return table.getColumnHeader(columnId);
    }

    @Override
    public List getDefaultFilterFields() {
        // Если список не задан, надо сформировать самостоятельно (три первых колонки)
        final List<Object> defFields = newArrayList();
        for (final Object columnId : getColumns()) {
            if (!"id".equals(columnId) && !table.isColumnCollapsed(columnId) && isFilteredColumn(columnId)) {
                defFields.add(columnId);
                if (defFields.size() == DEF_FIELDS_COUNT)
                    break;
            }
        }
        return defFields;
    }

    private boolean isArchiveEnabled() {
        return ArchivedObject.class.isAssignableFrom(entityClass);
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
                    final Set selectedIds = getSelectedItemIds();
                    refreshContainerItems(selectedIds);
                    checkState(!selectedIds.isEmpty(), "No selected row");
                    action.fire(selectedIds);
                } else
                    action.fire(null);

            };
            menuItem.setCommand(command);
        }
        if (action instanceof ItemAction) {
            needCurrentMenu.add(menuItem);
            menuItem.setEnabled(false);
        }

        if (!action.isAllowInReadOnly())
            disallowInReadOnlyMenu.add(menuItem);

        if (action instanceof ExtaGrid.NewObjectAction) {
            menuItem.setEnabled(GridUtils.isPermitInsert(container) && !isReadOnly());
        }
    }

    public void refreshContainerItems(final Set selectedIds) {
        selectedIds.forEach(id -> refreshContainerItem(id));
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        for (final MenuBar.MenuItem menuItem : disallowInReadOnlyMenu) {
            menuItem.setEnabled(!readOnly);
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
        // Поддержка фильтрации
        final CompositeFilterGenerator generator = new CompositeFilterGenerator();
        addDefaultFiltergenerators(generator);
        final FilterGenerator customGenerator = createFilterGenerator();
        if (customGenerator != null)
            generator.add(customGenerator);
        table.setFilterGenerator(generator);
        // Поддержка отображения описаний перечислений
        table.setFilterDecorator(createFilterDecorator());

        // Общие настройки таблицы
        table.setContainerDataSource(container);
        table.setSelectable(true);
        table.setMultiSelect(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
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
                    final Set selectedIds = getSelectedItemIds();
                    refreshContainerItems(selectedIds);
                    firedAction.fire(selectedIds);
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
            if (defAction != null) {
                table.addItemClickListener(event -> {
                    if (event.isDoubleClick()) {
                        final Object itemId = event.getItemId();
                        refreshContainerItem(itemId);
                        defAction.fire(newHashSet(itemId));
                    }
                });
            }
            for (final MenuBar.MenuItem btn : needCurrentMenu)
                btn.setVisible(true);
            // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
            table.addValueChangeListener(event -> {
                final boolean enableBtb = event.getProperty().getValue() != null;
                for (final MenuBar.MenuItem btn : needCurrentMenu)
                    btn.setEnabled(enableBtb);
            });

        }
//        table.setWrapFilters(true);
//        table.setFilterBarVisible(false);
//        table.addAttachListener(e -> table.setFilterBarVisible(false));

        // Задаем стиль для архивных записей
        if (isArchiveEnabled()) {
            table.setCellStyleGenerator((source, itemId, propertyId) -> {
                final ArchivedObject archivedObject = (ArchivedObject) getEntity(itemId);
                if (archivedObject.isArchived())
                    return "archived";
                else
                    return null;
            });
        }

        // Сохранение состояния таблицы
        new FilterTableState().extend(table, createFilterTableStateHandler());
    }

    private void addDefaultFiltergenerators(final CompositeFilterGenerator generator) {
        generator
                .with(new AbstractFilterGenerator() {
                    @Override
                    public Container.Filter generateFilter(final Object propertyId, final Field<?> originatingField) {
                        if (originatingField instanceof PastDateIntervalField) {
                            final Interval interval = (Interval) originatingField.getValue();
                            if (interval != null) {
                                final Class<?> type = container.getType(propertyId);
                                if (type == LocalDate.class)
                                    return new Between(propertyId,
                                            interval.getStart().toLocalDate(),
                                            interval.getEnd().toLocalDate());
                                else
                                    return new Between(propertyId,
                                            interval.getStart().withTimeAtStartOfDay(),
                                            interval.getEnd().withTime(23, 59, 59, 999));
                            }
                        }
                        return null;
                    }

                    @Override
                    public AbstractField<?> getCustomFilterComponent(final Object propertyId) {
                        final Class<?> type = container.getType(propertyId);
                        if (type == DateTime.class || type == LocalDate.class)
                            return new PastDateIntervalField("", "Нажмите для изменения временного интервала фильтра");
                        return null;
                    }
                })
                .with(new UserProfileFilterGenerator(AuditedObject_.createdBy.getName()))
                .with(new UserProfileFilterGenerator(AuditedObject_.lastModifiedBy.getName()));
    }

    protected FilterDecorator createFilterDecorator() {
        return new CommonFilterDecorator();
    }

    protected FilterGenerator createFilterGenerator() {
        return null;
    }

    private FilterTableStateHandler createFilterTableStateHandler() {
        return new FilterTableStateHandler() {
            @Override
            public void save(final FilterTableStateProfile profile) {

                final FilterTableStateProfile newProfile = TableUtils.createProfile(table, profile.getName());
                profile.getColumnInfos().clear();
                profile.getColumnInfos().addAll(newProfile.getColumnInfos());

                final ObjectMapper mapper = new ObjectMapper();
                final StringWriter profileJson = new StringWriter();
                try {
                    mapper.writeValue(profileJson, profile);
                } catch (final IOException e) {
                    Throwables.propagate(e);
                }
                lookup(UserGridStateService.class).saveState(getGridId(), profile.getName(), profileJson.toString());
                NotificationUtil.showSuccess("Профиль таблицы сохранен!");
            }

            @Override
            public void delete(final String profileName) {
                lookup(UserGridStateService.class).deleteState(getGridId(), profileName);
                NotificationUtil.showSuccess("Профиль таблицы удален!");
            }

            @Override
            public Set<FilterTableStateProfile> load() {
                final Set<FilterTableStateProfile> stateProfiles = newHashSet();

                // Извлекаем стандартный профиль (по умолчанию)
                final FilterTableStateProfile standardProfile = TableUtils.createProfile(table, STANDARD_PROFILE_NAME);
                stateProfiles.add(standardProfile);

                for (final UserGridState gridState : lookup(UserGridStateService.class).loadStates(getGridId())) {
                    final ObjectMapper mapper = new ObjectMapper();
                    try {
                        final FilterTableStateProfile profile = mapper.readValue(gridState.getState(), FilterTableStateProfile.class);
                        stateProfiles.add(profile);
                    } catch (final IOException e) {
                        Throwables.propagate(e);
                    }
                }

                return stateProfiles;
            }

            @Override
            public String getDefaultProfile() {
                final String profileName = lookup(UserGridStateService.class).getDefaultStateName(getGridId());
                return isNullOrEmpty(profileName) ? STANDARD_PROFILE_NAME : profileName;
            }

            @Override
            public void setDefaultProfile(final String profileName) {
                lookup(UserGridStateService.class).setDefaultState(getGridId(), profileName);
            }
        };
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
                    ((Button) command).addClickListener(event -> {
                        refreshContainerItem(itemId);
                        a.fire(newHashSet(itemId));
                    });
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
        if (container instanceof ExtaDbContainer)
            ((ExtaDbContainer) container).refresh();
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
        if (container instanceof ExtaDbContainer)
            ((ExtaDbContainer) container).refreshItem(itemId);
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
                titleLink.setCaption(item.getItemProperty(titleMap.getPropName()).getValue().toString());
                titleLink.setDescription(defAction.getDescription());
                titleLink.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                titleLink.addClickListener(event -> {
                    refreshContainerItem(itemId);
                    defAction.fire(newHashSet(itemId));
                });
                titleComp = titleLink;
            }
            titleComp.setImmediate(true);
            titleComp.addStyleName(ExtaTheme.MAIN_ITEM_TEXT);
            panel.addComponent(titleComp);

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
                if (event.isDoubleClick()) {
                    refreshContainerItem(itemId);
                    defAction.fire(newHashSet(itemId));
                }
            });
            panel.setImmediate(true);

            return panel;
        }
    }

    protected class NewObjectAction extends UIAction {
        public NewObjectAction(final String caption, final String description, final Fontello icon) {
            super(caption, description, icon, false);
        }

        public NewObjectAction(final String caption, final String description) {
            super(caption, description, Fontello.DOC_NEW, false);
        }

        @Override
        public void fire(final Set itemIds) {
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
        public void fire(final Set itemIds) {
            final Object itemId = getFirstItemId(itemIds);
            refreshContainerItem(itemId);
            final TEntity entity = getEntity(itemId);
            doEditObject(entity);
        }
    }

    protected Object getFirstItemId(final Set itemIds) {
        return itemIds.stream().findFirst().orElse(null);
    }

}
