package ru.extas.web.contacts.employee;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.ItemAction;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка сотрудников
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 * @since 0.3
 */
public class EmployeeMultySelect extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;
    private SupplierSer<SalePoint> salePointSupplier;
    private SupplierSer<LegalEntity> legalEntitySupplier;

    private EmployeesGrid grid;
    private ExtaBeanContainer<Employee> beanContainer;

    public EmployeeMultySelect(String caption) {
        setCaption(caption);
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
                final Set<Employee> list = getValue() != null ? getValue() : newHashSet();
                beanContainer = new ExtaBeanContainer<>(Employee.class);
                beanContainer.addNestedContainerProperty("company.name");
                beanContainer.addAll(list);

                return container = beanContainer;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new UIAction("Добавить", "Выбрать сотрудника для добавления в группу кураторов", Fontello.DOC_NEW) {
                    @Override
                    public void fire(final Set itemIds) {
                        final EmployeeSelectWindow selectWindow = new EmployeeSelectWindow("Выберите сотрудника");
                        selectWindow.setCompanySupplier(companySupplier);
                        selectWindow.addCloseListener(e -> {
                            if (selectWindow.isSelectPressed()) {
                                beanContainer.addAll(selectWindow.getSelected());
                                setValue(newHashSet(beanContainer.getItemIds()));
                                NotificationUtil.showSuccess("Сотрудники добавлены в группу");
                            }
                        });
                        selectWindow.showModal();
                    }
                });

                actions.add(new ItemAction("Удалить", "Убрать сотрудника из группы", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> beanContainer.removeItem(id));
                        setValue(newHashSet(beanContainer.getItemIds()));
                    }
                });
                return actions;
            }
        };

        grid.setSizeFull();
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
