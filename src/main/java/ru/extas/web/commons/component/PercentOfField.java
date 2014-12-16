package ru.extas.web.commons.component;

import com.vaadin.ui.*;
import ru.extas.web.commons.converters.StringToMoneyConverter;
import ru.extas.web.commons.converters.StringToPercentConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле ввода позволяющее отображать и вводить сумму в виде процентов от базовой
 *
 * Created by valery on 13.12.14.
 */
public class PercentOfField extends CustomField<BigDecimal> {

    private EditField inputField;
    private Label alterLabel;

    private enum Mode {
        PERCENT,
        VALUE
    }

    private Mode mode = Mode.VALUE;
    private BigDecimal base;


    public PercentOfField(String caption, String description) {
        setCaption(caption);
        setDescription(description);
        addReadOnlyStatusChangeListener(e -> inputField.setReadOnly((Boolean) e.getProperty().getValue()));
        addValueChangeListener(e -> updateState());
    }

    private void updateState() {
        BigDecimal value = getValue();
        if (value != null && base != null) {
            switch (mode) {
                case PERCENT:
                    inputField.setConverter(lookup(StringToPercentConverter.class));
                    inputField.setConvertedValue(value.divide(base, MathContext.DECIMAL128));
                    alterLabel.setValue(MessageFormat.format("{0, number, currency}", value));
                    break;
                case VALUE:
                    inputField.setConverter(lookup(StringToMoneyConverter.class));
                    inputField.setConvertedValue(value);
                    alterLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", value.divide(base, MathContext.DECIMAL128)));
                    break;
            }
        }
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
        updateState();
    }

    @Override
    protected Component initContent() {
        HorizontalLayout layout = new HorizontalLayout();

        inputField = new EditField(null);
        inputField.setReadOnly(isReadOnly());
        layout.addComponent(inputField);

        Button modeBtn = new Button("Режим");
        layout.addComponent(modeBtn);

        alterLabel = new Label();
        layout.addComponent(alterLabel);

        return layout;
    }

    @Override
    public Class<? extends BigDecimal> getType() {
        return BigDecimal.class;
    }
}
