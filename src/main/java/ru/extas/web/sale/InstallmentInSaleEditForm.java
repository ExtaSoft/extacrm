package ru.extas.web.sale;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.sale.ProdInstallments;
import ru.extas.model.sale.ProductInSale;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.component.*;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdInSaleStateSelect;
import ru.extas.web.product.ProdInstallmentsField;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * @author Valery Orlov
 *         Date: 23.03.2015
 *         Time: 18:24
 */
public class InstallmentInSaleEditForm extends ExtaEditForm<ProductInSale> {
    private final SupplierSer<BigDecimal> priceSupplier;
    private final SupplierSer<String> brandSupplier;
//    private final Sale sale;

    // Компоненты редактирования
    @PropertyId("summ")
    private EditField summField;
    @PropertyId("downpayment")
    private PercentOfField downpaymentField;
    @PropertyId("period")
    private ComboBox periodField;
    @PropertyId("product")
    private ProdInstallmentsField productField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("state")
    private ProdInSaleStateSelect stateField;

    private Label vendorLabel;
    private Label downpaymentLabel;
    private Label periodLabel;
    private Label monthlyPayLabel;

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *  @param caption       a {@link String} object.
     * @param productInSale
     * @param priceSupplier
     * @param brandSupplier
     */
    protected InstallmentInSaleEditForm(String caption, ProductInSale productInSale, SupplierSer<BigDecimal> priceSupplier, SupplierSer<String> brandSupplier) {
        super(caption, productInSale);
        this.priceSupplier = priceSupplier;
        this.brandSupplier = brandSupplier;
    }

    /**
     * <p>initEntity.</p>
     *
     * @param productInSale a TEditObject object.
     */
    @Override
    protected void initEntity(ProductInSale productInSale) {

    }

    /**
     * <p>saveEntity.</p>
     *
     * @param productInSale a TEditObject object.
     */
    @Override
    protected ProductInSale saveEntity(ProductInSale productInSale) {
        return productInSale;
    }

    /**
     * <p>createEditFields.</p>
     *
     * @return a {@link com.vaadin.ui.ComponentContainer} object.
     */
    @Override
    protected ComponentContainer createEditFields() {
        final ExtaFormLayout form = new ExtaFormLayout();

        form.addComponent(new FormGroupHeader("Продукт"));
        productField = new ProdInstallmentsField("Продукт", "Введите название продукта");
        productField.setRequired(true);
        productField.addValueChangeListener(e -> refreshProductFields());
        form.addComponent(productField);

        stateField = new ProdInSaleStateSelect("Статус рассмотрения", "Укажите статус рассмотрения заявки на продукт");
        form.addComponent(stateField);

        form.addComponent(new FormGroupHeader("Характеристики продукта"));
        vendorLabel = new Label();
        vendorLabel.setCaption("Эммитент");
        form.addComponent(vendorLabel);

        downpaymentLabel = new Label();
        downpaymentLabel.setCaption("Первоначальный взнос");
        form.addComponent(downpaymentLabel);

        periodLabel = new Label();
        periodLabel.setCaption("Период рассрочки");
        form.addComponent(periodLabel);

        form.addComponent(new FormGroupHeader("Параметры рассрочки"));
        downpaymentField = new PercentOfField("Первоначальный взнос", "Введите сумму первоначального взноса по рассрочке");
        downpaymentField.setRequired(true);
        form.addComponent(downpaymentField);

        periodField = new ComboBox("Срок рассрочки");
        periodField.setDescription("Введите период рассрочки (длительность рассрочки)");
        periodField.setImmediate(true);
        periodField.setNullSelectionAllowed(false);
        periodField.setRequired(true);
        periodField.setWidth(6, Unit.EM);
        // Наполняем возможными сроками кредита
        fillPeriodFieldItems();
        form.addComponent(periodField);

        summField = new EditField("Сумма рассрочки", "Введите сумму рассрочки (Также может рассчитываться автоматически)");
        summField.setRequired(true);
        form.addComponent(summField);

        // Размер ежемесячного платежа
        monthlyPayLabel = new Label();
        monthlyPayLabel.setCaption("Ежемесячный платеж");
        form.addComponent(monthlyPayLabel);

        // Ответственный со стороны банка
        responsibleField = new EmployeeField("Ответственный сотрудник", "Укажите ответственного со стороны эммитента рассрочки");
        responsibleField.setCompanySupplier(() -> {
            final ProdInstallments product = productField.getValue();
            if (product != null) {
                return product.getVendor();
            } else
                return null;
        });
        form.addComponent(responsibleField);

        // Инициализация валидаторов
        initValidators();
        // Обновление рассчетных полей
        refreshProductFields();
        refreshInstCosts();
        // Инициализация взаимосвязей
        initRelations();

        return form;
    }

    /**
     * Меняется продукт.
     */
    private void productChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики продукта.
        refreshProductFields();
        // Обновляем параметы продукта
        final ProdInstallments installments = productField.getValue();
        if (installments != null) {
            // Наполняем возможными сроками кредита
            fillPeriodFieldItems();

            // Задаем начальные значения параметров (если они не заданы)
            if (downpaymentField.getValue() == null)
                downpaymentField.setValue(
                        installments.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128));
            if (periodField.getValue() == null)
                periodField.setValue((installments.getMaxPeriod()));
            responsibleField.changeCompany();
        }
        // Обновляем(Пересчитываем) стоимость кредита
        refreshInstCosts();
    }

    /**
     * Меняется первоначальный взнос
     */
    private void downPaymentChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем сумму кредита
        final ProdInstallments installments = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final boolean canCalculate = installments != null && price != null && downPayment != null;
        if (canCalculate) {
            final BigDecimal instSum = price.subtract(downPayment);
            summField.setConvertedValue(instSum);
        }
        // Обновляем стоимость
        refreshInstCosts();
    }

    /**
     * Меняется срок кредитования
     */
    private void periodChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем стоимость
        refreshInstCosts();
    }

    /**
     * Меняется сумма
     */
    private void creditSummChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем первоначальный взнос чтобы получить цену техники с учетом новой суммы
        final ProdInstallments inst = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal instSum = (BigDecimal) summField.getConvertedValue();
        if (inst != null && price != null && instSum != null) {
            final BigDecimal downPayment = price.subtract(instSum);
            downpaymentField.setValue(downPayment);
        }
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем стоимость
        refreshInstCosts();
    }

    private void initRelations() {
        productField.addValueChangeListener(this::productChangeListener);
        downpaymentField.setBase(priceSupplier.get());
        downpaymentField.addValueChangeListener(this::downPaymentChangeListener);
        periodField.addValueChangeListener(this::periodChangeListener);
        summField.addValueChangeListener(this::creditSummChangeListener);
        // Наполняем возможными сроками кредита
        fillPeriodFieldItems();
    }

    /**
     * Обновляем(Пересчитываем) стоимость рассрочки
     */
    private void refreshInstCosts() {

        final ProdInstallments installments = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final Number period = (Number) periodField.getValue();
        final boolean canCalculate = installments != null && price != null && downPayment != null && period != null;
        if (canCalculate) {
            monthlyPayLabel.setValue(MessageFormat.format("{0, number, currency}",
                    price.subtract(downPayment).
                            divide(BigDecimal.valueOf(period.intValue()), MathContext.DECIMAL128)));
        }
        // Гасим поля, если нечего в них показывать
        monthlyPayLabel.setVisible(canCalculate);
    }

    /**
     * Добавляем проверки при вводе
     */
    private void initValidators() {
        downpaymentField.addValidator(value -> {
            ProdInstallments prod = productField.getValue();
            if (prod != null) {
                BigDecimal newDownpayment = (BigDecimal) value;
                BigDecimal minDownpayment = prod.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128);
                BigDecimal maxDownpayment = BigDecimal.valueOf(.99).multiply(priceSupplier.get(), MathContext.DECIMAL128);
                if (newDownpayment.compareTo(minDownpayment) < 0 ||
                        newDownpayment.compareTo(maxDownpayment) > 0) {
                    throw new Validator.InvalidValueException(
                            MessageFormat.format(
                                    "Недопустимая сумма первоначального взноса. " +
                                            "Первоначальный взнос должен быть в пределах " +
                                            "от {0, number, currency} до {1, number, currency}",
                                    minDownpayment, maxDownpayment));
                }
            }
        });
    }

    private void fillPeriodFieldItems() {
        // Наполняем возможными сроками кредита
        final ProdInstallments installments = productField.getValue();
        if (installments != null) {
            final int start = 1;
            final int end = installments.getMaxPeriod();
            final int step = 1;
            final Object curValue = periodField.getValue();
            periodField.removeAllItems();
            for (int i = start; i <= end; i += step) {
                periodField.addItem(i);
                periodField.setItemCaption(i, MessageFormat.format("{0} мес.", i));
            }
            periodField.setValue(curValue);
        }
    }

    /**
     * Обновляем характеристики продукта.
     */
    public void refreshProductFields() {
        final ProdInstallments installments = productField.getValue();
        final boolean canShowDetails = installments != null;
        if (canShowDetails) {
            final BeanItem<ProdInstallments> beanItem = new BeanItem<>(Optional.ofNullable(installments).orElse(new ProdInstallments()));
            beanItem.addNestedProperty("vendor.name");

            vendorLabel.setPropertyDataSource(beanItem.getItemProperty("vendor.name"));
            downpaymentLabel.setValue(
                    MessageFormat.format("от {0, number, #,##.##%} до {1, number, percent}",
                            installments.getMinDownpayment(), .99));
            periodLabel.setValue(
                    MessageFormat.format("от {0} до {1} мес.",
                            1, installments.getMaxPeriod()));
        }
        vendorLabel.setVisible(canShowDetails);
        downpaymentLabel.setVisible(canShowDetails);
        periodLabel.setVisible(canShowDetails);
    }
}
