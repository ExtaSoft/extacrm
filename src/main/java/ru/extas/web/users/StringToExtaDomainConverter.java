/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует подсистемы в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToExtaDomainConverter extends String2EnumConverter<ExtaDomain> {

    /**
     * <p>Constructor for StringToExtaDomainConverter.</p>
     */
    public StringToExtaDomainConverter() {
        super(ExtaDomain.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BiMap<ExtaDomain, String> createEnum2StringMap() {
        final BiMap<ExtaDomain, String> map = HashBiMap.create();
        map.put(ExtaDomain.DASHBOARD, "РАБОЧИЙ СТОЛ");
        map.put(ExtaDomain.TASKS_TODAY, "ЗАДАЧИ/НА СЕГОДНЯ");
        map.put(ExtaDomain.TASKS_WEEK, "ЗАДАЧИ/НА НЕДЕЛЮ");
        map.put(ExtaDomain.TASKS_MONTH, "ЗАДАЧИ/НА МЕСЯЦ");
        map.put(ExtaDomain.TASKS_ALL, "ЗАДАЧИ/ВСЕ");
        map.put(ExtaDomain.PERSON, "КОНТАКТЫ/ФИЗИЧЕСКИЕ ЛИЦА");
        map.put(ExtaDomain.COMPANY, "КОНТАКТЫ/КОМПАНИИ");
        map.put(ExtaDomain.LEGAL_ENTITY, "КОНТАКТЫ/ЮРИДИЧЕСКИЕ ЛИЦА");
        map.put(ExtaDomain.SALE_POINT, "КОНТАКТЫ/ТОРГОВЫЕ ТОЧКИ");
        map.put(ExtaDomain.LEADS_NEW, "ЛИДЫ/НОВЫЕ");
        map.put(ExtaDomain.LEADS_QUAL, "ЛИДЫ/КВАЛИФИЦИРОВАННЫЕ");
        map.put(ExtaDomain.LEADS_CLOSED, "ЛИДЫ/ЗАКРЫТЫЕ");
        map.put(ExtaDomain.SALES_OPENED, "ПРОДАЖИ/ОТКРЫТЫЕ");
        map.put(ExtaDomain.SALES_SUCCESSFUL, "ПРОДАЖИ/ЗАВЕРШЕННЫЕ");
        map.put(ExtaDomain.SALES_CANCELED, "ПРОДАЖИ/ОТМЕНЕННЫЕ");
        map.put(ExtaDomain.INSURANCE_PROP, "СТРАХОВАНИЕ/ИМУЩ. СТРАХОВКИ");
        map.put(ExtaDomain.INSURANCE_BSO, "СТРАХОВАНИЕ/БЛАНКИ (БСО)");
        map.put(ExtaDomain.INSURANCE_TRANSFER, "СТРАХОВАНИЕ/АКТЫ ПРИЕМА-ПЕРЕДАЧИ");
        map.put(ExtaDomain.INSURANCE_A_7, "СТРАХОВАНИЕ/КВИТАНЦИИ А-7");
        map.put(ExtaDomain.PROD_CREDIT, "ПРОДУКТЫ/КРЕДИТНЫЕ ПРОДУКТЫ");
        map.put(ExtaDomain.PROD_INSURANCE, "ПРОДУКТЫ/СТРАХОВЫЕ ПРОДУКТЫ");
        map.put(ExtaDomain.PROD_INSTALL, "ПРОДУКТЫ/РАССРОЧКА");
        map.put(ExtaDomain.MOTOR_MODEL, "ТЕХНИКА/МОДЕЛЬ ТЕХНИКИ");
        map.put(ExtaDomain.MOTOR_BRAND, "ТЕХНИКА/БРЕНД");
        map.put(ExtaDomain.MOTOR_TYPE, "ТЕХНИКА/ТИП ТЕХНИКИ");
        map.put(ExtaDomain.USERS, "ПОЛЬЗОВАТЕЛИ/ПОЛЬЗОВАТЕЛИ");
        map.put(ExtaDomain.USER_GROUPS, "ПОЛЬЗОВАТЕЛИ/ГРУППЫ");
        map.put(ExtaDomain.SETTINGS, "НАСТРОЙКИ");
        return map;
    }
}
