package ru.extas.web;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Table;

/**
 * Опции отрбражения датасета: <li>порядок</li> <li>заголовок</li> <li>
 * доступность</li> <li>видимость</li>
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceDataSource {

	private final List<DataSourceMapping> mappings;

	public InsuranceDataSource() {
		super();
		mappings = new ArrayList<DataSourceMapping>();
		mappings.add(new DataSourceMapping("regNum", "Номер полиса", true, true, false));
		mappings.add(new DataSourceMapping("chekNum", "Номер счета", true, true, true));
		mappings.add(new DataSourceMapping("date", "Дата договора", true, true, false));
		mappings.add(new DataSourceMapping("clientName", "Клиент - ФИО", true, true, false));
		mappings.add(new DataSourceMapping("clientBirthday", "Клиент - Дата рождения", true, true, true));
		mappings.add(new DataSourceMapping("clientPhone", "Клиент - Телефон", true, true, true));
		mappings.add(new DataSourceMapping("clientMale", "Клиент - Пол", true, true, true));
		mappings.add(new DataSourceMapping("motorType", "Тип техники", true, true, false));
		mappings.add(new DataSourceMapping("motorBrand", "Марка техники", true, true, false));
		mappings.add(new DataSourceMapping("motorModel", "Модель техники", true, true, false));
		mappings.add(new DataSourceMapping("riskSum", "Страховая сумма", true, true, false));
		mappings.add(new DataSourceMapping("premium", "Страховая премия", true, true, false));
		mappings.add(new DataSourceMapping("paymentDate", "Дата оплаты страховой премии", true, true, true));
		mappings.add(new DataSourceMapping("startDate", "Дата начала срока действия договора", true, true, true));
		mappings.add(new DataSourceMapping("endDate", "Дата окончания срока действия договора", true, true, true));
		mappings.add(new DataSourceMapping("createdBy", "Сотрудник", true, true, false));
		mappings.add(new DataSourceMapping("reseller", "Салон", true, true, false));
	}

	public void setTableColumnHeaders(Table table) {

		for (DataSourceMapping prop : mappings)
			table.setColumnHeader(prop.getPropName(), prop.getCaption());

	}

	public void setTableVisibleColumns(Table table) {

		List<String> clmnIds = new ArrayList<String>(mappings.size());
		for (DataSourceMapping prop : mappings)
			if (prop.isVisible() || prop.isInGrid())
				clmnIds.add(prop.getPropName());

		table.setVisibleColumns(clmnIds.toArray());
	}

	public void setTableCollapsedColumns(Table table) {

		for (DataSourceMapping prop : mappings)
			table.setColumnCollapsed(prop.getPropName(), prop.isCollapsed());

	}

}
