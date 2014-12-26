package ru.extas.web.sale;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import ru.extas.server.product.ProdCreditRepository;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PercentOfField;
import ru.extas.web.commons.window.OkCancelWindow;

import java.text.MessageFormat;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма кредитного калькулятора
 *
 * @author Valery Orlov
 *         Date: 24.12.2014
 *         Time: 15:45
 */
public class LoanCalculatorForm extends OkCancelWindow {

    private EditField priceField;
    private PercentOfField downpaymentField;
    private ComboBox periodField;
    private EditField summField;

    public LoanCalculatorForm() {
        super("Рассчет кредита и подбор кредитного продукта");

        final VerticalLayout mainLayout = new VerticalLayout();

        final ExtaFormLayout paramForm = new ExtaFormLayout();
        paramForm.addComponent(new FormGroupHeader("Параметры кредита"));
        priceField = new EditField("Стоимость техники", "Введите стоимость техники");
        priceField.setRequired(true);
        downpaymentField = new PercentOfField("Первоначальный взнос", "Введите сумму первоначального взноса по кредиту");
        downpaymentField.setRequired(true);
        paramForm.addComponent(downpaymentField);

        periodField = new ComboBox("Срок кредитования");
        periodField.setDescription("Введите период кредитования (срок кредита)");
        periodField.setImmediate(true);
        periodField.setNullSelectionAllowed(false);
        periodField.setRequired(true);
        periodField.setWidth(6, Unit.EM);
        // Наполняем возможными сроками кредита
        fillPeriodFieldItems();
        paramForm.addComponent(periodField);

        summField = new EditField("Сумма кредита", "Введите сумму кредита (Также может рассчитываться автоматически)");
        summField.setRequired(true);
        paramForm.addComponent(summField);


        mainLayout.addComponent(paramForm);

        setContent(mainLayout);
    }

    private void fillPeriodFieldItems() {
        // Наполняем возможными сроками кредита
        ProdCreditRepository rep = lookup(ProdCreditRepository.class);
        final int start = rep.getMinPeriod();
        final int end = rep.getMaxPeriod();
        final int step = rep.getPeriodMinStep();
        final Object curValue = periodField.getValue();
        periodField.removeAllItems();
        for (int i = start; i <= end; i += step) {
            periodField.addItem(i);
            periodField.setItemCaption(i, MessageFormat.format("{0} мес.", i));
        }
        periodField.setValue(curValue);
    }

}
