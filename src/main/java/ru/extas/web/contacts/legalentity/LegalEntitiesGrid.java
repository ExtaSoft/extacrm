/**
 *
 */
package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.security.ExtaDomain;
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

	private static final long serialVersionUID = 2299363623807745654L;
	private final Company company;

	/**
	 * <p>Constructor for LegalEntitiesGrid.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesGrid(final Company company) {
		super(LegalEntity.class, false);
		this.company = company;
		initialize();
	}

    @Override
    public ExtaEditForm<LegalEntity> createEditForm(final LegalEntity legalEntity, final boolean isInsert) {
        return new LegalEntityEditForm(legalEntity, company);
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
        final LegalEntityDataDecl dataDecl = new LegalEntityDataDecl();
        if (company != null)
            dataDecl.setColumnCollapsed("company.name", true);
        return dataDecl;
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<LegalEntity> container = new SecuredDataContainer<>(LegalEntity.class, ExtaDomain.LEGAL_ENTITY);
		if (company != null)
			container.addContainerFilter(new Compare.Equal("company", company));
		container.addNestedContainerProperty("regAddress.region");
		container.addNestedContainerProperty("company.name");
		return container;
	}

    @Override
    public LegalEntity createEntity() {
        final LegalEntity legalEntity = super.createEntity();
        legalEntity.setCompany(company);
        return legalEntity;
    }

    /** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		actions.add(new NewObjectAction("Новый", "Ввод нового Юридического лица в систему"));
		actions.add(new EditObjectAction("Изменить", "Редактирование данных Юридического лица"));

		return actions;
	}
}
