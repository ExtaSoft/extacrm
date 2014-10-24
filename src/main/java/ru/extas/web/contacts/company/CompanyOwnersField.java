package ru.extas.web.contacts.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
import ru.extas.web.contacts.employee.EmployeeSelectWindow;
import ru.extas.web.contacts.employee.EmployeesGrid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.web.commons.TableUtils.fullInitTable;

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
    private BeanItemContainer<Employee> beanContainer;

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
                final Set<Employee> list = getValue() != null ? getValue() : new HashSet<>();
                beanContainer = new BeanItemContainer<>(Employee.class);
                beanContainer.addNestedContainerProperty("company.name");
                beanContainer.addAll(list);

                return container = beanContainer;
            }

            @Override
            public ExtaEditForm<Employee> createEditForm(Employee employee, boolean isInsert) {
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
                        public void fire(Object itemId) {
                            final EmployeeSelectWindow selectWindow = new EmployeeSelectWindow("Выберите владельца компании", companySupplier);
                            selectWindow.addCloseListener(e -> {
                                if (selectWindow.isSelectPressed()) {
                                    beanContainer.addBean(selectWindow.getSelected());
                                    setValue(newHashSet(beanContainer.getItemIds()));
                                    NotificationUtil.showSuccess("Владелец добавлен");
                                }
                            });
                            selectWindow.showModal();
                        }
                    });
                actions.add(new EditObjectAction("Просмотр", "Редактирование данных сотрудинка"));
                if (!isReadOnly())
                    actions.add(new ItemAction("Удалить", "Удалить сотрудника из списка владельцев", Fontello.TRASH) {
                    @Override
                    public void fire(Object itemId) {
                        beanContainer.removeItem(itemId);
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

    public void setCompanySupplier(SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
