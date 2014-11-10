package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import java.util.Set;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка юридических лиц (из владельца)
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 20:03
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesField extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;
    private BeanItemContainer<LegalEntity> itemContainer;

    /**
	 * <p>Constructor for LegalEntitiesField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesField() {
		setBuffered(true);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final LegalEntitiesGrid grid = new LegalEntitiesGrid() {
            @Override
            public ExtaEditForm<LegalEntity> createEditForm(final LegalEntity legalEntity, final boolean isInsert) {
                final ExtaEditForm<LegalEntity> form = super.createEditForm(legalEntity, isInsert);
                form.addCloseFormListener(e -> {
                    if (form.isSaved() && isInsert)
                        setValue(((ExtaDataContainer) container).getEntitiesSet());
                });
                form.setReadOnly(isReadOnly());
                return form;
            }
        };
        grid.setCompanySupplier(companySupplier);
		grid.setSizeFull();
        grid.setReadOnly(isReadOnly());

        return grid;
	}

	/** {@inheritDoc} */
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
