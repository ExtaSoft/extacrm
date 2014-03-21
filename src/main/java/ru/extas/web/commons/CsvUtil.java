/**
 *
 */
package ru.extas.web.commons;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * Облегчает работу с CSV
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CsvUtil {

    /**
     * Преобразует даные в таблице в CSV и пишет все в поток вывода
     *
     * @param table таблица
     * @param out   поток вывода
     * @throws java.io.IOException if any.
     */
    public static void tableToCsv(final Table table, final OutputStream out) throws IOException {

        checkArgument(table != null, "Table can not be null");
        checkArgument(out != null, "Output stream can not be null");

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, "cp1251"), ';')) {

            // Формируем заголовки столбцов
            final String[] headers = table.getColumnHeaders();
            writer.writeNext(headers);

            // Пишем данные
            final List<String> values = newArrayListWithCapacity(headers.length);
            for (final Object itemId : table.getItemIds()) {
                final Item item = table.getItem(itemId);
                for (final Object propId : table.getVisibleColumns()) {
                    final Property<?> prop = item.getItemProperty(propId);
                    values.add(formatPropertyValue(prop, table.getLocale(), table.getConverter(propId)));
                }
                writer.writeNext(values.toArray(new String[values.size()]));
                values.clear();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static String formatPropertyValue(final Property<?> property, final Locale locale,
                                              Converter<String, Object> converter) {
        if (property == null) {
            return "";
        }

        if (property.getType() == BigDecimal.class) {
            converter = null;
        } else if (converter == null) {
            converter = (Converter<String, Object>) ConverterUtil.getConverter(String.class, property.getType(), UI
                    .getCurrent().getSession());
        }
        final Object value = property.getValue();
        if (converter != null) {
            return converter.convertToPresentation(value, null, locale);
        }
        return (null != value) ? value.toString() : "";
    }

    /**
     * <p>containerToCsv.</p>
     *
     * @param container a {@link com.vaadin.data.Container} object.
     * @param dataDecl a {@link ru.extas.web.commons.GridDataDecl} object.
     * @param local a {@link java.util.Locale} object.
     * @param out a {@link java.io.ByteArrayOutputStream} object.
     * @throws java.io.IOException if any.
     */
    public static void containerToCsv(Container container, GridDataDecl dataDecl, Locale local, ByteArrayOutputStream out) throws IOException {
        checkArgument(container != null, "Table can not be null");
        checkArgument(out != null, "Output stream can not be null");
        checkArgument(dataDecl != null, "Data declaration can not be null");

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, "cp1251"), ';')) {

            // Формируем заголовки столбцов
            List<String> headers = Lists.transform(dataDecl.getMappings(), new Function<DataDeclMapping, String>() {
                @Override
                public String apply(DataDeclMapping input) {
                    return input.getCaption();
                }
            });
            writer.writeNext(headers.toArray(new String[headers.size()]));

            // Пишем данные
            final List<String> values = newArrayListWithCapacity(headers.size());
            for (final Object itemId : container.getItemIds()) {
                final Item item = container.getItem(itemId);
                for (final DataDeclMapping propMap : dataDecl.getMappings()) {
                    final Property<?> prop = item.getItemProperty(propMap.getPropName());
                    values.add(formatPropertyValue(prop, local, (Converter<String, Object>) propMap.getConverter()));
                }
                writer.writeNext(values.toArray(new String[values.size()]));
                values.clear();
            }
        }
    }
}
