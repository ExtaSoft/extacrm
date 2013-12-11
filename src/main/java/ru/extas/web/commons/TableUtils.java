package ru.extas.web.commons;

import com.vaadin.ui.Table;
import org.tepi.filtertable.FilterTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Orlov
 *         Date: 09.12.13
 *         Time: 12:53
 */
public class TableUtils {

    /**
     * Инициализирует столбцы таблицы в соответствии с метаописанием
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumns(Table table, GridDataDecl dataDecl) {
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
    public static void initTableColumnConverters(Table table, GridDataDecl dataDecl) {
        for (DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Устанавливает конвертеры столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnConverters(FilterTable table, GridDataDecl dataDecl) {
        for (DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getConverter() != null)
                table.setConverter(prop.getPropName(), prop.getConverter());
    }

    /**
     * Устанавливает заголовки столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnHeaders(Table table, GridDataDecl dataDecl) {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Устанавливает заголовки столбцов таблицы
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableColumnHeaders(FilterTable table, GridDataDecl dataDecl) {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnHeader(prop.getPropName(), prop.getCaption());

    }

    /**
     * Задает видимые в таблице столбци
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableVisibleColumns(Table table, GridDataDecl dataDecl) {

        List<String> clmnIds = new ArrayList<>(dataDecl.getMappings().size());
        for (DataDeclMapping prop : dataDecl.getMappings())
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Задает видимые в таблице столбци
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableVisibleColumns(FilterTable table, GridDataDecl dataDecl) {

        List<String> clmnIds = new ArrayList<>(dataDecl.getMappings().size());
        for (DataDeclMapping prop : dataDecl.getMappings())
            clmnIds.add(prop.getPropName());

        table.setVisibleColumns(clmnIds.toArray());
    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableCollapsedColumns(Table table, GridDataDecl dataDecl) {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }

    /**
     * Устанавливает столбцы доступные в таблице, но свернутые
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void initTableCollapsedColumns(FilterTable table, GridDataDecl dataDecl) {

        for (DataDeclMapping prop : dataDecl.getMappings())
            table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

    }


}
