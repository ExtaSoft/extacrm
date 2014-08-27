package ru.extas.web.commons.window;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.RangeValidator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;

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
public class GetValueWindowLongRange extends ExtaEditForm<GetValueWindowLongRange.ValueContainer> {

    private ValueContainer valueContainer;

    // Компоненты редактирования
    @PropertyId("startValue")
    private TextField startValueField;
    @PropertyId("endValue")
    private TextField endValueField;

    /**
     * <p>Constructor for GetValueWindowLongRange.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param startValue a {@link java.lang.Long} object.
     * @param endValue a {@link java.lang.Long} object.
     */
    public GetValueWindowLongRange(final String caption, final Long startValue, final Long endValue) {
        super(caption, new BeanItem<>(new ValueContainer(startValue, endValue)));
    }

    /**
     * <p>Constructor for GetValueWindowLongRange.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public GetValueWindowLongRange(final String caption) {
        this(caption, 0L, 0L);
    }

    /**
     * <p>Constructor for GetValueWindowLongRange.</p>
     *
     * @param startValue a {@link java.lang.Long} object.
     * @param endValue a {@link java.lang.Long} object.
     */
    public GetValueWindowLongRange(final Long startValue, final Long endValue) {
        this("Введите диапазон целых чисел", startValue, endValue);
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
        final FormLayout form = new ExtaFormLayout();

        startValueField = new EditField("Начальное значение", "Введите целое число.");
        startValueField.setColumns(15);
        startValueField.setRequired(true);
        startValueField.addValidator(new RangeValidator<>("Неподходящее значение", Long.class, 0L, Long.MAX_VALUE));
        form.addComponent(startValueField);

        endValueField = new EditField("Конечное значение", "Введите целое число.");
        endValueField.setColumns(15);
        endValueField.setRequired(true);
        endValueField.addValidator(new RangeValidator<>("Неподходящее значение", Long.class, 0L, Long.MAX_VALUE));
        form.addComponent(endValueField);

        return form;
    }

    /**
     * <p>getStartValue.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getStartValue() {
        return valueContainer.getStartValue();
    }

    /**
     * <p>getEndValue.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getEndValue() {
        return valueContainer.getEndValue();
    }

    /**
     * @author Valery Orlov
     *         Date: 19.08.13
     *         Time: 18:19
     */
    public static class ValueContainer implements Serializable {

        private Long startValue;
        private Long endValue;

        public ValueContainer(final Long startValue, final Long endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        public Long getEndValue() {
            return endValue;
        }

        public void setEndValue(final Long endValue) {
            this.endValue = endValue;
        }

        public Long getStartValue() {
            return startValue;
        }

        public void setStartValue(final Long startValue) {
            this.startValue = startValue;
        }
    }

}
