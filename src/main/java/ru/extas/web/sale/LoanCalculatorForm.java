package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.PercentOfField;
import ru.extas.web.commons.window.OkCancelWindow;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdCreditField;

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

        mainLayout.addComponent(paramForm);

        setContent(mainLayout);
    }
}
