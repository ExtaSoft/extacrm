package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Поле ввода/редактирования списка торговых точек компании
 *
 * @author Valery Orlov
 *         Date: 20.02.14
 *         Time: 14:46
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsField extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;

    /**
	 * <p>Constructor for SalePointsField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointsField() {
		setBuffered(true);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final SalePointsGrid grid = new SalePointsGrid() {

            @Override
            public ExtaEditForm<SalePoint> createEditForm(final SalePoint salePoint, final boolean isInsert) {
                final ExtaEditForm<SalePoint> form = super.createEditForm(salePoint, isInsert);
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

        addReadOnlyStatusChangeListener(e -> grid.setReadOnly(isReadOnly()));
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
