package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.motor.MotorBrandRepository;

import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора марки техники
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 15:36
 * @version $Id: $Id
 * @since 0.3
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

        setDescription("Укажите марку техники");
        setInputPrompt("Выберите...");
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setImmediate(true);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(13, Unit.EM);
        fillItems(null);
    }

    /**
     * <p>fillItems.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    protected void fillItems(String type) {
        MotorBrandRepository brandRepository = lookup(MotorBrandRepository.class);
        List<String> brands;
        if(type == null)
            brands = brandRepository.loadAllNames();
        else
            brands = brandRepository.loadNamesByType(type);

        String curBrand = (String) getValue();
        removeAllItems();
        for (final String item : brands)
            addItem(item);

        if(curBrand != null) {
            if(!brands.contains(curBrand))
                setValue(null);
            else
                setValue(curBrand);
        }

    }

    /**
     * <p>linkToType.</p>
     *
     * @param typeField a {@link ru.extas.web.motor.MotorTypeSelect} object.
     */
    public void linkToType(MotorTypeSelect typeField) {
        typeField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Property prop = event.getProperty();
                if(prop != null) {
                    String type = (String) prop.getValue();
                    fillItems(type);
                }
            }
        });
    }
}
