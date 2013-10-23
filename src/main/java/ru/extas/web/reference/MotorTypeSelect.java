package ru.extas.web.reference;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.SupplementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор типа техники (справочник)
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 15:09
 */
public class MotorTypeSelect extends ComboBox {
    public MotorTypeSelect(String caption) {
        super(caption);
        setDescription("Укажите тип техники");
        setInputPrompt("Выберите или начните ввод...");
        setImmediate(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(13, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadMotorTypes())
            addItem(item);
    }

    public MotorTypeSelect() {
        this("Тип техники");
    }
}
