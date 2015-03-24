package ru.extas.web.reference;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.references.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора для региона
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 18:04
 * @version $Id: $Id
 * @since 0.3
 */
public class RegionSelect extends ComboBox {

    /**
     * <p>Constructor for RegionSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public RegionSelect(final String caption) {
        super(caption);

        setDescription("Укажите регион проживания");
        setInputPrompt("Выберите регион...");
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setImmediate(true);
        setScrollToSelectedItem(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(15, Unit.EM);
        lookup(SupplementService.class).loadRegions().forEach((final String item) -> addItem(item));
    }

    /**
     * <p>Constructor for RegionSelect.</p>
     */
    public RegionSelect() {
        this("Регион");
    }
}
