package ru.extas.web.commons;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Класс, реализующий универсальную панель фильтра грида
 *
 * @author Valery Orlov
 *         Date: 15.04.2016
 *         Time: 14:21
 */
@SuppressWarnings("unchecked")
class ExtaGridFilterPanel extends Panel {

    private static final int DEF_FIELDS_COUNT = 4;
    private final IFilterGrid grid;
    private final MenuBar.MenuItem addDropDownBtn;
    private final MenuBar menuBar;

    /**
     * Интерфейс получения данных от грида
     */
    interface IFilterGrid {

        /**
         * Возвращает идентификаторы колонок по которым строится начальный фильтр
         *
         * @return список колонок
         */
        default List getDefaultFilterFields() {
            return null;
        }

        /**
         * Возвращает идентификаторы столбцов таблици
         *
         * @return массив столбцов
         */
        Object[] getColumns();

        /**
         * Позволяет выяснить фильтруемый ли столбец
         *
         * @param columnId идентификатор столбца
         * @return true если столбец фильтруемый
         */
        boolean isFilteredColumn(Object columnId);

        /**
         * Возвращает компонент ввода для фильтра
         *
         * @param columnId идентификатор колонки для которой ищется поле
         * @return компонент ввода для фильтра или null
         */
        Component getColumnComponent(Object columnId);

        /**
         * Возвращает описание колонки
         *
         * @param columnId идентификатор колонки
         * @return описание колонки
         */
        String getColumnHeader(Object columnId);
    }

    ExtaGridFilterPanel(IFilterGrid grid) {
        super("Фильтр записей");
        this.grid = grid;

        setIcon(FontAwesome.FILTER);
        addStyleName(ExtaTheme.PANEL_WELL);

        final MVerticalLayout filterContent = new MVerticalLayout().withMargin(new MMarginInfo(false, true));
        setContent(filterContent);


        // Получить список полей фильтра по умолчанию
        List defFields = grid.getDefaultFilterFields();
        // Если список не задан, надо сформировать самостоятельно (три первых колонки)
        if (defFields == null || defFields.isEmpty()) {
            defFields = newArrayList();
            for (Object columnId : grid.getColumns()) {
                if (!"id".equals(columnId) && grid.isFilteredColumn(columnId)) {
                    defFields.add(columnId);
                    if (defFields.size() == DEF_FIELDS_COUNT)
                        break;
                }
            }
        }

        // Формируем поля фильтра
        final MHorizontalLayout fields = new MHorizontalLayout().withStyleName(ExtaTheme.LAYOUT_HORIZONTAL_WRAPPING);
        for (Object columnId : defFields) {
            addFilterField(fields, columnId);
        }
        // Последним добавляем кнопку "добавить"
        menuBar = new MenuBar();
        menuBar.addStyleName(ExtaTheme.MENUBAR_SMALL);
        menuBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
        addDropDownBtn = menuBar.addItem("Добавить", FontAwesome.PLUS, null);
        addDropDownBtn.setDescription("Нажмите чтобы добавить поле фильтра");
        for (final Object columnId : grid.getColumns()) {
            if (grid.isFilteredColumn(columnId) && !defFields.contains(columnId)) {
                addMenuItem(fields, columnId);
            }
        }
        fields.add(menuBar, Alignment.BOTTOM_LEFT);

        filterContent.with(fields);
    }

    private void addMenuItem(MHorizontalLayout fields, Object columnId) {
        addDropDownBtn.addItem(grid.getColumnHeader(columnId),
                item -> {
                    // Добавить поле в конец
                    addFilterField(fields, columnId);
                    // Удалить поле из меню
                    addDropDownBtn.removeChild(item);
                });
    }

    private void addFilterField(MHorizontalLayout fields, Object columnId) {
        final MHorizontalLayout fieldWrapp = new MHorizontalLayout().withSpacing(false).withMargin(false);
        Component field = grid.getColumnComponent(columnId);
        if (field != null) {
            field.setCaption(grid.getColumnHeader(columnId));
            field.addStyleName(ExtaTheme.TEXTFIELD_TINY);
            fieldWrapp.with(field);
            fieldWrapp.add(new MButton(FontAwesome.TRASH_O)
                            .withStyleName(ExtaTheme.BUTTON_TINY, ExtaTheme.BUTTON_BORDERLESS_COLORED)
                            .withDescription("Нажмите чтобы удалить поле из фильтра")
                            .withListener(event -> {
                                // Удаляем компонент
                                fields.removeComponent(fieldWrapp);
                                // Добавляем поле в меню
                                addMenuItem(fields, columnId);
                            }), Alignment.BOTTOM_LEFT);
            if (fields.getComponentIndex(menuBar) == -1)
                fields.addComponent(fieldWrapp);
            else
                fields.addComponent(fieldWrapp, fields.getComponentIndex(menuBar));

        }
    }
}
