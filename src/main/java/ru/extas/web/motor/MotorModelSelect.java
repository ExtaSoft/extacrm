package ru.extas.web.motor;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
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
        setInvalidAllowed(true);
        setNewItemsAllowed(true);
        setNewItemHandler(newValue -> {
            addItem(newValue);
            setValue(newValue);
        });
        setFilteringMode(FilteringMode.CONTAINS);
        setWidth(15, Unit.EM);
        addValueChangeListener(e -> {
            final String value = (String) e.getProperty().getValue();
            if (value != null && !containsId(value)) {
                addItem(value);
                //setValue(value);
            }
        });
    }

    /**
     * <p>fillItems.</p>
     *
     * @param type a {@link String} object.
     */
    protected void fillItems(final String type, final String brand) {
        final MotorModelRepository modelRepository = lookup(MotorModelRepository.class);
        final String curModel = (String) getValue();
        if (type != null && brand != null) {
            final List<String> models = modelRepository.loadNamesByTypeAndBrand(type, brand);

            removeAllItems();
            for (final String item : models)
                addItem(item);
        }
        if (curModel != null) {
            setValue(curModel);
        }
    }

    public void linkToTypeAndBrand(final MotorTypeSelect typeField, final MotorBrandSelect brandField) {
        final ValueChangeListener listener = event -> {
            final String type = (String) typeField.getValue();
            final String brand = (String) brandField.getValue();
            fillItems(type, brand);
        };
        typeField.addValueChangeListener(listener);
        brandField.addValueChangeListener(listener);
    }
}
