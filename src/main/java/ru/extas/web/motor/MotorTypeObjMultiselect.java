package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.vaadin.tokenfield.TokenField;
import ru.extas.model.motor.MotorType;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.component.ExtaTokenField;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует редактирование списка тип техники
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorTypeObjMultiselect extends CustomField<Set> {


    /**
     * <p>Constructor for MotorBrandMultiselect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public MotorTypeObjMultiselect(final String caption) {
        setBuffered(true);
        setCaption(caption);
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
        tokenField.setInputPrompt("Введите или выберите тип техники...");
        tokenField.setDescription("Введите или выберите тип техники...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        tokenField.setInputSizeFull();
        //tokenField.setInputWidth(13, Unit.EX);
        //tokenField.setTokenInsertPosition(TokenField.InsertPosition.BEFORE);

        final ExtaDataContainer<MotorType> container = new ExtaDataContainer<>(MotorType.class);
        container.sort(new Object[]{"name"}, new boolean[]{true});
        tokenField.setContainerDataSource(container);
        tokenField.setTokenCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tokenField.setTokenCaptionPropertyId("name");
        tokenField.addValueChangeListener(event -> {
            final Set selected = (Set) tokenField.getValue();
            final Set objValue = newHashSet();
            for (final Object id : selected)
                objValue.add(container.getItem(id).getEntity());
            setValue(objValue);
        });
        final Property dataSource = getPropertyDataSource();
        final Set<MotorType> set = dataSource != null ? (Set<MotorType>) dataSource.getValue() : null;
        if (set != null) {
            final Set idValue = newHashSet();
            for(final MotorType type : set)
                idValue.add(type.getId());
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
