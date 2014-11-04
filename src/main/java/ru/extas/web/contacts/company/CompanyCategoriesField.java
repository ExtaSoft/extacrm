package ru.extas.web.contacts.company;

import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import org.vaadin.tokenfield.TokenField;
import ru.extas.server.references.CategoryService;
import ru.extas.server.references.SupplementService;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Позволяет редактировать набор категорий компании
 *
 * @author Valery Orlov
 *         Date: 04.11.2014
 *         Time: 14:18
 */
public class CompanyCategoriesField extends CustomField<Set> {

    private TokenField tokenField = new TokenField();

    public CompanyCategoriesField(final String caption) {
        setCaption(caption);
        setBuffered(true);
    }

    @Override
    protected Component initContent() {
        tokenField.setStyleName(TokenField.STYLE_TOKENFIELD);
        tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setInputPrompt("Выберите категорию...");
        tokenField.setDescription("Выберите одну лили несколько категорий компании...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        tokenField.setWidth(getWidth(), getWidthUnits());
        tokenField.setHeight(getHeight(), getHeightUnits());

        tokenField.addValueChangeListener(event -> {
            final Set selected = (Set) tokenField.getValue();
            setValue(selected);
        });

        final Set<String> set = getValue() != null ? getValue() : null;
        if (set != null) {
            tokenField.setValue(newHashSet(set));
        }
        final Collection<String> regions = lookup(CategoryService.class).loadCompanyCategories();
        tokenField.setContainerDataSource(CollectionContainer.fromBeans(regions));

        return tokenField;
    }

    @Override
    public void setWidth(float width, Unit unit) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setWidth(width, unit));
        super.setWidth(width, unit);
    }

    @Override
    public void setHeight(String height) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setHeight(height));
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setWidth(width));
        super.setWidth(width);
    }

    @Override
    public void setHeightUndefined() {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setHeightUndefined());
        super.setHeightUndefined();
    }

    @Override
    public void setWidthUndefined() {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setWidthUndefined());
        super.setWidthUndefined();
    }

    @Override
    public void setSizeUndefined() {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setSizeUndefined());
        super.setSizeUndefined();
    }

    @Override
    public void setSizeFull() {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setSizeFull());
        super.setSizeFull();
    }

    @Override
    public void setHeight(float height, Unit unit) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setHeight(height, unit));
        super.setHeight(height, unit);
    }

    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

}
