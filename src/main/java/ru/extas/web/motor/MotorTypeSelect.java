package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.motor.MotorTypeRepository;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор типа техники (справочник)
 *
 * @author Valery Orlov
 * Date: 23.10.13
 * Time: 15:09
 * @version $Id: $Id
 * @since 0.3
 */
public class MotorTypeSelect extends ComboBox {
    /**
     * <p>Constructor for MotorTypeSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public MotorTypeSelect(final String caption) {
        super(caption);
        setDescription("Укажите тип техники");
        setInputPrompt("Выберите...");
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setImmediate(true);
        setScrollToSelectedItem(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(13, Unit.EM);
        fillItems(null);
    }

    /**
     * <p>Constructor for MotorTypeSelect.</p>
     */
    public MotorTypeSelect() {
        this("Тип техники");
    }

    public void linkToBrand(MotorBrandSelect brandField) {
        brandField.addValueChangeListener(event -> {
            final Property prop = event.getProperty();
            if (prop != null) {
                final String brand = (String) prop.getValue();
                fillItems(brand);
            }
        });
    }

    private void fillItems(String brand) {
        final MotorTypeRepository typeRepository = lookup(MotorTypeRepository.class);
        final List<String> types;
        if (isNullOrEmpty(brand))
            types = typeRepository.loadAllNames();
        else
            types = typeRepository.loadByBrand(brand);

        final String curType = (String) getValue();
        removeAllItems();
        for (final String item : types)
            addItem(item);

        if (curType != null) {
            if (!types.contains(curType))
                setValue(null);
            else
                setValue(curType);
        }
    }

}
