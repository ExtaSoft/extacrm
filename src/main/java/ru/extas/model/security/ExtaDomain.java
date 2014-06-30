package ru.extas.model.security;

/**
 * Раздел/подраздел системы доступ к которому предоставляется
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
	USERS("security/users"),
    /**
     * Группы пользователей
     */
    USER_GROUPS("security/groups"),
    /**
	 * Настройки
	 */
	SETTINGS("settings"),
    /**
     * Модель техники
     */
    MOTOR_MODEL("motor/models"),
    /**
     * Бренд
     */
    MOTOR_BRAND("motor/brands"),
    /**
     * Тип техники
     */
    MOTOR_TYPE("motor/types");

	/**
	 * <p>Constructor for ExtaDomain.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	ExtaDomain(final String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>getByName.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link ExtaDomain} object.
	 */
	public static ExtaDomain getByName(String name) {
		for (ExtaDomain item : ExtaDomain.values())
			if (item.getName().equals(name))
				return item;
		throw new IllegalArgumentException("There's no enum for name: " + name);
	}

	private final String name;
}
