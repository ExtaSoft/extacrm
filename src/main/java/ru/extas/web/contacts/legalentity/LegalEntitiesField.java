package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.*;

import java.util.Set;

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

	private final Company company;
    private BeanItemContainer<LegalEntity> itemContainer;

    /**
	 * <p>Constructor for LegalEntitiesField.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public LegalEntitiesField(final Company company) {
		this.company = company;
		setBuffered(true);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final LegalEntitiesGrid grid = new LegalEntitiesGrid(company) {
			@Override
			protected Container createContainer() {
				final Set<LegalEntity> list = getValue() != null ? getValue() : newHashSet();
                itemContainer = new BeanItemContainer<>(LegalEntity.class);
				itemContainer.addNestedContainerProperty("regAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
                itemContainer.addAll(list);
                return itemContainer;
			}

            @Override
            public ExtaEditForm<LegalEntity> createEditForm(LegalEntity legalEntity, boolean isInsert) {
                final LegalEntityEditForm form = new LegalEntityEditForm(legalEntity, company) {
                    @Override
                    protected LegalEntity saveObject(LegalEntity obj) {
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
