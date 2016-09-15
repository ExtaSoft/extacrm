package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import ru.extas.model.product.ProdCredit;
import ru.extas.model.product.ProdCreditDoc;
import ru.extas.model.product.ProductInstance;
import ru.extas.server.financial.LoanCalculator;
import ru.extas.server.financial.LoanInfo;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PercentOfField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdCreditField;
import ru.extas.web.product.ProdInstanceStateSelect;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 * @author sandarkin
 */
public class LoanInSaleEditForm extends ExtaEditForm<ProductInstance> {

    private final SupplierSer<BigDecimal> priceSupplier;
    private final SupplierSer<String> brandSupplier;
    // Компоненты редактирования
    @PropertyId("summ")
    private EditField summField;
    @PropertyId("downpayment")
    private PercentOfField downpaymentField;
    @PropertyId("period")
    private ComboBox periodField;
    @PropertyId("product")
    private ProdCreditField productField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("state")
    private ProdInstanceStateSelect stateField;
    private Label vendorLabel;
    private Label programTypeLabel;
    private Label sumLabel;
    private Label downpaymentLabel;
    private Label periodLabel;
    private Label percentLabel;
    private Label documentsLabel;
    private Label monthlyPayLabel;
    private Label overpaymentLabel;
    private Label yearlyRiseLabel;
    private Label monthlyRiseLabel;

    public LoanInSaleEditForm(final String caption, final ProductInstance targetObject,
                              final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        super(caption, targetObject);
        this.priceSupplier = priceSupplier;
        this.brandSupplier = brandSupplier;
    }

    /**
     * <p>initEntity.</p>
     *
     */
    @Override
    protected void initEntity(final ProductInstance targetObject) {

    }

    /**
     * <p>saveEntity.</p>
     *
     */
    @Override
    protected ProductInstance saveEntity(final ProductInstance targetObject) {
        return targetObject;
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
        productField = new ProdCreditField("Продукт", "Введите название продукта");
        productField.setRequired(true);
        productField.addValueChangeListener(e -> refreshProductFields());
        form.addComponent(productField);

        stateField = new ProdInstanceStateSelect("Статус рассмотрения", "Укажите статус рассмотрения заявки на продукт");
        form.addComponent(stateField);

        form.addComponent(new FormGroupHeader("Характеристики продукта"));
        vendorLabel = new Label();
        vendorLabel.setCaption("Банк");
        form.addComponent(vendorLabel);

        programTypeLabel = new Label();
        programTypeLabel.setCaption("Тип программы");
        form.addComponent(programTypeLabel);

        sumLabel = new Label();
        sumLabel.setCaption("Сумма кредита");
        form.addComponent(sumLabel);

        downpaymentLabel = new Label();
        downpaymentLabel.setCaption("Первоначальный взнос");
        form.addComponent(downpaymentLabel);

        periodLabel = new Label();
        periodLabel.setCaption("Период кредитования");
        form.addComponent(periodLabel);

        documentsLabel = new Label();
        documentsLabel.setCaption("Пакет документов");
        documentsLabel.setContentMode(ContentMode.HTML);
        form.addComponent(documentsLabel);

        percentLabel = new Label();
        percentLabel.setCaption("Процент");
        form.addComponent(percentLabel);

        form.addComponent(new FormGroupHeader("Параметры кредита"));
        downpaymentField = new PercentOfField("Первоначальный взнос", "Введите сумму первоначального взноса по кредиту");
        downpaymentField.setRequired(true);
        downpaymentField.addValueChangeListener(e -> {
            System.out.print("periodField");
        });
        form.addComponent(downpaymentField);

        periodField = new ComboBox("Срок кредитования");
        periodField.setDescription("Введите период кредитования (срок кредита)");
        periodField.setImmediate(true);
        periodField.setNullSelectionAllowed(false);
        periodField.setRequired(true);
        periodField.setWidth(6, Unit.EM);
        // Наполняем возможными сроками кредита
        fillPeriodFieldItems();
        periodField.addValueChangeListener(e -> {
            System.out.print("periodField");
        });
        form.addComponent(periodField);

        summField = new EditField("Сумма кредита", "Введите сумму кредита (Также может рассчитываться автоматически)");
        summField.setRequired(true);
        form.addComponent(summField);

        // Ответственный со стороны банка
        responsibleField = new EmployeeField("Менеджер банка", "Укажите ответственного со стороны банка");
        responsibleField.setCompanySupplier(() -> {
            final ProdCredit product = productField.getValue();
            if (product != null) {
                return product.getVendor();
            } else
                return null;
        });
        form.addComponent(responsibleField);

        form.addComponent(new FormGroupHeader("Стоимость кредита"));
        // Размер ежемесячного платежа
        monthlyPayLabel = new Label();
        monthlyPayLabel.setCaption("Ежемесячный платеж");
        form.addComponent(monthlyPayLabel);
        // Переплата в рамках программы
        overpaymentLabel = new Label();
        overpaymentLabel.setCaption("Переплата");
        form.addComponent(overpaymentLabel);
        // Среднегодовое удорожание кредита
        yearlyRiseLabel = new Label();
        yearlyRiseLabel.setCaption("Среднегодовое удорожание");
        form.addComponent(yearlyRiseLabel);
        // Среднемесячное удорожание кредита
        monthlyRiseLabel = new Label();
        monthlyRiseLabel.setCaption("Среднемесячное удорожание");
        form.addComponent(monthlyRiseLabel);

        // Инициализация валидаторов
        initValidators();
        // Обновление рассчетных полей
        refreshProductFields();
        refreshCreditCosts();
        // Инициализация взаимосвязей
        initRelations();

        return form;
    }

    private void initRelations() {
        productField.addValueChangeListener(this::productChangeListener);
//        productField.addValueChangeListener(super::updateSubValue);
        downpaymentField.setBase(priceSupplier.get());
        downpaymentField.addValueChangeListener(this::downPaymentChangeListener);
//        downpaymentField.addValueChangeListener(super::updateSubValue);
        periodField.addValueChangeListener(this::periodChangeListener);
//        periodField.addValueChangeListener(super::updateSubValue);
        summField.addValueChangeListener(this::creditSummChangeListener);
//        summField.addValueChangeListener(super::updateSubValue);
        // Наполняем возможными сроками кредита
        fillPeriodFieldItems();
    }

    /**
     * Меняется продукт.
     */
    private void productChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики продукта.
        refreshProductFields();
        // Обновляем параметы продукта
        final ProdCredit credit = productField.getValue();
        if (credit != null) {
            // Наполняем возможными сроками кредита
            fillPeriodFieldItems();

            // Задаем начальные значения параметров (если они не заданы)
            if (downpaymentField.getValue() == null)
                downpaymentField.setValue(
                        credit.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128));
            if (periodField.getValue() == null)
                periodField.setValue((credit.getMinPeriod()));
            responsibleField.changeCompany();
        }
        // Обновляем(Пересчитываем) стоимость кредита
        refreshCreditCosts();
    }

    private void fillPeriodFieldItems() {
        // Наполняем возможными сроками кредита
        final ProdCredit credit = productField.getValue();
        if (credit != null) {
            final int start = credit.getMinPeriod();
            final int end = credit.getMaxPeriod();
            final int step = credit.getStep();
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
     * Меняется первоначальный взнос
     */
    private void downPaymentChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем сумму кредита
        final ProdCredit credit = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final boolean canCalculate = credit != null && price != null && downPayment != null;
        if (canCalculate) {
            final BigDecimal creditSum = lookup(LoanCalculator.class).calcCreditSum(credit, price, downPayment);
            summField.setConvertedValue(creditSum);
        }
        // Обновляем стоимость
        refreshCreditCosts();
    }

    /**
     * Меняется срок кредитования
     */
    private void periodChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем стоимость
        refreshCreditCosts();
    }

    /**
     * Меняется сумма
     */
    private void creditSummChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
        // Обновляем первоначальный взнос чтобы получить цену техники с учетом новой суммы
        final ProdCredit credit = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal creditSum = (BigDecimal) summField.getConvertedValue();
        if (credit != null && price != null && creditSum != null) {
            final BigDecimal downPayment = lookup(LoanCalculator.class).calcDownPayment(credit, price, creditSum);
            downpaymentField.setValue(downPayment);
        }
        // Обновляем характеристики
        refreshProductFields();
        // Обновляем стоимость
        refreshCreditCosts();
    }

    /**
     * Добавляем проверки при вводе
     */
    private void initValidators() {
        downpaymentField.addValidator(value -> {
            final ProdCredit prod = productField.getValue();
            final BigDecimal price = priceSupplier.get();
            if (prod != null && price != null) {
                final BigDecimal newDownpayment = (BigDecimal) value;
                final BigDecimal minDownpayment = prod.getMinDownpayment().multiply(price, MathContext.DECIMAL128);
                final BigDecimal maxDownpayment = prod.getMaxDownpayment().multiply(price, MathContext.DECIMAL128);
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
        summField.addValidator(value -> {
            final ProdCredit prod = productField.getValue();
            if (prod != null) {
                final BigDecimal newSumm = (BigDecimal) value;
                final BigDecimal minSumm = prod.getMinSum();
                final BigDecimal maxSumm = prod.getMaxSum();
                if (newSumm.compareTo(minSumm) < 0 ||
                        newSumm.compareTo(maxSumm) > 0) {
                    throw new Validator.InvalidValueException(
                            MessageFormat.format(
                                    "Недопустимый размер кредита. " +
                                            "По условиям кредитного продукта, размер кредита должен быть в пределах " +
                                            "от {0, number, currency} до {1, number, currency}",
                                    minSumm, maxSumm));
                }
            }
        });
    }

    /**
     * Обновляем(Пересчитываем) стоимость кредита
     */
    private void refreshCreditCosts() {

        final ProdCredit credit = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final Number period = (Number) periodField.getValue();
        boolean canCalculate = credit != null && price != null && downPayment != null && period != null && period.intValue() != 0;
        final BigDecimal percent = getInterestRate(credit, period, downPayment);
        canCalculate = canCalculate && percent != null;
        if (canCalculate) {
            final LoanInfo loanInfo = lookup(LoanCalculator.class).calc(credit, price, downPayment, period.intValue());
            monthlyPayLabel.setValue(MessageFormat.format("{0, number, currency}", loanInfo.getMonthlyPay()));
            overpaymentLabel.setValue(MessageFormat.format("{0, number, currency}", loanInfo.getOverpayment()));
            yearlyRiseLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", loanInfo.getYearlyRise()));
            monthlyRiseLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", loanInfo.getMonthlyRise()));
        }
        // Гасим поля, если нечего в них показывать
        monthlyPayLabel.setVisible(canCalculate);
        overpaymentLabel.setVisible(canCalculate);
        yearlyRiseLabel.setVisible(canCalculate);
        monthlyRiseLabel.setVisible(canCalculate);
    }

    /**
     * Обновляем характеристики продукта.
     */
    public void refreshProductFields() {
        final ProdCredit credit = productField.getValue();
        final boolean canShowDetails = credit != null;
        if (canShowDetails) {
            final BeanItem<ProdCredit> beanItem = new BeanItem<>(Optional.ofNullable(credit).orElse(new ProdCredit()));
            beanItem.addNestedProperty("vendor.name");

            vendorLabel.setPropertyDataSource(beanItem.getItemProperty("vendor.name"));
            programTypeLabel.setPropertyDataSource(beanItem.getItemProperty("programType"));
            sumLabel.setValue(
                    MessageFormat.format("от {0, number, currency} до {1, number, currency}",
                            credit.getMinSum(), credit.getMaxSum()));
            downpaymentLabel.setValue(
                    MessageFormat.format("от {0, number, #,##.##%} до {1, number, percent}",
                            credit.getMinDownpayment(), credit.getMaxDownpayment()));
            periodLabel.setValue(
                    MessageFormat.format("от {0} до {1} мес. с шагом {2} мес.",
                            credit.getMinPeriod(), credit.getMaxPeriod(), credit.getStep()));
            percentLabel.setValue(getInterestRateText());
            documentsLabel.setValue(getDocumentsList());
        }
        vendorLabel.setVisible(canShowDetails);
        programTypeLabel.setVisible(canShowDetails);
        sumLabel.setVisible(canShowDetails);
        downpaymentLabel.setVisible(canShowDetails);
        periodLabel.setVisible(canShowDetails);
        percentLabel.setVisible(canShowDetails);
        documentsLabel.setVisible(canShowDetails);
    }

    private String getDocumentsList() {
        final ProdCredit credit = productField.getValue();
        final ArrayList<ProdCreditDoc> prodCreditDocs = newArrayList(credit.getDocList());
        return Joiner.on(", ").join(
                prodCreditDocs.stream()
//                            .sorted((a, b) -> Boolean.valueOf(b.isRequired()).compareTo(a.isRequired()))
                        .map(d -> MessageFormat.format("<span class=\"{1}\" title=\"{2}\">{0}</span>",
                                d.getName(),
                                d.isRequired() ? "ea-credit-doc-req" : "ea-credit-doc-add",
                                d.isRequired() ? "Обязательный документ" : "Дополнительный документ"))
                        .toArray());
    }

    private String getInterestRateText() {
        final BigDecimal interest = getInterestRate();
        return interest == null ? "Рассчитывается..." : MessageFormat.format("{0, number, percent}", interest);
    }

    private BigDecimal getInterestRate() {
        final ProdCredit credit = productField.getValue();
        final Number period = (Number) periodField.getValue();
        final BigDecimal downpaymentSum = downpaymentField.getValue();
        return getInterestRate(credit, period, downpaymentSum);
    }

    private BigDecimal getInterestRate(final ProdCredit credit, final Number period, final BigDecimal downpaymentSum) {
        final boolean canCalculate = credit != null && period != null && downpaymentSum != null;
        if (canCalculate)
            return lookup(LoanCalculator.class)
                    .calcInterest(credit, downpaymentSum.divide(priceSupplier.get(), MathContext.DECIMAL128), period.intValue());
        else
            return null;
    }
}

