/**
 * 
 */
package ru.extas.server;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.List;

import javax.jdo.Query;

import com.google.appengine.repackaged.com.google.common.base.Joiner;

/**
 * Упрощает работу с запросами
 * 
 * @author Valery Orlov
 * 
 */
public class QueryUtils {

	/**
	 * Создает строку сортировки для запроса
	 * 
	 * @param q
	 *            запрос
	 * 
	 * @param sortPropertyIds
	 *            поля сортировки
	 * @param sortStates
	 *            направление сортировки
	 */
	public static void setOrdering(Query q, Object[] sortPropertyIds, boolean[] sortStates) {

		String order = "";
		if (sortPropertyIds != null && sortPropertyIds.length != 0 && sortStates != null && sortStates.length != 0) {
			List<String> sortProps = newArrayListWithCapacity(sortPropertyIds.length);
			int i = 0;
			for (Object prop : sortPropertyIds) {
				sortProps.add((String) prop + (sortStates[i] ? "ascending" : "descending"));
				i++;
			}
			order = Joiner.on(",").join(sortProps);
			q.setOrdering(order);
		}
	}

	/**
	 * Устанавливает диапазон выборки
	 * 
	 * @param q
	 *            запрос
	 * @param startIndex
	 *            начало диапазона
	 * @param count
	 *            размер выборки
	 */
	public static void setRange(Query q, int startIndex, int count) {
		if (startIndex != -1 && count != 0)
			q.setRange(startIndex, startIndex + count);
	}
}
