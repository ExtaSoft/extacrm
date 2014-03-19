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
 * @version $Id: $Id
 */
public class MotorBrandSelect extends ComboBox {

    /**
     * <p>Constructor for MotorBrandSelect.</p>
     */
    public MotorBrandSelect() {
        this("Марка техники");
    }

    /**
     * <p>Constructor for MotorBrandSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
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
