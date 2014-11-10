/**
 *
 */
package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.AddressInfo_;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.LegalEntity_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

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
        final ExtaDataContainer<LegalEntity> container = new SecuredDataContainer<LegalEntity>(LegalEntity.class, ExtaDomain.LEGAL_ENTITY) {
            @Override
            protected Predicate createAreaPredicate(final CriteriaBuilder cb, final Root objectRoot, Predicate predicate, final Set permitRegions, final Set permitBrands) {
                if (!permitRegions.isEmpty()) {
                    final Predicate regPredicate =
                            objectRoot.get(LegalEntity_.regAddress)
                                    .get(AddressInfo_.region)
                                    .in(permitRegions);
                    predicate = predicate == null ? regPredicate : cb.and(predicate, regPredicate);
                }
                if (!permitBrands.isEmpty()) {
                    final Predicate brPredicate =
                            objectRoot.join(LegalEntity_.motorBrands, JoinType.LEFT)
                                    .in(permitBrands);
                    predicate = predicate == null ? brPredicate : cb.and(predicate, brPredicate);
                }
                return predicate;
            }
        };
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
