/**
 *
 */
package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import org.tepi.filtertable.FilterGenerator;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.security.SalePointSecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.PhoneFilterGenerator;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;
import ru.extas.web.motor.MotorBrandSelect;

import java.text.MessageFormat;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (Точки продаж)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsGrid extends ExtaGrid<SalePoint> {

    private static final long serialVersionUID = 2299363623807745654L;

    private SupplierSer<Company> companySupplier;

    /**
     * <p>Constructor for SalePointsGrid.</p>
     */
    public SalePointsGrid() {
        super(SalePoint.class);
    }

    @Override
    public ExtaEditForm<SalePoint> createEditForm(final SalePoint salePoint, final boolean isInsert) {
        final SalePointEditForm form = new SalePointEditForm(salePoint);
        form.setCompanySupplier(companySupplier);
        return form;
    }

    @Override
    public SalePoint createEntity() {
        final SalePoint entity = super.createEntity();
        if (companySupplier != null)
            entity.setCompany(companySupplier.get());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new SalePointsDataDecl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDbContainer<SalePoint> container = new SecuredDataContainer<SalePoint>(new SalePointSecurityFilter());
        container.addNestedContainerProperty("posAddress.regionWithType");
        container.addNestedContainerProperty("posAddress.cityWithType");
        container.addNestedContainerProperty("posAddress.value");
        container.addNestedContainerProperty("company.name");
        if (companySupplier != null)
            container.addContainerFilter(new Compare.Equal("company", companySupplier.get()));
        return container;
    }

    @Override
    protected FilterGenerator createFilterGenerator() {
        return new CompositeFilterGenerator()
                .with(new AbstractFilterGenerator() {
                    @Override
                    public Container.Filter generateFilter(final Object propertyId, final Object value) {
                        if (propertyId.equals(SalePointsDataDecl.SALEPOINT_BRANDS_COLUMN)) {
                            return new Like("legalEntities.motorBrands", MessageFormat.format("%{0}%", value), false);
                        } else if (propertyId.equals(SalePointsDataDecl.SALEPOINT_LE_COLUMN)) {
                            return new Like("legalEntities.name", MessageFormat.format("%{0}%", value), false);
                        } else if (propertyId.equals(SalePointsDataDecl.SALEPOINT_INN_COLUMN)) {
                            return new Like("legalEntities.inn", MessageFormat.format("%{0}%", value), false);
                        } else
                            return super.generateFilter(propertyId, value);
                    }

                    @Override
                    public Container.Filter generateFilter(final Object propertyId, final Field<?> originatingField) {
                        return null;
                    }

                    @Override
                    public AbstractField<?> getCustomFilterComponent(final Object propertyId) {
                        if (propertyId.equals(SalePointsDataDecl.SALEPOINT_BRANDS_COLUMN))
                            return new MotorBrandSelect();
                        else if (propertyId.equals(SalePointsDataDecl.SALEPOINT_LE_COLUMN) ||
                                propertyId.equals(SalePointsDataDecl.SALEPOINT_INN_COLUMN))
                            return new EditField();
                        else
                            return null;
                    }
                })
                .with(new PhoneFilterGenerator(SalePoint_.phone.getName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод новой торговой точки в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование данных торговой точки"));

        return actions;
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }

}
