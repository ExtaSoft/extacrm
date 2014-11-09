package ru.extas.web.contacts.employee;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.server.references.CategoryService;

/**
 * Селектор банковских сотрудников
 *
 * @author Valery Orlov
 *         Date: 09.11.2014
 *         Time: 9:58
 */
public class BankEmployeeField extends EmployeeField {

    public BankEmployeeField(String caption, String description) {
        super(caption, description);
        Container.Filter filter = new Compare.Equal("company.categories", CategoryService.COMPANY_CAT_BANK);
        setFilter(filter);
    }

}
