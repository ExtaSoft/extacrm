package ru.extas.web.contacts.employee;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.Optional;
import java.util.Set;

/**
 * Реализует ввод/редактирование списка сотрудников для компании и торговой точки
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 * @since 0.3
 */
public class EmployeeFieldMulty extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;
    private SupplierSer<SalePoint> salePointSupplier;
    private SupplierSer<LegalEntity> legalEntitySupplier;

    private EmployeesGrid grid;

    public EmployeeFieldMulty() {
        setBuffered(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        grid = new EmployeesGrid() {
            {
                setCompanySupplier(companySupplier);
                setSalePointSupplier(salePointSupplier);
            }

            @Override
            protected Container createContainer() {
                final ExtaDbContainer<Employee> cont = (ExtaDbContainer<Employee>) super.createContainer();
                Optional.ofNullable(legalEntitySupplier)
                        .ifPresent(s -> cont.addContainerFilter(new Compare.Equal("legalWorkPlace", s.get())));

                return cont;
            }

            @Override
            public ExtaEditForm<Employee> createEditForm(final Employee employee, final boolean isInsert) {
                final EmployeeEditForm form = (EmployeeEditForm) super.createEditForm(employee, isInsert);
                form.addCloseFormListener(e -> {
                    if (form.isSaved() && isInsert)
                        setValue(((ExtaDbContainer) container).getEntitiesSet());
                });
                form.setCompanySupplier(companySupplier);
                form.setSalePointSupplier(salePointSupplier);
                form.setLegalEntitySupplier(legalEntitySupplier);
                form.setReadOnly(isReadOnly());
                return form;
            }
        };

        grid.setReadOnly(isReadOnly());
        addReadOnlyStatusChangeListener(e -> grid.setReadOnly(isReadOnly()));
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
