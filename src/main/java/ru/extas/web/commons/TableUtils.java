package ru.extas.web.commons;

import com.vaadin.addon.tableexport.CustomTableExportableColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Table;
import org.tepi.filtertable.FilterTable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>TableUtils class.</p>
 *
 * @author Valery Orlov
 *         Date: 09.12.13
 *         Time: 12:53
 * @version $Id: $Id
 * @since 0.3
 */
public class TableUtils {

    /**
     * Инициализирует столбцы таблицы в соответствии с метаописанием
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumns(final Table table, final GridDataDecl dataDecl) {
        initTableColumnHeaders(table, dataDecl);
        initTableVisibleColumns(table, dataDecl);
        initTableCollapsedColumns(table, dataDecl);
        initTableColumnConverters(table, dataDecl);
    }


    /**
     * Устанавливает конвертеры столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnConverters(final Table table, final GridDataDecl dataDecl) {
        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Устанавливает конвертеры столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnConverters(final FilterTable table, final GridDataDecl dataDecl) {
        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Устанавливает заголовки столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnHeaders(final Table table, final GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Устанавливает заголовки столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnHeaders(final FilterTable table, final GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Задает видимые в таблице столбци
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableVisibleColumns(final Table table, final GridDataDecl dataDecl) {

        final List<String> clmnIds = new ArrayList<>(dataDecl.getMappings().size());
        for (final DataDeclMapping prop : dataDecl.getMappings())
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Задает видимые в таблице столбци
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableVisibleColumns(final FilterTable table, final GridDataDecl dataDecl) {

        final List<String> clmnIds = new ArrayList<>(dataDecl.getMappings().size());
        for (final DataDeclMapping prop : dataDecl.getMappings())
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableCollapsedColumns(final Table table, final GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableCollapsedColumns(final FilterTable table, final GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }

    /**
     * Полностью инициализирует таблицу в соответствии с метаописанием
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void fullInitTable(final Table table, final GridDataDecl dataDecl) {
        addGeneratedColumns(table, dataDecl);
        initTableColumnHeaders(table, dataDecl);
        initTableVisibleColumns(table, dataDecl);
        initTableCollapsedColumns(table, dataDecl);
        initTableColumnConverters(table, dataDecl);
    }

    private static void addGeneratedColumns(Table table, GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getGenerator() != null)
                table.addGeneratedColumn(prop.getPropName(), new Table.ColumnGenerator() {
                    @Override
                    public Object generateCell(Table source, Object itemId, Object columnId) {
                        Item item = source.getItem(itemId);
                        return prop.getGenerator().generateCell(columnId, item);
                    }
                });
    }

    /**
     * Полностью инициализирует таблицу в соответствии с метаописанием
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void fullInitTable(FilterTable table, GridDataDecl dataDecl) {
        addGeneratedColumns(table, dataDecl);
        initTableColumnHeaders(table, dataDecl);
        initTableVisibleColumns(table, dataDecl);
        initTableCollapsedColumns(table, dataDecl);
        initTableColumnConverters(table, dataDecl);
    }

    private static void addGeneratedColumns(final FilterTable table, GridDataDecl dataDecl) {
        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getGenerator() != null)
                table.addGeneratedColumn(prop.getPropName(), new CustomTableExportableColumnGenerator() {
                    @Override
                    public Object generateCell(CustomTable source, Object itemId, Object columnId) {
                        Item item = source.getItem(itemId);
                        return prop.getGenerator().generateCell(columnId, item);
                    }

                    @Override
                    public Property getGeneratedProperty(Object itemId, Object columnId) {
                        Item item = table.getItem(itemId);
                        return prop.getGenerator().getCellProperty(columnId, item);
                    }

                    @Override
                    public Class<?> getType() {
                        return prop.getGenerator().getType();
                    }
                });

    }

}
