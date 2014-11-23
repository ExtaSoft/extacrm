package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import org.vaadin.tokenfield.TokenField;
import ru.extas.server.motor.MotorBrandRepository;
import ru.extas.web.commons.component.ExtaTokenField;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует редактирование списка брендов
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:44
 * @version $Id: $Id
 * @since 0.3
 */
public class MotorBrandMultiselect extends CustomField<Set> {


    /**
     * <p>Constructor for MotorBrandMultiselect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public MotorBrandMultiselect(final String caption) {
        setBuffered(true);
        setCaption(caption);
    }

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

        final ExtaTokenField tokenField = new ExtaTokenField();
        addReadOnlyStatusChangeListener(e -> tokenField.setReadOnly(isReadOnly()));
        tokenField.setStyleName(ExtaTokenField.STYLE_TOKENFIELD);
        tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setInputPrompt("Введите или выберите бренд...");
        tokenField.setDescription("Введите или выберите бренд...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        //tokenField.setInputSizeFull();
        //tokenField.setInputWidth(13, Unit.EX);
        //tokenField.setTokenInsertPosition(TokenField.InsertPosition.BEFORE);
        tokenField.addValueChangeListener(event -> {
            final Set selected = (Set) tokenField.getValue();
            setValue(selected);
        });

		final Property dataSource = getPropertyDataSource();
		final Set<String> set = dataSource != null ? (Set<String>) dataSource.getValue() : null;
		if (set != null) {
			tokenField.setValue(newHashSet(set));
		}
        final Collection<String> brands = lookup(MotorBrandRepository.class).loadAllNames();
        tokenField.setContainerDataSource(CollectionContainer.fromBeans(brands));

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
