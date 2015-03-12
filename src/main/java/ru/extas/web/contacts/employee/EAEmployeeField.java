package ru.extas.web.contacts.employee;

import ru.extas.model.contacts.Company;
import ru.extas.server.contacts.CompanyRepository;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Селектор наших сотрудников
 *
 * @author Valery Orlov
 *         Date: 09.11.2014
 *         Time: 9:58
 */
public class EAEmployeeField extends EmployeeField {

    public EAEmployeeField(final String caption, final String description) {
        super(caption, description);

        setCompanySupplier(this::getOurCompany);
    }

    private Company getOurCompany() {
        return lookup(CompanyRepository.class).findEACompany();
    }
}
