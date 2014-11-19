package ru.extas.web.commons;

import com.vaadin.addon.tableexport.CustomTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>MyExcelExport class.</p>
 *
 * @author Valery Orlov
 *         Date: 12.05.2014
 *         Time: 16:22
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MyExcelExport extends ExcelExport {

    /**
     * <p>Constructor for MyExcelExport.</p>
     *
     * @param tableHolder a {@link com.vaadin.addon.tableexport.CustomTableHolder} object.
     */
    public MyExcelExport(final CustomTableHolder tableHolder) {
        super(tableHolder);
    }

    /** {@inheritDoc} */
    @Override
    protected CellStyle getCellStyle(final Object rootItemId, final int row, final int col, final boolean totalsRow) {
        final Object propId = getPropIds().get(col);
        final Class<?> propType = getPropertyType(propId);
        // get the basic style for the type of cell (i.e. data, header, total)
        if ((rowHeaders) && (col == 0) || propType == null) {
            if (null == rowHeaderCellStyle) {
                return columnHeaderCellStyle;
            }
            return rowHeaderCellStyle;
        }
        if (totalsRow) {
            if (this.propertyExcelFormatMap.containsKey(propId)) {
                final short df = dataFormat.getFormat(propertyExcelFormatMap.get(propId));
                final CellStyle customTotalStyle = workbook.createCellStyle();
                customTotalStyle.cloneStyleFrom(totalsDoubleCellStyle);
                customTotalStyle.setDataFormat(df);
                return customTotalStyle;
            }
            if (isIntegerLongShortOrBigDecimal(propType)) {
                return totalsIntegerCellStyle;
            }
            return totalsDoubleCellStyle;
        }
        // Check if the user has over-ridden that data format of this property
        if (this.propertyExcelFormatMap.containsKey(propId)) {
            final short df = dataFormat.getFormat(propertyExcelFormatMap.get(propId));
            if (dataFormatCellStylesMap.containsKey(df)) {
                return dataFormatCellStylesMap.get(df);
            }
            // if it hasn't already been created for re-use, we create a cell style and override the data format
            // For data cells, each data format corresponds to a single complete cell style
            final CellStyle retStyle = workbook.createCellStyle();
            retStyle.cloneStyleFrom(dataFormatCellStylesMap.get(doubleDataFormat));
            retStyle.setDataFormat(df);
            dataFormatCellStylesMap.put(df, retStyle);
            return retStyle;
        }
        // if not over-ridden, use the overall setting
        if (isDoubleOrFloat(propType)) {
            return dataFormatCellStylesMap.get(doubleDataFormat);
        } else {
            if (isIntegerLongShortOrBigDecimal(propType)) {
                return dataFormatCellStylesMap.get(integerDataFormat);
            } else {
                if (java.util.Date.class.isAssignableFrom(propType)
                        || LocalDate.class.isAssignableFrom(propType)
                        || DateTime.class.isAssignableFrom(propType)
                        ) {
                    return dataFormatCellStylesMap.get(dateDataFormat);
                }
            }
        }
        return dataFormatCellStylesMap.get(doubleDataFormat);
    }

    /** {@inheritDoc} */
    @Override
    protected void addDataRow(final Sheet sheetToAddTo, final Object rootItemId, final int row) {
        final Row sheetRow = sheetToAddTo.createRow(row);
        Property prop;
        Object propId;
        Object value;
        Cell sheetCell;
        for (int col = 0; col < getPropIds().size(); col++) {
            propId = getPropIds().get(col);
            prop = getProperty(rootItemId, propId);
            if (null == prop) {
                value = null;
            } else {
                value = prop.getValue();
            }
            sheetCell = sheetRow.createCell(col);
            final CellStyle cs = getCellStyle(rootItemId, row, col, false);
            sheetCell.setCellStyle(cs);
            final Short poiAlignment = getTableHolder().getCellAlignment(propId);
            CellUtil.setAlignment(sheetCell, workbook, poiAlignment);
            if (null != value) {
                if (!isNumeric(prop.getType())) {
                    if (Date.class.isAssignableFrom(prop.getType())) {
                        sheetCell.setCellValue((Date) value);
                    } else if (LocalDate.class.isAssignableFrom(prop.getType())) {
                        sheetCell.setCellValue(((LocalDate) value).toDate());
                    } else if (DateTime.class.isAssignableFrom(prop.getType())) {
                        sheetCell.setCellValue(((DateTime) value).toGregorianCalendar());
                    } else {
                        sheetCell.setCellValue(createHelper.createRichTextString(value.toString()));
                    }
                } else {
                    try {
                        // parse all numbers as double, the format will determine how they appear
                        final Double d = Double.parseDouble(value.toString());
                        sheetCell.setCellValue(d);
                    } catch (final NumberFormatException nfe) {
                        sheetCell.setCellValue(createHelper.createRichTextString(value.toString()));
                    }
                }
            }
        }
    }

    private boolean isNumeric(final Class<?> type) {
        if (isIntegerLongShortOrBigDecimal(type)) {
            return true;
        }
        if (isDoubleOrFloat(type)) {
            return true;
        }
        return false;
    }

    private boolean isIntegerLongShortOrBigDecimal(final Class<?> type) {
        if ((Integer.class.equals(type) || (int.class.equals(type)))) {
            return true;
        }
        if ((Long.class.equals(type) || (long.class.equals(type)))) {
            return true;
        }
        if ((Short.class.equals(type)) || (short.class.equals(type))) {
            return true;
        }
        if ((BigDecimal.class.equals(type)) || (BigDecimal.class.equals(type))) {
            return true;
        }
        return false;
    }

    /**
     * Utility method to determine whether value being put in the Cell is double-like type.
     *
     * @param type the type
     * @return true, if is double-like
     */
    private boolean isDoubleOrFloat(final Class<?> type) {
        if ((Double.class.equals(type)) || (double.class.equals(type))) {
            return true;
        }
        if ((Float.class.equals(type)) || (float.class.equals(type))) {
            return true;
        }
        return false;
    }

    private Property getProperty(final Object rootItemId, final Object propId) {
        Property prop;
        if (getTableHolder().isGeneratedColumn(propId)) {
            prop = getTableHolder().getPropertyForGeneratedColumn(propId, rootItemId);
        } else {
            prop = getTableHolder().getContainerDataSource().getContainerProperty(rootItemId, propId);
            if (useTableFormatPropertyValue) {
                if (getTableHolder().isExportableFormattedProperty()) {
                    final String formattedProp = getTableHolder().getFormattedPropertyValue(rootItemId, propId, prop);
                    if (null == prop) {
                        prop = new ObjectProperty<String>(formattedProp, String.class);
                    } else {
                        final Object val = prop.getValue();
                        if (null == val) {
                            prop = new ObjectProperty<String>(formattedProp, String.class);
                        } else {
                            if (!val.toString().equals(formattedProp)) {
                                prop = new ObjectProperty<String>(formattedProp, String.class);
                            }
                        }
                    }
                }
            }
        }
        return prop;
    }

    private Class<?> getPropertyType(final Object propId) {
        final Class<?> classType;
        if (getTableHolder().isGeneratedColumn(propId)) {
            classType = getTableHolder().getPropertyTypeForGeneratedColumn(propId);
        } else {
            classType = getTableHolder().getContainerDataSource().getType(propId);
        }
        return classType;
    }
}
