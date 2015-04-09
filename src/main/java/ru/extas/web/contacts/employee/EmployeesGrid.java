package ru.extas.web.contacts.employee;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.security.ExtaDomain;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Грид для управления сотрудниками
 *
 * @author Valery Orlov
 *         Date: 19.10.2014
 *         Time: 15:11
 */
public class EmployeesGrid extends ExtaGrid<Employee> {

    private SupplierSer<Company> companySupplier;
    private SupplierSer<SalePoint> salePointSupplier;

    public EmployeesGrid() {
        this(null);
    }

    public EmployeesGrid(final Company company) {
        super(Employee.class);
    }

    @Override
    public ExtaEditForm<Employee> createEditForm(final Employee employee, final boolean isInsert) {
        final EmployeeEditForm form = new EmployeeEditForm(employee);
        form.setCompanySupplier(companySupplier);
        return form;
    }

    @Override
    protected GridDataDecl createDataDecl() {
        final EmployeesDataDecl dataDecl = new EmployeesDataDecl();
        if (companySupplier != null)
            dataDecl.setColumnCollapsed("company.name", true);
        return dataDecl;
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDbContainer<Employee> container = salePointSupplier != null || companySupplier != null ?
                new ExtaDbContainer<>(Employee.class) :
                SecuredDataContainer.create(Employee.class, ExtaDomain.EMPLOYEE);
        container.addNestedContainerProperty("company.name");
        container.sort(new Object[]{"company.name", "name"}, new boolean[]{true, true});
        if (salePointSupplier != null) {
            container.addContainerFilter(new Compare.Equal("workPlace", salePointSupplier.get()));
        } else if (companySupplier != null)
            container.addContainerFilter(new Compare.Equal("company", companySupplier.get()));
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод сотрудника в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование данных сотрудинка"));

        return actions;
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }

    public void setSalePointSupplier(final SupplierSer<SalePoint> salePointSupplier) {
        this.salePointSupplier = salePointSupplier;
    }

    public SupplierSer<SalePoint> getSalePointSupplier() {
        return salePointSupplier;
    }
}
