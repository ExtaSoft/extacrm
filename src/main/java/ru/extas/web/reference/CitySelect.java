package ru.extas.web.reference;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора города с учетом региона
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:31
 */
public class CitySelect extends ComboBox {
    public CitySelect(String caption) {
        super(caption);
        setDescription("Введите город");
        setInputPrompt("Город");
        setImmediate(true);
        setNewItemsAllowed(true);
        setNullSelectionAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        for (final String item : lookup(SupplementService.class).loadCities())
            addItem(item);

    }

    public CitySelect() {
        this("Город");
    }
}
