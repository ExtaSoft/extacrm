package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.motor.MotorBrand;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.component.ExtaTokenField;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Реализует редактирование списка брендов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorBrandObjMultiselect extends ExtaCustomField<Set> {


    /**
     * <p>Constructor for MotorBrandMultiselect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public MotorBrandObjMultiselect(final String caption) {
        super(caption, "");
        setBuffered(true);
    }

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

        final VerticalLayout lo = new VerticalLayout();
        lo.setSpacing(true);
        final ExtaTokenField tokenField = new ExtaTokenField(lo);
        addReadOnlyStatusChangeListener(e -> tokenField.setReadOnly(isReadOnly()));
        tokenField.setStyleName(ExtaTokenField.STYLE_TOKENFIELD);
        tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setInputPrompt("Введите или выберите бренд...");
        tokenField.setDescription("Введите или выберите бренд...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        tokenField.setInputSizeFull();
        //tokenField.setInputWidth(13, Unit.EX);
        //tokenField.setTokenInsertPosition(TokenField.InsertPosition.BEFORE);

        final ExtaDbContainer<MotorBrand> container = new ExtaDbContainer<>(MotorBrand.class);
        container.sort(new Object[]{"name"}, new boolean[]{true});
        tokenField.setContainerDataSource(container);
        tokenField.setTokenCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tokenField.setTokenCaptionPropertyId("name");
        tokenField.addValueChangeListener(event -> {
            final Set selected = (Set) tokenField.getValue();
            final Set objValue = newHashSet();
            for (final Object id : selected)
                objValue.add(extractBean(container.getItem(id)));
            setValue(objValue);
        });
        final Property dataSource = getPropertyDataSource();
        final Set<MotorBrand> set = dataSource != null ? (Set<MotorBrand>) dataSource.getValue() : null;
        if (set != null) {
            final Set idValue = newHashSet();
            for(final MotorBrand brand : set)
                idValue.add(container.getEntityItemId(brand));
            tokenField.setValue(idValue);
        }

        final HorizontalLayout layout = new HorizontalLayout(tokenField);
        //layout.setMargin(new MarginInfo(true, false,false,false));
        return layout;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
