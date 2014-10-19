package ru.extas.web.contacts;

import com.vaadin.data.Container;
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

    public EmployeesGrid() {
        super(Employee.class);
    }

    @Override
    public ExtaEditForm<Employee> createEditForm(Employee employee, boolean isInsert) {
        return new EmployeeEditForm(employee);
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new EmployeesDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDataContainer<Employee> container = new SecuredDataContainer<>(Employee.class, ExtaDomain.EMPLOYEE);
        container.addNestedContainerProperty("company.name");
        container.sort(new Object[]{"company.name", "name"}, new boolean[]{true, true});
//        if (company != null)
//            container.addContainerFilter(new Compare.Equal("company", company));
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
