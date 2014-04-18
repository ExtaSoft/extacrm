package ru.extas.security;

import org.apache.shiro.authz.Permission;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * <p>DefaultRolePermissoin class.</p>
 *
 * @author Valery Orlov
 *         Date: 04.03.14
 *         Time: 0:22
 * @version $Id: $Id
 * @since 0.3
 */
public class DefaultRolePermissoin {
	/**
	 * <p>createAdminPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createAdminPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
        for(ExtaDomain domain : EnumSet.allOf(ExtaDomain.class))
		    permissions.add(new ExtaPermission(domain, SecureAction.ALL, SecureTarget.ALL));
		return permissions;
	}

	/**
	 * <p>createDistributorPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createDistributorPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
        // Начало (dashboard)	Все объекты	Полный доступ
        permissions.add(new ExtaPermission(ExtaDomain.DASHBOARD, SecureAction.ALL, SecureTarget.ALL));
        // Физические лица	Собственные объекты	Чтение, Ввод, Редактирование
        permissions.add(new ExtaPermission(ExtaDomain.PERSON, SecureAction.VIEW, SecureTarget.ALL));
        // Компании	Собственные объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.COMPANY, SecureAction.VIEW, SecureTarget.ALL));
        // Юридические лица	Собственные объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.LEGAL_ENTITY, SecureAction.VIEW, SecureTarget.ALL));
        // Торговые точки	Собственные объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.SALE_POINT, SecureAction.VIEW, SecureTarget.ALL));
        // Лиды	Собственные объекты	Чтение, Ввод, Редактирование
        permissions.add(new ExtaPermission(ExtaDomain.LEADS_NEW, SecureAction.VIEW, SecureTarget.ALL));
        permissions.add(new ExtaPermission(ExtaDomain.LEADS_QUAL, SecureAction.VIEW, SecureTarget.ALL));
        permissions.add(new ExtaPermission(ExtaDomain.LEADS_CLOSED, SecureAction.VIEW, SecureTarget.ALL));
        // Продажи	Собственные объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.SALES_OPENED, SecureAction.VIEW, SecureTarget.ALL));
        permissions.add(new ExtaPermission(ExtaDomain.SALES_SUCCESSFUL, SecureAction.VIEW, SecureTarget.ALL));
        permissions.add(new ExtaPermission(ExtaDomain.SALES_CANCELED, SecureAction.VIEW, SecureTarget.ALL));
        // Кредитные продукты	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_CREDIT, SecureAction.VIEW, SecureTarget.ALL));
        // Рассрочка	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSTALL, SecureAction.VIEW, SecureTarget.ALL));
        // Страховые продукты	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSURANCE, SecureAction.VIEW, SecureTarget.ALL));
		return permissions;
	}

	/**
	 * <p>createDealerPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createDealerPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
		// Начало (dashboard)	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.DASHBOARD, SecureAction.ALL, SecureTarget.ALL));
		// Задачи	Собственные объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_TODAY, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_WEEK, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_MONTH, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_ALL, SecureAction.ALL, SecureTarget.OWNONLY));
		// Физические лица	Собственные объекты	Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.PERSON, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.SALE_POINT));
		// Компании	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.COMPANY, SecureAction.VIEW, SecureTarget.SALE_POINT));
		// Юридические лица	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.LEGAL_ENTITY, SecureAction.ALL, SecureTarget.SALE_POINT));
		// Торговые точки	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.SALE_POINT, SecureAction.VIEW, SecureTarget.SALE_POINT));
		// Лиды	Собственные объекты	Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_NEW, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.SALE_POINT));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_QUAL, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.SALE_POINT));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_CLOSED, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.SALE_POINT));
		// Продажи	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.SALES_OPENED, SecureAction.VIEW, SecureTarget.SALE_POINT));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_SUCCESSFUL, SecureAction.VIEW, SecureTarget.SALE_POINT));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_CANCELED, SecureAction.VIEW, SecureTarget.SALE_POINT));
		// Кредитные продукты	Все объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.PROD_CREDIT, SecureAction.VIEW, SecureTarget.ALL));
        // Рассрочка	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSTALL, SecureAction.VIEW, SecureTarget.ALL));
        // Страховые продукты	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSURANCE, SecureAction.VIEW, SecureTarget.ALL));
		return permissions;
	}

	/**
	 * <p>createDealerManagerPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createDealerManagerPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
		// Начало (dashboard)	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.DASHBOARD, SecureAction.ALL, SecureTarget.ALL));
		// Задачи	Собственные объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_TODAY, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_WEEK, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_MONTH, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_ALL, SecureAction.ALL, SecureTarget.CORPORATE));
		// Физические лица	Собственные объекты	Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.PERSON, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.CORPORATE));
		// Компании	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.COMPANY, SecureAction.VIEW, SecureTarget.CORPORATE));
		// Юридические лица	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.LEGAL_ENTITY, SecureAction.ALL, SecureTarget.CORPORATE));
		// Торговые точки	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.SALE_POINT, SecureAction.VIEW, SecureTarget.CORPORATE));
		// Лиды	Собственные объекты	Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_NEW, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_QUAL, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_CLOSED, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.CORPORATE));
		// Продажи	Собственные объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.SALES_OPENED, SecureAction.VIEW, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_SUCCESSFUL, SecureAction.VIEW, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_CANCELED, SecureAction.VIEW, SecureTarget.CORPORATE));
        // Имущ. страховки | Все объекты | Чтение, Ввод, Редактирование
        permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_PROP, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.ALL));
        // Квитанции А-7 | Собственные объекты | Полный доступ
        permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_A_7, SecureAction.ALL, SecureTarget.OWNONLY));
		// Кредитные продукты	Все объекты	Чтение
		permissions.add(new ExtaPermission(ExtaDomain.PROD_CREDIT, SecureAction.VIEW, SecureTarget.ALL));
        // Рассрочка	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSTALL, SecureAction.VIEW, SecureTarget.ALL));
        // Страховые продукты	Все объекты	Чтение
        permissions.add(new ExtaPermission(ExtaDomain.PROD_INSURANCE, SecureAction.VIEW, SecureTarget.ALL));
		return permissions;
	}

	/**
	 * <p>createManagerPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createManagerPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
		// Начало (dashboard)	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.DASHBOARD, SecureAction.ALL, SecureTarget.ALL));
		// Задачи	Корпоративные объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_TODAY, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_WEEK, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_MONTH, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_ALL, SecureAction.ALL, SecureTarget.CORPORATE));
		// Физические лица	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.PERSON, SecureAction.ALL, SecureTarget.ALL));
		// Компании	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.COMPANY, SecureAction.ALL, SecureTarget.ALL));
		// Юридические лица	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.LEGAL_ENTITY, SecureAction.ALL, SecureTarget.ALL));
		// Торговые точки	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.SALE_POINT, SecureAction.ALL, SecureTarget.ALL));
		// Лиды	Корпоративные объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_NEW, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_QUAL, SecureAction.ALL, SecureTarget.CORPORATE));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_CLOSED, SecureAction.ALL, SecureTarget.CORPORATE));
		// Продажи	Корпоративные объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.SALES_OPENED, SecureAction.ALL, SecureTarget.ALL));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_SUCCESSFUL, SecureAction.ALL, SecureTarget.ALL));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_CANCELED, SecureAction.ALL, SecureTarget.ALL));
		// Имущ. страховки	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_PROP, SecureAction.ALL, SecureTarget.ALL));
		// Бланки (БСО)	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_BSO, SecureAction.ALL, SecureTarget.ALL));
		// Акты приема/передачи	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_TRANSFER, SecureAction.ALL, SecureTarget.ALL));
		// Квитанции А-7	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_A_7, SecureAction.ALL, SecureTarget.ALL));
		// Кредитные продукты	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.PROD_CREDIT, SecureAction.ALL, SecureTarget.ALL));
		// Страховые продукты	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.PROD_INSURANCE, SecureAction.ALL, SecureTarget.ALL));
		// Рассрочка	Все объекты	Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.PROD_INSTALL, SecureAction.ALL, SecureTarget.ALL));
		return permissions;
	}

	/**
	 * <p>createUserPermissions.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public static List<Permission> createUserPermissions() {
		final List<Permission> permissions;
		permissions = newArrayList();
		// Начало (dashboard) | Все объекты |Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.DASHBOARD, SecureAction.ALL, SecureTarget.ALL));
		// Задачи | Собственные объекты | Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_TODAY, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_WEEK, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_MONTH, SecureAction.ALL, SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.TASKS_ALL, SecureAction.ALL, SecureTarget.OWNONLY));
		// Физические лица | Все объекты | Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.PERSON, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.ALL));
		// Компании | Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.COMPANY, SecureAction.VIEW, SecureTarget.ALL));
		// Юридические лица | Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.LEGAL_ENTITY, SecureAction.VIEW, SecureTarget.ALL));
		// Торговые точки | Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.SALE_POINT, SecureAction.VIEW, SecureTarget.ALL));
		// Лиды | Собственные объекты | Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_NEW, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_QUAL, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.LEADS_CLOSED, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		// Продажи | Собственные объекты | Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.SALES_OPENED, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_SUCCESSFUL, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		permissions.add(new ExtaPermission(ExtaDomain.SALES_CANCELED, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.OWNONLY));
		// Имущ. страховки | Все объекты | Чтение, Ввод, Редактирование
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_PROP, EnumSet.of(SecureAction.VIEW, SecureAction.EDIT, SecureAction.INSERT), SecureTarget.ALL));
		// Квитанции А-7 | Собственные объекты | Полный доступ
		permissions.add(new ExtaPermission(ExtaDomain.INSURANCE_A_7, SecureAction.ALL, SecureTarget.OWNONLY));
		// Кредитные продукты | Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.PROD_CREDIT, SecureAction.VIEW, SecureTarget.ALL));
		// Страховые продукты |  Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.PROD_INSURANCE, SecureAction.VIEW, SecureTarget.ALL));
		// Рассрочка | Все объекты | Чтение
		permissions.add(new ExtaPermission(ExtaDomain.PROD_INSTALL, SecureAction.VIEW, SecureTarget.ALL));
		return permissions;
	}
}
