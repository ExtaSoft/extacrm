package ru.extas.web.insurance;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отрбражения страховок
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceDataDecl extends GridDataDecl {

	public InsuranceDataDecl() {
		super();
		addMapping("regNum", "Номер полиса", true, true, false);
		addMapping("chekNum", "Номер счета", true, true, true);
		addMapping("date", "Дата договора", true, true, false);
		addMapping("clientName", "Клиент - ФИО", true, true, false);
		addMapping("clientBirthday", "Клиент - Дата рождения", true, true, true);
		addMapping("clientPhone", "Клиент - Телефон", true, true, true);
		addMapping("clientMale", "Клиент - Пол", true, true, true);
		addMapping("motorType", "Тип техники", true, true, false);
		addMapping("motorBrand", "Марка техники", true, true, false);
		addMapping("motorModel", "Модель техники", true, true, false);
		addMapping("riskSum", "Страховая сумма", true, true, false);
		addMapping("premium", "Страховая премия", true, true, false);
		addMapping("paymentDate", "Дата оплаты страховой премии", true, true, true);
		addMapping("startDate", "Дата начала срока действия договора", true, true, true);
		addMapping("endDate", "Дата окончания срока действия договора", true, true, true);
		addMapping("createdBy", "Сотрудник", true, true, false);
		addMapping("reseller", "Салон", true, true, false);
	}

}
