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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 17:48
 */
public abstract class ExtaGrid extends CustomComponent {
    private static final long serialVersionUID = 2299363623807745654L;

    public static final String OVERALL_COLUMN = "OverallColumn";

    protected Table table;
    protected Container container;
    private List<UIAction> actions;
    private GridDataDecl dataDecl;
    private Mode currentMode;
    private List<Button> needCurrentBtns;

    public enum Mode {
        TABLE,
        DETAIL_LIST
    }

    public ExtaGrid(boolean initNow) {
        if (initNow)
            initialize();
    }

    public ExtaGrid() {
        this(true);
    }

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
        final HorizontalLayout commandBar = createGridToolbar(mode);
        panel.addComponent(commandBar);

        // Переключение режима таблицы
        final HorizontalLayout modeSwitchBar = new HorizontalLayout();
        modeSwitchBar.addStyleName("mode-switch-bar segment");
        final Button tableModeBtn = new Button("Таблица");
        tableModeBtn.setDescription("Нажмите чтобы переключить список в стандартный табличный режим");
        tableModeBtn.addStyleName("first icon-table icon-only");
        final Button detailModeBtn = new Button("Детали");
        detailModeBtn.setDescription("Нажмите чтобы переключить список в детализированный режим");
        detailModeBtn.addStyleName("last icon-list-alt icon-only");
        if (currentMode == Mode.TABLE)
            tableModeBtn.addStyleName("down");
        else
            detailModeBtn.addStyleName("down");
        final Button.ClickListener switchListener = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (event.getButton() == tableModeBtn && currentMode == Mode.DETAIL_LIST) {
                    initContent(Mode.TABLE);
                    tableModeBtn.addStyleName("down");
                    detailModeBtn.removeStyleName("down");
                } else if (currentMode == Mode.TABLE) {
                    initContent(Mode.DETAIL_LIST);
                    tableModeBtn.removeStyleName("down");
                    detailModeBtn.addStyleName("down");
                }
            }
        };
        tableModeBtn.addClickListener(switchListener);
        detailModeBtn.addClickListener(switchListener);
        modeSwitchBar.addComponent(tableModeBtn);
        modeSwitchBar.addComponent(detailModeBtn);
        panel.addComponent(modeSwitchBar);

        // Таблица
        initTable(mode);
        panel.addComponent(table);

        setCompositionRoot(panel);
    }

    private HorizontalLayout createGridToolbar(Mode mode) {
        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("grid-toolbar");
        commandBar.setSpacing(true);

        needCurrentBtns = newArrayList();
        for (final UIAction action : actions) {
            final Button button = new Button(action.getName());
            button.setDescription(action.getDescription());
            button.addStyleName(action.getIconStyle());

            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    if (action instanceof ItemAction)
                        action.fire(checkNotNull(table.getValue(), "No selected row"));
                    else
                        action.fire(null);
                }
            });
            if (commandBar.getComponentCount() == 0)
                button.focus(); // фокус на первую кнопку
            commandBar.addComponent(button);
            if (action instanceof ItemAction) {
                needCurrentBtns.add(button);
                button.setEnabled(false);
            }
        }
        return commandBar;
    }

    /**
     * Полноценная инициализация колонок таблицы
     *
     * @param mode
     */
    private void initTable(Mode mode) {

        // Создаем таблицу
        table = new Table();
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
            table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
            table.addGeneratedColumn(OVERALL_COLUMN, new Table.ColumnGenerator() {
                @Override
                public Object generateCell(final Table source, final Object itemId, Object columnId) {
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
            for (Button btn : needCurrentBtns)
                btn.setVisible(false);
        } else { // Classic table
            // Настройка столбцов таблицы
            table.setColumnHeaderMode(Table.ColumnHeaderMode.EXPLICIT);
            //table.removecolutable.getVisibleColumns()
            initTableColumnHeaders();
            initTableVisibleColumns();
            initTableCollapsedColumns();
            initTableColumnConverters();
            table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
                @Override
                public void itemClick(ItemClickEvent event) {
                    if (event.isDoubleClick())
                        defAction.fire(event.getItemId());
                }
            });
            for (Button btn : needCurrentBtns)
                btn.setVisible(true);
            // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
            table.addValueChangeListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(final Property.ValueChangeEvent event) {
                    final boolean enableBtb = event.getProperty().getValue() != null;
                    for (Button btn : needCurrentBtns)
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
                Button command = new Button();
                command.addStyleName("item-action");
                command.setCaption(a.getName());
                command.setDescription(a.getDescription());
                command.addStyleName(a.getIconStyle());
                command.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        a.fire(itemId);
                    }
                });
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
     * Устанавливает конвертеры столбцов таблицы
     */
    private void initTableColumnConverters() {
        for (DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Устанавливает заголовки столбцов таблицы
     */
    private void initTableColumnHeaders() {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Задает видимые в таблице столбци
     */
    private void initTableVisibleColumns() {

        List<String> clmnIds = new ArrayList<>(dataDecl.getMappings().size());
        for (DataDeclMapping prop : dataDecl.getMappings())
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     */
    private void initTableCollapsedColumns() {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }


    protected abstract GridDataDecl createDataDecl();

    protected abstract Container createContainer();

    protected abstract List<UIAction> createActions();
}
