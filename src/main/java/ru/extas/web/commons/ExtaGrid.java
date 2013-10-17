package ru.extas.web.commons;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
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

    protected final Table table;
    protected final Container container;
    private final List<UIAction> actions;
    private final GridDataDecl dataDecl;

    public ExtaGrid() {
        super();
        // Создаем таблицу скроллинга
        table = new Table();
        // Запрос данных
        container = createContainer();
        // Действия в таблице
        actions = createActions();
        // Описатель данных
        dataDecl = createDataDecl();

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Формируем тулбар
        final HorizontalLayout commandBar = createGridToolbar();
        panel.addComponent(commandBar);

        // Таблица
        initTable();
        panel.addComponent(table);

        setCompositionRoot(panel);
    }

    private HorizontalLayout createGridToolbar() {
        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("grid-toolbar");
        commandBar.setSpacing(true);

        final List<Button> needCurrent = newArrayList();
        for (final UIAction action : actions) {
            final Button button = new Button(action.getName());
            button.setDescription(action.getDescription());
            button.addStyleName(action.getIconStyle());

            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    button.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            if (action instanceof ItemAction)
                                action.fire(checkNotNull(table.getValue(), "No selected row"));
                            else
                                action.fire(null);
                        }
                    });
                }
            });
            commandBar.addComponent(button);
            if (action instanceof ItemAction) {
                needCurrent.add(button);
                button.setEnabled(false);
            }
        }
        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                for (Button btn : needCurrent)
                    btn.setEnabled(enableBtb);
            }
        });
        return commandBar;
    }

    /**
     * Полноценная инициализация колонок таблицы
     */
    private void initTable() {
        // Общие настройки таблицы
        table.setContainerDataSource(container);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
        table.setSizeFull();
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

        // Настройка столбцов таблицы
//        initTableColumnHeaders(table);
//        initTableVisibleColumns(table);
//        initTableCollapsedColumns(table);
//        initTableColumnConverters(table);

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
                final HorizontalLayout header = new HorizontalLayout(titleComp, createItemToolbar(itemId));
                header.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
                panel.addComponent(header);

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
                //HorizontalLayout actionToolbar = createItemToolbar(itemId);
                //panel.addComponent(actionToolbar);

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
    }

    private HorizontalLayout createItemToolbar(final Object itemId) {
        HorizontalLayout actionToolbar = new HorizontalLayout();
        actionToolbar.addStyleName("item-toolbar");
        actionToolbar.setSpacing(true);
        actionToolbar.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        for (final UIAction a : actions)
            if (a instanceof ItemAction) {
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
