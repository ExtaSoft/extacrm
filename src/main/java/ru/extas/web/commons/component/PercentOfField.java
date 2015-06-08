package ru.extas.web.commons.component;

import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.converters.StringToMoneyConverter;
import ru.extas.web.commons.converters.StringToPercentConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.Optional;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле ввода позволяющее отображать и вводить сумму в виде процентов от базовой
 * <p>
 * Created by valery on 13.12.14.
 */
public class PercentOfField extends ExtaCustomField<BigDecimal> {

    private EditField inputField;
    private Label alterLabel;

    private enum Mode {
        PERCENT,
        VALUE
    }

    private Mode mode = Mode.VALUE;
    private BigDecimal base;


    public PercentOfField(final String caption, final String description) {
        super(caption, description);
        addValueChangeListener(e -> updateState());
    }

    private void updateState() {
        final BigDecimal value = getValue();
        if (value != null && base != null && !base.equals(BigDecimal.ZERO) && inputField != null) {
            switch (mode) {
                case PERCENT:
                    inputField.setConverter(lookup(StringToPercentConverter.class));
                    inputField.setWidth(5, Unit.EM);
                    inputField.setConvertedValue(value.divide(base, MathContext.DECIMAL128));
                    alterLabel.setValue(MessageFormat.format("{0, number, currency}", value));
                    break;
                case VALUE:
                    inputField.setConverter(lookup(StringToMoneyConverter.class));
                    inputField.setWidth(7, Unit.EM);
                    inputField.setConvertedValue(value);
                    alterLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", value.divide(base, MathContext.DECIMAL128)));
                    break;
            }
        }
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(final BigDecimal base) {
        this.base = base;
        updateState();
    }

    @Override
    protected Component initContent() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setSpacing(true);

        inputField = new EditField(null);
        inputField.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
        inputField.setReadOnly(isReadOnly());
        inputField.setConverter(BigDecimal.class);
        inputField.addValueChangeListener(e -> {
            final BigDecimal value = Optional.ofNullable((BigDecimal) inputField.getConvertedValue()).orElse(BigDecimal.ZERO);
            if (mode == Mode.PERCENT) setValue(value.multiply(base, MathContext.DECIMAL128));
            else if (mode == Mode.VALUE) setValue(value);
        });
        layout.addComponent(inputField);

        final Button modeBtn = new Button("Режим", FontAwesome.EXCHANGE);
        modeBtn.setDescription("Нажмите чтобы переключить режим поля ввода с суммы на проценты или обратно.");
        modeBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        modeBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        modeBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        modeBtn.addClickListener(e -> {
            if (mode == Mode.PERCENT) mode = Mode.VALUE;
            else if (mode == Mode.VALUE) mode = Mode.PERCENT;
            updateState();
        });
        layout.addComponent(modeBtn);

        alterLabel = new Label();
        alterLabel.addStyleName(ExtaTheme.LABEL_COLORED);
        layout.addComponent(alterLabel);

        updateState();
        addReadOnlyStatusChangeListener(e -> inputField.setReadOnly(isReadOnly()));
        return layout;
    }

    @Override
    public Class<? extends BigDecimal> getType() {
        return BigDecimal.class;
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        super.commit();
    }
}
