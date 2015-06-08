package ru.extas.web.contacts.company;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Component;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import ru.extas.server.references.CategoryService;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.component.ExtaTokenField;

import java.util.ArrayList;
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
public class CompanyCategoriesField extends ExtaCustomField<Set> {

    private final ExtaTokenField tokenField = new ExtaTokenField();

    public CompanyCategoriesField(final String caption) {
        super(caption, "Укажите категории компании");
        setBuffered(true);
        addReadOnlyStatusChangeListener(e -> tokenField.setReadOnly(isReadOnly()));
    }

    @Override
    protected Component initContent() {
        tokenField.setStyleName(ExtaTokenField.STYLE_TOKENFIELD);
        tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setInputPrompt("Выберите категорию...");
        tokenField.setDescription("Выберите одну или несколько категорий компании...");
        tokenField.setRememberNewTokens(false);
        tokenField.setNewTokensAllowed(false);
        tokenField.setWidth(getWidth(), getWidthUnits());
        tokenField.setHeight(getHeight(), getHeightUnits());

        tokenField.addValueChangeListener(event -> {
            final Set selected = (Set) tokenField.getValue();
            setValue(selected);
            refreshContainer(selected);
        });

        final Set<String> set = getValue() != null ? getValue() : null;
        if (set != null) {
            tokenField.setValue(newHashSet(set));
        }
        refreshContainer(set);

        return tokenField;
    }

    private void refreshContainer(final Set<String> selected) {
        final Collection<String> categories = new ArrayList(lookup(CategoryService.class).loadCompanyCategories());
        if (selected != null)
            categories.removeAll(selected);
        tokenField.setContainerDataSource(CollectionContainer.fromBeans(categories));
    }

    @Override
    public void setWidth(final float width, final Unit unit) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setWidth(width, unit));
        super.setWidth(width, unit);
    }

    @Override
    public void setHeight(final String height) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setHeight(height));
        super.setHeight(height);
    }

    @Override
    public void setWidth(final String width) {
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
    public void setHeight(final float height, final Unit unit) {
        Optional.ofNullable(tokenField).ifPresent(f -> f.setHeight(height, unit));
        super.setHeight(height, unit);
    }

    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

}
