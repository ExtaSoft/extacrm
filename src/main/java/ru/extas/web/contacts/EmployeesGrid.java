package ru.extas.web.contacts;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;

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

    private final Company company;

    public EmployeesGrid() {
        this(null);
    }

    public EmployeesGrid(Company company) {
        super(Employee.class, false);
        this.company = company;
        initialize();
    }

    @Override
    public ExtaEditForm<Employee> createEditForm(Employee employee, boolean isInsert) {
        return new EmployeeEditForm(employee, company);
    }

    @Override
    protected GridDataDecl createDataDecl() {
        final EmployeesDataDecl dataDecl = new EmployeesDataDecl();
        if(company != null)
            dataDecl.setColumnCollapsed("company.name", true);
        return dataDecl;
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDataContainer<Employee> container = new SecuredDataContainer<>(Employee.class, ExtaDomain.EMPLOYEE);
        container.addNestedContainerProperty("company.name");
        container.sort(new Object[]{"company.name", "name"}, new boolean[]{true, true});
        if (company != null)
            container.addContainerFilter(new Compare.Equal("company", company));
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод сотрудника в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование данных сотрудинка"));

        return actions;
    }
}
