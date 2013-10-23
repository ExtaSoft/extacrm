package ru.extas.web.reference;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора для региона
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 18:04
 */
public class RegionSelect extends ComboBox {

    public RegionSelect(String caption) {
        super(caption);

        setDescription("Укажите регион проживания");
        setInputPrompt("Выберите или начните ввод...");
        setImmediate(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(18, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadRegions())
            addItem(item);
    }

    public RegionSelect() {
        this("Регион");
    }
}
