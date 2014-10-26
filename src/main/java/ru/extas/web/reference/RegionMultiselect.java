package ru.extas.web.reference;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import org.vaadin.tokenfield.TokenField;
import ru.extas.server.references.SupplementService;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует редактирование списка регионов
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:44
 * @version $Id: $Id
 * @since 0.3
 */
public class RegionMultiselect extends CustomField<Set> {


    /**
     * <p>Constructor for RegionMultiselect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public RegionMultiselect(final String caption) {
        setBuffered(true);
        setCaption(caption);
    }

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

        final TokenField tokenField = new TokenField();
        tokenField.setStyleName(TokenField.STYLE_TOKENFIELD);
        tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setInputPrompt("Введите или выберите регион...");
        tokenField.setDescription("Введите или выберите регион...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        tokenField.setInputSizeFull();
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
        final Collection<String> regions = lookup(SupplementService.class).loadRegions();
        tokenField.setContainerDataSource(CollectionContainer.fromBeans(regions));

        final HorizontalLayout layout = new HorizontalLayout(tokenField);
        //layout.setSizeFull();
        //layout.setMargin(new MarginInfo(true, false,false,false));
        return layout;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
