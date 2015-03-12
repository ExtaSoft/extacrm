package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.server.motor.MotorBrandRepository;
import ru.extas.server.motor.MotorModelRepository;

import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле выбора модели техники
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 15:36
 * @version $Id: $Id
 * @since 0.3
 */
public class MotorModelSelect extends ComboBox {

    /**
     * <p>Constructor for MotorBrandSelect.</p>
     */
    public MotorModelSelect() {
        this("Модель техники");
    }

    /**
     * <p>Constructor for MotorBrandSelect.</p>
     *
     * @param caption a {@link String} object.
     */
    public MotorModelSelect(final String caption) {
        super(caption);

        setDescription("Укажите модель техники");
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
     * @param type a {@link String} object.
     */
    protected void fillItems(final String type, final String brand) {
        final MotorModelRepository modelRepository = lookup(MotorModelRepository.class);
        final List<String> brands;
        if(type == null || brand == null)
            brands = modelRepository.loadAllNames();
        else
            brands = modelRepository.loadNamesByTypeAndBrand(type);

        final String curBrand = (String) getValue();
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
     * @param typeField a {@link MotorTypeSelect} object.
     */
    public void linkToType(final MotorTypeSelect typeField) {
        typeField.addValueChangeListener(event -> {
            final Property prop = event.getProperty();
            if(prop != null) {
                final String type = (String) prop.getValue();
                fillItems(type);
            }
        });
    }
}
