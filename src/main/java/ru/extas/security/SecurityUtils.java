package ru.extas.security;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;

/**
 * Вспомогательные методы при работе с правами доступа
 *
 * @author Valery Orlov
 *         Date: 05.03.14
 *         Time: 17:48
 * @version $Id: $Id
 * @since 0.3
 */
public class SecurityUtils {

    private static BiMap<ExtaDomain, Class> domain2Class = HashBiMap.create(64);

    {
        domain2Class.put(ExtaDomain.DASHBOARD, Class.class);
        domain2Class.put(ExtaDomain.TASKS_TODAY, Class.class);
        domain2Class.put(ExtaDomain.TASKS_WEEK, Class.class);
        domain2Class.put(ExtaDomain.TASKS_MONTH, Class.class);
        domain2Class.put(ExtaDomain.TASKS_ALL, Class.class);
        domain2Class.put(ExtaDomain.PERSON, Person.class);
        domain2Class.put(ExtaDomain.COMPANY, Company.class);
        domain2Class.put(ExtaDomain.LEGAL_ENTITY, LegalEntity.class);
        domain2Class.put(ExtaDomain.SALE_POINT, SalePoint.class);
        domain2Class.put(ExtaDomain.LEADS_NEW, Lead.class);
        domain2Class.put(ExtaDomain.LEADS_QUAL, Class.class);
        domain2Class.put(ExtaDomain.LEADS_CLOSED, Class.class);
        domain2Class.put(ExtaDomain.SALES_OPENED, Class.class);
        domain2Class.put(ExtaDomain.SALES_SUCCESSFUL, Class.class);
        domain2Class.put(ExtaDomain.SALES_CANCELED, Class.class);
        domain2Class.put(ExtaDomain.INSURANCE_PROP, Class.class);
        domain2Class.put(ExtaDomain.INSURANCE_BSO, Class.class);
        domain2Class.put(ExtaDomain.INSURANCE_TRANSFER, Class.class);
        domain2Class.put(ExtaDomain.INSURANCE_A_7, Class.class);
        domain2Class.put(ExtaDomain.PROD_CREDIT, Class.class);
        domain2Class.put(ExtaDomain.PROD_INSURANCE, Class.class);
        domain2Class.put(ExtaDomain.PROD_INSTALL, Class.class);
        domain2Class.put(ExtaDomain.USERS, Class.class);
        domain2Class.put(ExtaDomain.SETTINGS, Class.class);
    }

    /**
     * <p>findDomainByClass.</p>
     *
     * @param entityClass a {@link java.lang.Class} object.
     * @return a {@link ru.extas.security.ExtaDomain} object.
     */
    public static ExtaDomain findDomainByClass(final Class entityClass) {
        // FIXME: To Implement
        return null;
    }

}
