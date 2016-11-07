package ru.extas.web.commons;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;


/**
 * Класс, реализующий универсальную панель фильтра грида
 *
 * @author Valery Orlov
 *         Date: 15.04.2016
 *         Time: 14:21
 */
@SuppressWarnings("unchecked")
class ExtaGridFilterPanel extends Panel {

    private final IFilterGrid grid;
    private final MenuBar.MenuItem addDropDownBtn;
    private final MenuBar menuBar;

    /**
     * Интерфейс получения данных от грида
     */
    interface IFilterGrid {

        static final int DEF_FIELDS_COUNT = 4;

        /**
         * Возвращает идентификаторы колонок по которым строится начальный фильтр
         *
         * @return список колонок
         */
        List getDefaultFilterFields();

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

    ExtaGridFilterPanel(final IFilterGrid grid) {
        super("Фильтр записей");
        this.grid = grid;

        setIcon(FontAwesome.FILTER);
        addStyleName(ExtaTheme.PANEL_WELL);

        final MVerticalLayout filterContent = new MVerticalLayout().withMargin(new MMarginInfo(false, true));
        setContent(filterContent);


        // Получить список полей фильтра по умолчанию
        final List defFields = grid.getDefaultFilterFields();

        // Формируем поля фильтра
        final MHorizontalLayout fields = new MHorizontalLayout() {
            @Override
            public void detach() {
                iterator().forEachRemaining(cw -> {
                    if (cw instanceof MHorizontalLayout) {
                        final Component c = ((MHorizontalLayout) cw).getComponent(0);
                        if (c.getParent() == null)
                            c.setParent((HasComponents) cw);
                    }
                });
                super.detach();
            }
        }.withStyleName(ExtaTheme.LAYOUT_HORIZONTAL_WRAPPING);
        for (final Object columnId : defFields) {
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

    private void addMenuItem(final MHorizontalLayout fields, final Object columnId) {
        final String columnHeader = grid.getColumnHeader(columnId);
        if (!isEmpty(columnHeader))
            addDropDownBtn.addItem(columnHeader,
                    item -> {
                        // Добавить поле в конец
                        addFilterField(fields, columnId);
                        // Удалить поле из меню
                        addDropDownBtn.removeChild(item);
                    });
    }

    private void addFilterField(final MHorizontalLayout fields, final Object columnId) {
        final MHorizontalLayout fieldWrapp = new MHorizontalLayout().withSpacing(false).withMargin(false);
        final Component field = grid.getColumnComponent(columnId);
        if (field != null) {
            field.setCaption(grid.getColumnHeader(columnId));
            field.addStyleName(ExtaTheme.TEXTFIELD_TINY);
            field.setParent(null);
            fieldWrapp.with(field);
            fieldWrapp.add(new MButton(FontAwesome.TRASH_O)
                    .withStyleName(ExtaTheme.BUTTON_TINY, ExtaTheme.BUTTON_BORDERLESS_COLORED)
                    .withDescription("Нажмите чтобы удалить поле из фильтра")
                    .withListener(event -> {
                        // Очищаем фильр по удаляемому полю
                        if (field instanceof Property)
                            ((Property) field).setValue(null);
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
