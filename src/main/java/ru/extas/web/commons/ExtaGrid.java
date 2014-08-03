package ru.extas.web.commons;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;

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
public abstract class ExtaGrid extends CustomComponent {
    private static final long serialVersionUID = 2299363623807745654L;

    /**
     * Constant <code>OVERALL_COLUMN="OverallColumn"</code>
     */
    public static final String OVERALL_COLUMN = "OverallColumn";

    protected FilterTable table;
    protected Container container;
    private List<UIAction> actions;
    private GridDataDecl dataDecl;
    private Mode currentMode;
    private List<MenuBar.MenuItem> needCurrentBtns;
    private MenuBar.MenuItem tableModeBtn;
    private MenuBar.MenuItem detailModeBtn;

    public enum Mode {
        TABLE,
        DETAIL_LIST
    }

    /**
     * <p>Constructor for ExtaGrid.</p>
     *
     * @param initNow a boolean.
     */
    public ExtaGrid(boolean initNow) {
        if (initNow)
            initialize();
    }

    /**
     * <p>Constructor for ExtaGrid.</p>
     */
    public ExtaGrid() {
        this(true);
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

    private void initContent(Mode mode) {
        currentMode = mode;
        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Формируем тулбар
        final MenuBar commandBar = createGridToolbar(mode);
        panel.addComponent(commandBar);

        // Переключение режима таблицы
        final MenuBar modeSwitchBar = new MenuBar();
        modeSwitchBar.addStyleName("borderless");
        modeSwitchBar.addStyleName("mode-switch-bar");
        final MenuBar.MenuItem tableFilterBtn = modeSwitchBar.addItem("", Fontello.FILTER0, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                table.setFilterBarVisible(selectedItem.isChecked());
            }
        });
        tableFilterBtn.setDescription("Показать строку фильтра таблицы");
        tableFilterBtn.setStyleName("icon-only");
        tableFilterBtn.setCheckable(true);

        MenuBar.Command modeCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if (selectedItem == tableModeBtn && currentMode == Mode.DETAIL_LIST) {
                    initContent(Mode.TABLE);
                    detailModeBtn.setChecked(false);
                } else if (currentMode == Mode.TABLE) {
                    initContent(Mode.DETAIL_LIST);
                    tableModeBtn.setChecked(false);
                }
            }
        };
        tableModeBtn = modeSwitchBar.addItem("", Fontello.TABLE, modeCommand);
        tableModeBtn.setDescription("Нажмите чтобы переключить список в стандартный табличный режим");
        tableModeBtn.setStyleName("icon-only");
        tableModeBtn.setCheckable(true);

        detailModeBtn = modeSwitchBar.addItem("", Fontello.LIST_ALT, modeCommand);
        detailModeBtn.setDescription("Нажмите чтобы переключить список в детализированный режим");
        detailModeBtn.setStyleName("icon-only");
        detailModeBtn.setCheckable(true);

        if (currentMode == Mode.TABLE) {
            tableModeBtn.setChecked(true);
            detailModeBtn.setChecked(false);
        } else {
            detailModeBtn.setChecked(true);
            tableModeBtn.setChecked(false);
        }
        panel.addComponent(modeSwitchBar);

        // Таблица
        initTable(mode);
        panel.addComponent(table);

        setCompositionRoot(panel);
    }

    private MenuBar createGridToolbar(Mode mode) {
        final MenuBar commandBar = new MenuBar();
        commandBar.addStyleName("grid-toolbar");
        commandBar.addStyleName("borderless");
        commandBar.focus();

        needCurrentBtns = newArrayList();
        for (final UIAction action : actions) {
            final MenuBar.MenuItem menuItem = commandBar.addItem(action.getName(), action.getIcon(), null);
            fillGridTollbarItem(action, menuItem);
        }
        return commandBar;
    }

    private void fillGridTollbarItem(final UIAction action, MenuBar.MenuItem menuItem) {
        menuItem.setDescription(action.getDescription());

        if(action instanceof UIActionGroup) {
            for (final UIAction subAction : (((UIActionGroup) action).getActionsGroup())) {
                fillGridTollbarItem(subAction, menuItem.addItem(subAction.getName(), subAction.getIcon(), null));
            }
        } else {
            MenuBar.Command command = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    if (action instanceof ItemAction) {
                        Object item = table.getValue();
                        refreshContainerItem(item);
                        action.fire(checkNotNull(item, "No selected row"));
                    } else
                        action.fire(null);

                }
            };
            menuItem.setCommand(command);
        }
        if (action instanceof ItemAction) {
            needCurrentBtns.add(menuItem);
            menuItem.setEnabled(false);
        }
    }

    /**
     * Полноценная инициализация колонок таблицы
     *
     * @param mode a {@link ru.extas.web.commons.ExtaGrid.Mode} object.
     */
    protected void initTable(Mode mode) {

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

        // Ищем действие по умолчанию
        final UIAction defAction = getDefaultAction(actions);
        if (defAction != null) {
            table.addActionHandler(new Action.Handler() {
                @Override
                public Action[] getActions(Object target, Object sender) {
                    Action[] actionsArr = new Action[actions.size()];
                    int i = 0;
                    for (final UIAction a : actions) {
                        actionsArr[i] = new Action(a.getName());
                        i++;
                    }
                    return actionsArr;
                }

                @Override
                public void handleAction(final Action action, Object sender, Object target) {
                    UIAction firedAction = Iterables.find(actions, new Predicate<UIAction>() {
                        @Override
                        public boolean apply(UIAction input) {
                            return input.getName().equals(action.getCaption());
                        }
                    });
                    firedAction.fire(table.getValue());
                }
            });
        }

        if (mode == Mode.DETAIL_LIST) {
            table.setColumnHeaderMode(CustomTable.ColumnHeaderMode.HIDDEN);
            table.addGeneratedColumn(OVERALL_COLUMN, new CustomTable.ColumnGenerator() {
                @Override
                public Object generateCell(final CustomTable source, final Object itemId, Object columnId) {
                    Item item = source.getItem(itemId);

                    Iterator<DataDeclMapping> mapIterator = dataDecl.getMappings().iterator();
                    DataDeclMapping titleMap = mapIterator.next();
                    VerticalLayout panel = new VerticalLayout();

                    // Основная строка данных
                    AbstractComponent titleComp;
                    if (defAction == null) {
                        Label titleLbl = new Label(item.getItemProperty(titleMap.getPropName()));
                        if (titleMap.getConverter() != null)
                            titleLbl.setConverter(titleMap.getConverter());
                        titleLbl.setDescription(titleMap.getCaption());
                        titleComp = titleLbl;
                    } else {
                        Button titleLink = new Button();
                        titleLink.addStyleName("link");
                        titleLink.setCaption((String) item.getItemProperty(titleMap.getPropName()).getValue());
                        titleLink.setDescription(defAction.getDescription());
                        titleLink.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                        titleLink.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                defAction.fire(itemId);
                            }
                        });
                        titleComp = titleLink;
                    }
                    titleComp.setImmediate(true);
                    titleComp.addStyleName("main-item-text");
                    panel.addComponent(titleComp);
//                final HorizontalLayout header = new HorizontalLayout(titleComp, createItemToolbar(itemId));
//                header.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
//                panel.addComponent(header);

                    // Дополнительные данные
                    HorizontalLayout details = new HorizontalLayout();
                    details.setSpacing(true);
                    while (mapIterator.hasNext()) {
                        DataDeclMapping prop = mapIterator.next();
                        if (!prop.isCollapsed()) {
                            Label detail = new Label(item.getItemProperty(prop.getPropName()));
                            detail.addStyleName("h3");
                            detail.setDescription(prop.getCaption());
                            details.addComponent(detail);
                        }
                    }
                    panel.addComponent(details);

                    // Кнопки действий
                    HorizontalLayout actionToolbar = createItemToolbar(itemId);
                    if (actionToolbar.getComponentCount() > 0)
                        panel.addComponent(actionToolbar);

                    // Forward clicks on the layout as selection
                    // in the table
                    panel.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                        @Override
                        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                            source.select(itemId);
                            if (event.isDoubleClick())
                                defAction.fire(itemId);
                        }
                    });
                    panel.setImmediate(true);

                    return panel;
                }
            });
            table.setColumnHeader(OVERALL_COLUMN, "Общая информация");
            table.setVisibleColumns(OVERALL_COLUMN);
            for (MenuBar.MenuItem btn : needCurrentBtns)
                btn.setVisible(false);
        } else { // Classic table
            // Настройка столбцов таблицы
            table.setColumnHeaderMode(CustomTable.ColumnHeaderMode.EXPLICIT);
            //table.removecolutable.getVisibleColumns()
            fullInitTable(table, dataDecl);
            table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
                @Override
                public void itemClick(ItemClickEvent event) {
                    if (event.isDoubleClick())
                        defAction.fire(event.getItemId());
                }
            });
            for (MenuBar.MenuItem btn : needCurrentBtns)
                btn.setVisible(true);
            // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
            table.addValueChangeListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(final Property.ValueChangeEvent event) {
                    final boolean enableBtb = event.getProperty().getValue() != null;
                    for (MenuBar.MenuItem btn : needCurrentBtns)
                        btn.setEnabled(enableBtb);
                }
            });

        }
    }

    private HorizontalLayout createItemToolbar(final Object itemId) {
        HorizontalLayout actionToolbar = new HorizontalLayout();
        actionToolbar.addStyleName("item-toolbar");
        actionToolbar.setSpacing(true);
        actionToolbar.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        for (final UIAction a : actions)
            if (a instanceof ItemAction && !(a instanceof DefaultAction)) {
                Component command = a.createButton();
                if (command instanceof Button) {
                    ((Button)command).addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            a.fire(itemId);
                        }
                    });
                }
                actionToolbar.addComponent(command);
            }
        return actionToolbar;
    }

    private UIAction getDefaultAction(List<UIAction> actions) {
        UIAction defAction = null;
        for (UIAction a : actions)
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
        if (container instanceof ExtaDataContainer)
            ((ExtaDataContainer) container).refresh();
        else if (container instanceof RefreshBeanContainer)
            ((RefreshBeanContainer) container).refreshItems();
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
}
