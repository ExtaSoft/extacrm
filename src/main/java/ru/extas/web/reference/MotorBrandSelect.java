package ru.extas.web.reference;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора марки техники
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 15:36
 */
public class MotorBrandSelect extends ComboBox {

    public MotorBrandSelect() {
        this("Марка техники");
    }

    public MotorBrandSelect(String caption) {
        super(caption);

        setDescription("Укажите марку страхуемой техники");
        setInputPrompt("Выберите или начните ввод...");
        setImmediate(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(13, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadMotorBrands())
            addItem(item);
    }
}
