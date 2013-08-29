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
 */
public class GetValueWindowLongRange extends AbstractEditForm<GetValueWindowLongRange.ValueContainer> {

    private ValueContainer valueContainer;

    // Компоненты редактирования
    @PropertyId("startValue")
    private TextField startValueField;
    @PropertyId("endValue")
    private TextField endValueField;

    public GetValueWindowLongRange(final String caption, final Long startValue, final Long endValue) {
        super(caption, new BeanItem<>(new ValueContainer(startValue, endValue)));
    }

    public GetValueWindowLongRange(final String caption) {
        this(caption, 0L, 0L);
    }

    public GetValueWindowLongRange(final Long startValue, final Long endValue) {
        this("Введите диапазон целых чисел", startValue, endValue);
    }

    @Override
    protected void initObject(final ValueContainer obj) {
        this.valueContainer = obj;
    }

    @Override
    protected void saveObject(final ValueContainer obj) {
    }

    @Override
    protected void checkBeforeSave(final ValueContainer obj) {
    }

    @Override
    protected ComponentContainer createEditFields(final ValueContainer obj) {
        final FormLayout form = new FormLayout();

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

    public Long getStartValue() {
        return valueContainer.getStartValue();
    }

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
