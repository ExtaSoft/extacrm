package ru.extas.web.commons;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import ru.extas.model.product.ProdHirePurchase;
import ru.extas.model.product.ProductInstance;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PercentOfField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdHirePurchaseField;
import ru.extas.web.product.ProdInstanceStateSelect;
import ru.extas.web.product.ProductExpendituresField;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * @author sandarkin
 * @version 2.0
 */
public abstract class HirePurchaseInSmthEditForm<T> extends ExtaEditForm<T> {

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
    private ProdHirePurchaseField productField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("state")
    private ProdInstanceStateSelect stateField;
    @PropertyId("expenditureList")
    private ProductExpendituresField expendituresField;

    private Label vendorLabel;
    private Label downpaymentLabel;
    private Label periodLabel;

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *  @param caption       a {@link String} object.
     * @param priceSupplier
     * @param brandSupplier
     */
    public HirePurchaseInSmthEditForm(final String caption, final T targetObject,
                                      final SupplierSer<BigDecimal> priceSupplier,
                                      final SupplierSer<String> brandSupplier) {
        super(caption, targetObject);
        setWinWidth(750, Unit.PIXELS);
        this.priceSupplier = priceSupplier;
        this.brandSupplier = brandSupplier;
    }

    /**
     * <p>initEntity.</p>
     *
     */
    @Override
    protected void initEntity(final T targetObject) {

    }

    /**
     * <p>saveEntity.</p>
     *
     */
    @Override
    protected T saveEntity(final T targetObject) {
        return targetObject;
    }

    /**
     * <p>createEditFields.</p>
     *
     * @return a {@link ComponentContainer} object.
     */
    @Override
    protected ComponentContainer createEditFields() {
        final ExtaFormLayout form = new ExtaFormLayout();

        form.addComponent(new FormGroupHeader("Продукт"));
        productField = new ProdHirePurchaseField("Продукт", "Введите название продукта");
        productField.setRequired(true);
        productField.addValueChangeListener(e -> refreshProductFields());
        form.addComponent(productField);

        stateField = new ProdInstanceStateSelect("Статус рассмотрения", "Укажите статус рассмотрения заявки на продукт");
        form.addComponent(stateField);

        form.addComponent(new FormGroupHeader("Сопутствующие расходы"));
        expendituresField = new ProductExpendituresField("Статьи расходов",
                "Список дополнительных расходов сопровождающих продукт", (ProductInstance) getEntity());
        form.addComponent(expendituresField);

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

        // Ответственный со стороны банка
        responsibleField = new EmployeeField("Ответственный сотрудник", "Укажите ответственного со стороны эммитента рассрочки");
        responsibleField.setCompanySupplier(() -> {
            final ProdHirePurchase product = productField.getValue();
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
        final ProdHirePurchase prod = productField.getValue();
        if (prod != null) {
            // Наполняем возможными сроками кредита
            fillPeriodFieldItems();

            // Задаем начальные значения параметров (если они не заданы)
            if (downpaymentField.getValue() == null)
                downpaymentField.setValue(
                        prod.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128));
            if (periodField.getValue() == null)
                periodField.setValue((prod.getMaxPeriod()));
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
        final ProdHirePurchase prod = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final boolean canCalculate = prod != null && price != null && downPayment != null;
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
        final ProdHirePurchase prod = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal instSum = (BigDecimal) summField.getConvertedValue();
        if (prod != null && price != null && instSum != null) {
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

        final ProdHirePurchase prod = productField.getValue();
        final BigDecimal price = priceSupplier.get();
        final BigDecimal downPayment = downpaymentField.getValue();
        final Number period = (Number) periodField.getValue();
    }

    /**
     * Добавляем проверки при вводе
     */
    private void initValidators() {
        downpaymentField.addValidator(value -> {
            final ProdHirePurchase prod = productField.getValue();
            if (prod != null) {
                final BigDecimal newDownpayment = (BigDecimal) value;
                final BigDecimal minDownpayment = prod.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128);
                final BigDecimal maxDownpayment = BigDecimal.valueOf(.99).multiply(priceSupplier.get(), MathContext.DECIMAL128);
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
        final ProdHirePurchase prod = productField.getValue();
        if (prod != null) {
            final int start = 1;
            final int end = prod.getMaxPeriod();
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
        final ProdHirePurchase installments = productField.getValue();
        final boolean canShowDetails = installments != null;
        if (canShowDetails) {
            final BeanItem<ProdHirePurchase> beanItem = new BeanItem<>(Optional.ofNullable(installments).orElse(new ProdHirePurchase()));
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
