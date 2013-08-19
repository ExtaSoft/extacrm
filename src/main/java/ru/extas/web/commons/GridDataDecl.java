/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;
import ru.extas.web.commons.DataDeclMapping.PresentFlag;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Опции отрбражения датасета в таблице:
 * <ul>
 * <li>порядок</li>
 * <li>заголовок</li>
 * <li>доступность</li>
 * <li>видимость</li>
 * </ul>
 *
 * @author Valery Orlov
 */
public class GridDataDecl {

    private final List<DataDeclMapping> mappings = new ArrayList<>();

    /**
     *
     */
    protected GridDataDecl() {
        super();
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param presentFlags параметры отображения
     */
    protected void addMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags) {
        mappings.add(new DataDeclMapping(propName, caption, presentFlags));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName - Имя свойства
     * @param caption  - Заголовок столбца
     */
    protected void addMapping(String propName, String caption) {
        mappings.add(new DataDeclMapping(propName, caption));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param converterCls
     */
    public void addMapping(String propName, String caption, Class<? extends Converter<String, ?>> converterCls) {
        mappings.add(new DataDeclMapping(propName, caption, lookup(converterCls)));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param presentFlags - параметры отображения
     * @param converterCls - конвертер значения
     */
    public void addMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags, Class<? extends Converter<String, ?>> converterCls) {
        mappings.add(new DataDeclMapping(propName, caption, presentFlags, lookup(converterCls)));
    }

    /**
     * Устанавливает заголовки столбцов таблицы
     *
     * @param table таблица
     */
    void setTableColumnHeaders(Table table) {

        for (DataDeclMapping prop : mappings)
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Задает видимые в таблице столбци
     *
     * @param table - таблица
     */
    void setTableVisibleColumns(Table table) {

        List<String> clmnIds = new ArrayList<>(mappings.size());
        for (DataDeclMapping prop : mappings)
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     *
     * @param table - таблица
     */
    void setTableCollapsedColumns(Table table) {

        for (DataDeclMapping prop : mappings)
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }

    /**
     * Полноценная инициализация колонок таблицы
     *
     * @param table - таблица
     */
    public void initTableColumns(Table table) {
        // Общие настройки таблицы
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);

        // Настройка столбцов таблицы
        setTableColumnHeaders(table);
        setTableVisibleColumns(table);
        setTableCollapsedColumns(table);
        setTableColumnConverters(table);

    }

    /**
     * Устанавливает конвертеры столбцов таблицы
     *
     * @param table
     */
    void setTableColumnConverters(Table table) {
        for (DataDeclMapping prop : mappings)
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Добавляет маркеры создания/модификации записи
     */
    protected void addCreateModifyMarkers() {
        addMapping("modifiedBy", "Кто изменил", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("modifiedAt", "Когда изменил", EnumSet.of(PresentFlag.COLLAPSED)/*
                                                                                     * ,
																					 * StringToJodaDTConverter
																					 * .
																					 * class
																					 */);
        addMapping("createdBy", "Кто создал", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("createdAt", "Когда создал", EnumSet.of(PresentFlag.COLLAPSED)/*
                                                                                 * ,
																				 * StringToJodaDTConverter
																				 * .
																				 * class
																				 */);
    }

}