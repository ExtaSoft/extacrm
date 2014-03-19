package ru.extas.security;

/**
 * Раздел/подраздел системы доступ к которому предоставляется
 */
public enum ExtaDomain {

	/**
	 * Начало (dashboard)
	 */
	DASHBOARD("dashboard"),
	/**
	 * Задачи на сегодня
	 */
	TASKS_TODAY("tasks/today"),
	/**
	 * Задачи на неделю
	 */
	TASKS_WEEK("tasks/week"),
	/**
	 * Задачи на месяц
	 */
	TASKS_MONTH("tasks/month"),
	/**
	 * Задачи все
	 */
	TASKS_ALL("tasks/all"),
	/**
	 * Физические лица
	 */
	PERSON("contacts/persons"),
	/**
	 * Компании
	 */
	COMPANY("contacts/companies"),
	/**
	 * Юридические лица
	 */
	LEGAL_ENTITY("contacts/legal-entities"),
	/**
	 * Торговые точки
	 */
	SALE_POINT("contacts/sale-points"),
	/**
	 * Лиды новые
	 */
	LEADS_NEW("leads/new"),
	/**
	 * Лиды квалифицированные
	 */
	LEADS_QUAL("leads/qualified"),
	/**
	 * Лиды закрытые
	 */
	LEADS_CLOSED("leads/closed"),
	/**
	 * Продажи "Открытые"
	 */
	SALES_OPENED("sales/opened"),
	/**
	 * Продажи "Успешно завершенные"
	 */
	SALES_SUCCESSFUL("sales/successful"),
	/**
	 * Продажи "Отмененные"
	 */
	SALES_CANCELED("sales/canseled"),
	/**
	 * Имущ. страховки
	 */
	INSURANCE_PROP("insurance/property"),
	/**
	 * Бланки (БСО)
	 */
	INSURANCE_BSO("insurance/bso"),
	/**
	 * Акты приема/передачи
	 */
	INSURANCE_TRANSFER("insurance/transfer"),
	/**
	 * Квитанции А-7
	 */
	INSURANCE_A_7("insurance/a7"),
	/**
	 * Кредитные продукты
	 */
	PROD_CREDIT("products/credit"),
	/**
	 * Страховые продукты
	 */
	PROD_INSURANCE("products/insurance"),
	/**
	 * Рассрочка
	 */
	PROD_INSTALL("products/installment"),
	/**
	 * Пользователи
	 */
	USERS("users"),
	/**
	 * Настройки
	 */
	SETTINGS("settings");

	ExtaDomain(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ExtaDomain getByName(String name) {
		for (ExtaDomain item : ExtaDomain.values())
			if (item.getName().equals(name))
				return item;
		throw new IllegalArgumentException("There's no enum for name: " + name);
	}

	private final String name;
}
