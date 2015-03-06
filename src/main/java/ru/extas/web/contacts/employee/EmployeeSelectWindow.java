package ru.extas.web.contacts.employee;

import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlyWindow;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 21.10.2014
 *         Time: 22:52
 */
public class EmployeeSelectWindow extends CloseOnlyWindow {
    private Set<Employee> selected;
    private boolean selectPressed;
    private SupplierSer<Company> companySupplier;
    private SupplierSer<SalePoint> salePointSupplier;
    private final EmployeesGrid grid;

    public EmployeeSelectWindow(final String caption) {
        super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
        grid = new EmployeesGrid() {
            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new DefaultAction("Выбрать", "Выбрать выделенного в списке сотрудника и закрыть окно", Fontello.CHECK) {
                    @Override
                    public void fire(final Set itemIds) {
                        selected = getEntities(itemIds);
                        selectPressed = true;
                        close();
                    }
                });

                actions.addAll(super.createActions());

                return actions;
            }

        };
        setContent(grid);
    }

    public boolean isSelectPressed() {
        return selectPressed;
    }

    public Set<Employee> getSelected() {
        return selected;
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
        grid.setCompanySupplier(companySupplier);
    }

    public SupplierSer<SalePoint> getSalePointSupplier() {
        return salePointSupplier;
    }

    public void setSalePointSupplier(final SupplierSer<SalePoint> salePointSupplier) {
        this.salePointSupplier = salePointSupplier;
        grid.setSalePointSupplier(salePointSupplier);
    }
}
