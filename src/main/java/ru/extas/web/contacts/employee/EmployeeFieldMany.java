package ru.extas.web.contacts.employee;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка сотрудников для компании и торговой точки
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 * @since 0.3
 */
public class EmployeeFieldMany extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;
    private SupplierSer<SalePoint> salePointSupplier;
    private SupplierSer<LegalEntity> legalEntitySupplier;

    private EmployeesGrid grid;
    private ExtaBeanContainer<Employee> beanContainer;

    public EmployeeFieldMany() {
        setBuffered(true);
        setRequiredError("Необходимо выбрать хотябы одного сотрудника!");
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
                final EmployeeEditForm form = new EmployeeEditForm(employee) {
                    @Override
                    protected Employee saveEntity(final Employee employee) {
                        if (isInsert)
                            beanContainer.addBean(employee);
                        else
                            setValue(null, true); // Форсируем изменения
                        setValue(newHashSet(beanContainer.getItemIds()));
                        return employee;
                    }
                };
                form.setCompanySupplier(companySupplier);
                form.setSalePointSupplier(salePointSupplier);
                form.setLegalEntitySupplier(legalEntitySupplier);
                form.setReadOnly(isReadOnly());
                return form;
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

    public SupplierSer<SalePoint> getSalePointSupplier() {
        return salePointSupplier;
    }

    public void setSalePointSupplier(final SupplierSer<SalePoint> salePointSupplier) {
        this.salePointSupplier = salePointSupplier;
    }

    public SupplierSer<LegalEntity> getLegalEntitySupplier() {
        return legalEntitySupplier;
    }

    public void setLegalEntitySupplier(final SupplierSer<LegalEntity> legalEntitySupplier) {
        this.legalEntitySupplier = legalEntitySupplier;
    }
}
