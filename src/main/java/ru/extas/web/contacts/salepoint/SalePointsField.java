package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
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

	private final Company company;
    private BeanItemContainer<SalePoint> itemContainer;

    /**
	 * <p>Constructor for SalePointsField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public SalePointsField(final Company company) {
		this.company = company;
		setBuffered(true);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final SalePointsGrid grid = new SalePointsGrid(company) {
			@Override
			protected Container createContainer() {
				final Set<SalePoint> set = getValue() != null ? getValue() : newHashSet();
                itemContainer = new BeanItemContainer<>(SalePoint.class);
				itemContainer.addNestedContainerProperty("regAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
                itemContainer.addAll(set);
                return itemContainer;
			}

            @Override
            public ExtaEditForm<SalePoint> createEditForm(SalePoint salePoint, boolean isInsert) {
                final SalePointEditForm form = new SalePointEditForm(salePoint, company) {
                    @Override
                    protected SalePoint saveObject(SalePoint obj) {
                        if (isInsert)
                            itemContainer.addBean(obj);
                        setValue(newHashSet(itemContainer.getItemIds()));
                        return obj;
                    }
                };
                form.setReadOnly(isReadOnly());
                return form;
            }
        };
		grid.setSizeFull();
        grid.setReadOnly(isReadOnly());

        return grid;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
