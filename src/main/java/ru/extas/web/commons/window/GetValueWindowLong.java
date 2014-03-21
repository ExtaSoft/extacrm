package ru.extas.web.commons.window;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RangeValidator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import ru.extas.web.commons.component.EditField;

import java.io.Serializable;

/**
 * Показывает окно для ввода целого числа
 *
 * @author Valery Orlov
 *         Date: 19.08.13
 *         Time: 17:17
 * @version $Id: $Id
 * @since 0.3
 */
public class GetValueWindowLong extends AbstractEditForm<GetValueWindowLong.ValueContainer> {

    private ValueContainer valueContainer;
    // Компоненты редактирования
    @PropertyId("value")
    private TextField valueField;

    /**
     * <p>Constructor for GetValueWindowLong.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param defaultValue a {@link java.lang.Long} object.
     */
    public GetValueWindowLong(final String caption, Long defaultValue) {
        super(caption, new BeanItem<>(new ValueContainer(defaultValue)));
    }

    /**
     * <p>Constructor for GetValueWindowLong.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public GetValueWindowLong(final String caption) {
        this(caption, 0L);
    }

    /**
     * <p>Constructor for GetValueWindowLong.</p>
     *
     * @param defaultValue a {@link java.lang.Long} object.
     */
    public GetValueWindowLong(final Long defaultValue) {
        this("Введите целое число", defaultValue);
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final ValueContainer obj) {
        this.valueContainer = obj;
    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(final ValueContainer obj) {
    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final ValueContainer obj) {
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final ValueContainer obj) {
        final FormLayout form = new FormLayout();

        valueField = new EditField("Значение", "Введите целое число.");
        valueField.setColumns(15);
        valueField.setRequired(true);
        valueField.addValidator(new RangeValidator<>("Неподходящее значение", Long.class, 0L, java.lang.Long.MAX_VALUE));
        //valueField.setConverter(new StringToLongConverter());
        form.addComponent(valueField);

        return form;
    }

    /**
     * <p>getValue.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getValue() {
        return valueContainer.getValue();
    }

    /**
     * @author Valery Orlov
     *         Date: 19.08.13
     *         Time: 18:19
     */
    public static class ValueContainer implements Serializable {

        private Long value;

        public ValueContainer(final Long defaultValue) {
            this.value = defaultValue;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(final Long value) {
            this.value = value;
        }
    }

}
