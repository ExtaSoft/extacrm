package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaBeanContainer;
import ru.extas.web.contacts.employee.EmployeeSelectWindow;
import ru.extas.web.contacts.employee.EmployeesGrid;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Поле для ввода/редактирования списка владельцев компании
 *
 * @author Valery Orlov
 *         Date: 13.02.14
 *         Time: 15:10
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanyOwnersField extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;
    private EmployeesGrid grid;
    private ExtaBeanContainer<Employee> beanContainer;

    /**
     * <p>Constructor for CompanyOwnersField.</p>
     *
     * @param company
     */
    public CompanyOwnersField() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        grid = new EmployeesGrid() {
            @Override
            protected Container createContainer() {
                final Set<Employee> list = getValue() != null ? getValue() : newHashSet();
                beanContainer = new ExtaBeanContainer<>(Employee.class);
                beanContainer.addNestedContainerProperty("company.name");
                beanContainer.addAll(list);

                return container = beanContainer;
            }

            @Override
            public ExtaEditForm<Employee> createEditForm(final Employee employee, final boolean isInsert) {
                final ExtaEditForm<Employee> form = super.createEditForm(employee, isInsert);
                form.setReadOnly(isReadOnly());
                return form;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                if (!isReadOnly())
                    actions.add(new UIAction("Добавить", "Добавить сотрудника в список владельцев компании", Fontello.DOC_NEW) {
                        @Override
                        public void fire(final Set itemIds) {
                            final EmployeeSelectWindow selectWindow = new EmployeeSelectWindow("Выберите владельца компании");
                            selectWindow.addCloseListener(e -> {
                                if (selectWindow.isSelectPressed()) {
                                    beanContainer.addAll(selectWindow.getSelected());
                                    setValue(newHashSet(beanContainer.getItemIds()));
                                    NotificationUtil.showSuccess("Владелец добавлен");
                                }
                            });
                            selectWindow.setCompanySupplier(companySupplier);
                            selectWindow.showModal();
                        }
                    });
                actions.add(new EditObjectAction("Просмотр", "Редактирование данных сотрудинка"));
                if (!isReadOnly())
                    actions.add(new ItemAction("Удалить", "Удалить сотрудника из списка владельцев", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> beanContainer.removeItem(id));
                        setValue(newHashSet(beanContainer.getItemIds()));
                    }
                });

                return actions;
            }
        };
        grid.setCompanySupplier(companySupplier);
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

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
