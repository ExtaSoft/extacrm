/**
 * 
 */
package ru.extas.web.commons;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVWriter;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

/**
 * Облегчает работу с CSV
 * 
 * @author Valery Orlov
 * 
 */
public class CsvUtil {

	/**
	 * Преобразует даные в таблице в CSV и пишет все в поток вывода
	 * 
	 * @param table
	 *            таблица
	 * @param out
	 *            поток вывода
	 * @throws IOException
	 */
	public static void tableToCsv(final Table table, final OutputStream out) throws IOException {

		checkArgument(table != null, "Table can not be null");
		checkArgument(out != null, "Output stream can not be null");

		final CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, "cp1251"), ';');
		try {

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
				writer.writeNext(values.toArray(new String[0]));
				values.clear();
			}
		} finally {
			writer.close();
		}
	}

	@SuppressWarnings("unchecked")
	private static String formatPropertyValue(final Property<?> property, final Locale locale,
			Converter<String, Object> converter) {
		if (property == null) { return ""; }

		if (property.getType() == BigDecimal.class) {
			converter = null;
		} else if (converter == null) {
			converter = (Converter<String, Object>)ConverterUtil.getConverter(String.class, property.getType(), UI
					.getCurrent().getSession());
		}
		final Object value = property.getValue();
		if (converter != null) { return converter.convertToPresentation(value, null, locale); }
		return (null != value) ? value.toString() : "";
	}
}
