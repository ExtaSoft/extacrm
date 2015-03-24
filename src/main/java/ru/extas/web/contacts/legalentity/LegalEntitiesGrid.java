/**
 *
 */
package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.security.LegalEntitySecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesGrid extends ExtaGrid<LegalEntity> {

    private SupplierSer<Company> companySupplier;

    /**
     * <p>Constructor for LegalEntitiesGrid.</p>
     */
    public LegalEntitiesGrid() {
        super(LegalEntity.class);
    }

    @Override
    public ExtaEditForm<LegalEntity> createEditForm(final LegalEntity legalEntity, final boolean isInsert) {
        final LegalEntityEditForm form = new LegalEntityEditForm(legalEntity);
        form.setCompanySupplier(companySupplier);
        return form;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        final LegalEntityDataDecl dataDecl = new LegalEntityDataDecl();
        if (companySupplier != null)
            dataDecl.setColumnCollapsed("company.name", true);
        return dataDecl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaJpaContainer<LegalEntity> container =
                new SecuredDataContainer<LegalEntity>(new LegalEntitySecurityFilter());

        if (companySupplier != null)
            container.addContainerFilter(new Compare.Equal("company", companySupplier.get()));
        container.addNestedContainerProperty("regAddress.region");
        container.addNestedContainerProperty("company.name");
        return container;
    }

    @Override
    public LegalEntity createEntity() {
        final LegalEntity legalEntity = super.createEntity();
        if (companySupplier != null)
            legalEntity.setCompany(companySupplier.get());
        return legalEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод нового Юридического лица в систему"));
        actions.add(new EditObjectAction("Изменить", "Редактирование данных Юридического лица"));

        return actions;
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }

}
