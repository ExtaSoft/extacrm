package ru.extas.web.contacts.employee;

import ru.extas.web.commons.EmailLinkColumnGen;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.contacts.company.CompanyColumnGenerator;

/**
 * Определение колонок в гриде сотрудников
 *
 * @author Valery Orlov
 *         Date: 19.10.2014
 *         Time: 15:15
 */
public class EmployeesDataDecl extends GridDataDecl {

    public EmployeesDataDecl() {
        addMapping("name", "Имя");
        addMapping("company.name", "Компания", new CompanyColumnGenerator());
        addMapping("jobPosition", "Должность");
        addMapping("phone", "Телефон", PhoneConverter.class);
        addMapping("email", "E-Mail", new EmailLinkColumnGen());
        super.addDefaultMappings();
    }
}
