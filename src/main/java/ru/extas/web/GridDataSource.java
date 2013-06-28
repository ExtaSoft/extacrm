/**
 * 
 */
package ru.extas.web;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Table;

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
 * 
 */
public class GridDataSource {

	private final List<DataSourceMapping> mappings = new ArrayList<DataSourceMapping>();

	/**
	 * 
	 */
	public GridDataSource() {
		super();
	}

	/**
	 * Задает параметры отображения для свойства объекта
	 * 
	 * @param propName
	 *            - Имя свойства
	 * @param caption
	 *            - Заголовок столбца
	 * @param visible
	 *            - Видимость столбца
	 * @param inGrid
	 *            - Доступность в гриде
	 * @param collapsed
	 *            - Свернутый столбец в гриде
	 */
	public void addMapping(String propName, String caption, boolean visible, boolean inGrid, boolean collapsed) {
		mappings.add(new DataSourceMapping(propName, caption, visible, inGrid, collapsed));
	}

	/**
	 * Устанавливает заголовки столбцов таблици
	 * 
	 * @param table
	 *            таблица
	 */
	public void setTableColumnHeaders(Table table) {

		for (DataSourceMapping prop : mappings)
			table.setColumnHeader(prop.getPropName(), prop.getCaption());

	}

	/**
	 * Задает видимые в таблице столбци
	 * 
	 * @param table
	 *            - таблица
	 */
	public void setTableVisibleColumns(Table table) {

		List<String> clmnIds = new ArrayList<String>(mappings.size());
		for (DataSourceMapping prop : mappings)
			if (prop.isVisible() || prop.isInGrid())
				clmnIds.add(prop.getPropName());

		table.setVisibleColumns(clmnIds.toArray());
	}

	/**
	 * Устанавливает столбцы доступные в таблице, но свернутые
	 * 
	 * @param table
	 *            - таблица
	 */
	public void setTableCollapsedColumns(Table table) {

		for (DataSourceMapping prop : mappings)
			table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

	}

}