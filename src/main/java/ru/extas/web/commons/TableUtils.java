package ru.extas.web.commons;

import com.vaadin.addon.tableexport.CustomTableExportableColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Table;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.ColumnInfo;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.SortOrder;
import org.tepi.filtertable.FilterTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static void addGeneratedColumns(final Table table, final GridDataDecl dataDecl) {

        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getGenerator() != null)
                table.addGeneratedColumn(prop.getPropName(), (source, itemId, columnId) -> {
                    final Item item = source.getItem(itemId);
                    return prop.getGenerator().generateCell(columnId, item, itemId);
                });
    }

    /**
     * Полностью инициализирует таблицу в соответствии с метаописанием
     *
     * @param table    таблица
     * @param dataDecl метаописание столбцов
     */
    public static void fullInitTable(final FilterTable table, final GridDataDecl dataDecl) {
        addGeneratedColumns(table, dataDecl);
        initTableColumnHeaders(table, dataDecl);
        initTableVisibleColumns(table, dataDecl);
        initTableCollapsedColumns(table, dataDecl);
        initTableColumnConverters(table, dataDecl);
    }

    private static void addGeneratedColumns(final FilterTable table, final GridDataDecl dataDecl) {
        for (final DataDeclMapping prop : dataDecl.getMappings())
            if (prop.getGenerator() != null)
                table.addGeneratedColumn(prop.getPropName(), new CustomTableExportableColumnGenerator() {
                    @Override
                    public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
                        final Item item = source.getItem(itemId);
                        return prop.getGenerator().generateCell(columnId, item, itemId);
                    }

                    @Override
                    public Property getGeneratedProperty(final Object itemId, final Object columnId) {
                        final Item item = table.getItem(itemId);
                        return prop.getGenerator().getCellProperty(columnId, item);
                    }

                    @Override
                    public Class<?> getType() {
                        return prop.getGenerator().getType();
                    }
                });

    }

    public static FilterTableStateProfile createProfile(final FilterTable filterTable, final String profileName) {

        final Set<ColumnInfo> columnInfos = new HashSet<>();
        addVisibleColumns(columnInfos, filterTable);

        return new FilterTableStateProfile(profileName, columnInfos);
    }

    private static int getColumnIndex(final FilterTable filterTable, final Object propertyId) {
        for (int i = 0; i < filterTable.getVisibleColumns().length; i++) {
            if (filterTable.getVisibleColumns()[i].equals(propertyId)) {
                return i;
            }
        }
        return 0;
    }

    private static void addVisibleColumns(final Set<ColumnInfo> columnInfos, final FilterTable filterTable) {
        for (final Object propertyId : filterTable.getVisibleColumns()) {
            columnInfos.add(createColumnInfo(propertyId, filterTable));
        }
    }

//    private static void addCollapsedColumns(Set<ColumnInfo> columnInfos, FilterTable filterTable) {
//        for (Object propertyId : filterTable.getContainerPropertyIds()) {
//            if (filterTable.isColumnCollapsible(propertyId)) {
//                columnInfos.add(createColumnInfo(propertyId, filterTable));
//            }
//        }
//    }
//
    private static ColumnInfo createColumnInfo(final Object propertyId, final FilterTable filterTable) {
        final ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setPropertyId((String) propertyId);
        columnInfo.setCollapsed(filterTable.isColumnCollapsed(propertyId));
//        columnInfo.setFilter(filterTable.getFilterFieldValue(propertyId));
        columnInfo.setWidth(filterTable.getColumnWidth(propertyId));
        columnInfo.setWidthRatio(filterTable.getColumnExpandRatio(propertyId));
        columnInfo.setIndex(getColumnIndex(filterTable, propertyId));
        if (propertyId.equals(filterTable.getSortContainerPropertyId())) {
            if (filterTable.isSortAscending()) {
                columnInfo.setSortOrder(SortOrder.ASCENDING);
            } else {
                columnInfo.setSortOrder(SortOrder.DESCENDING);
            }
        }
        return columnInfo;
    }

}
