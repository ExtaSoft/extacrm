package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.web.commons.*;
import ru.extas.web.contacts.employee.EmployeeEditForm;
import ru.extas.web.contacts.employee.EmployeesGrid;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * Реализует ввод/редактирование списка сотрудников для компании и торговой точки
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanyEmployeesField extends CustomField<Set> {

    private final Company company;
    private EmployeesGrid grid;
    private BeanItemContainer<Employee> beanContainer;

    /**
     * <p>Constructor for ContactEmployeeField.</p>
     *
     * @param company
     */
    public CompanyEmployeesField(Company company) {
        this.company = company;
        setBuffered(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        grid = new EmployeesGrid(company) {
            @Override
            protected Container createContainer() {
                final Set<Employee> list = getValue() != null ? getValue() : new HashSet<>();
                beanContainer = new BeanItemContainer<>(Employee.class);
                beanContainer.addNestedContainerProperty("company.name");
                beanContainer.addAll(list);

                return container = beanContainer;
            }

            @Override
            public ExtaEditForm<Employee> createEditForm(Employee employee, boolean isInsert) {
                final EmployeeEditForm form = new EmployeeEditForm(employee, company) {
                    @Override
                    protected Employee saveObject(Employee obj) {
                        if (isInsert)
                            beanContainer.addBean(obj);
                        setValue(newHashSet(beanContainer.getItemIds()));
                        return obj;
                    }
                };
                form.setReadOnly(isReadOnly());
                return form;
            }
        };

        grid.setReadOnly(isReadOnly());
        return grid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

}
