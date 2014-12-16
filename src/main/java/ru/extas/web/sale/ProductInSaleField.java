package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import ru.extas.model.sale.*;
import ru.extas.server.financial.LoanCalculator;
import ru.extas.server.financial.LoanInfo;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PercentOfField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdCreditField;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Встроенная форма редактирования продуктов в продаже
 *
 * @author Valery Orlov
 *         Date: 21.01.14
 *         Time: 15:28
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductInSaleField extends CustomField<List> {

    private final SupplierSer<BigDecimal> priceSupplier;
    private ExtaBeanContainer<ProductInSale> container;
    private ItemGrid productsContainer;

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     */
    public ProductInSaleField(final SupplierSer<BigDecimal> priceSupplier) {
        this("Продукты в продаже", priceSupplier);
    }

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public ProductInSaleField(final String caption, final SupplierSer<BigDecimal> priceSupplier) {
        this.priceSupplier = priceSupplier;
        setWidth(100, Unit.PERCENTAGE);
        setCaption(caption);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final List<ProductInSale> list = getValue() != null ? (List<ProductInSale>) getValue() : new ArrayList<>();
        container = new ExtaBeanContainer<>(ProductInSale.class);
        if (list != null) {
            container.addAll(list);
        }
        final VerticalLayout root = new VerticalLayout();

        productsContainer = new ItemGrid();
        productsContainer.setColumns(1);
        productsContainer.setSelectable(false);
        productsContainer.setContainerDataSource(container);
        productsContainer.setItemGenerator((pSource, pItemId) -> {
            final Product product = container.getItem(pItemId).getBean().getProduct();
            if (product instanceof ProdCredit)
                return new CreditItemComponent(pSource, pItemId);
            else if (product instanceof ProdInsurance)
                return new Label("Страховые продукты не поддерживаются!!!");
            else if (product instanceof ProdInstallments)
                return new Label("Рассрочка не поддерживается!!!");
            else
                return new Label("Неизвесный тип продукта!!!");
        });
        root.addComponent(productsContainer);

        final Button addBtn = new Button("Добавить продукт", FontAwesome.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addClickListener(e -> {
        });
        root.addComponent(addBtn);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            addBtn.setVisible(!isRedOnly);
            productsContainer.setReadOnly(isRedOnly);
        });
        return root;
    }

    @Override
    public boolean isModified() {

        if (super.isModified())
            return true;

        if (productsContainer != null) {
            final Iterator<Component> iterator = productsContainer.iterator();
            while (iterator.hasNext()) {
                if (((ProductItemComponent) iterator.next()).isModified())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        super.validate();
        if (productsContainer != null) {
            final Iterator<Component> iterator = productsContainer.iterator();
            while (iterator.hasNext()) {
                ((ProductItemComponent) iterator.next()).validate();
            }
        }
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        if (productsContainer != null) {
            final Iterator<Component> iterator = productsContainer.iterator();
            while (iterator.hasNext()) {
                ((ProductItemComponent) iterator.next()).commit();
            }
        }
        super.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class CreditItemComponent extends CssLayout implements ProductItemComponent {

        private final Object itemId;

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

        private final BeanItem<ProductInSale> productInSaleItem;
        private BeanFieldGroup<ProductInSale> fieldGroup;

        public CreditItemComponent(AbstractItemLayout pSource, Object itemId) {
            this.itemId = itemId;
            productInSaleItem = container.getItem(itemId);
            final Product product = productInSaleItem.getBean().getProduct();

            addStyleName(ExtaTheme.LAYOUT_CARD);
            setWidth(100, Unit.PERCENTAGE);

            HorizontalLayout panelCaption = new HorizontalLayout();
            panelCaption.addStyleName(ExtaTheme.PANEL_CAPTION);
            panelCaption.setWidth(100, Unit.PERCENTAGE);
            Label label = new Label("Кредит");
            panelCaption.addComponent(label);
            panelCaption.setExpandRatio(label, 1);

            MenuBar productMenu = new MenuBar();
            productMenu.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            productMenu.addStyleName(ExtaTheme.MENUBAR_SMALL);
            MenuBar.MenuItem editMenuItem = productMenu.addItem("", FontAwesome.PENCIL, null);
            editMenuItem.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            editMenuItem.setDescription("Редактировать продукт");
            MenuBar.MenuItem delMenuItem = productMenu.addItem("", FontAwesome.TRASH_O, null);
            delMenuItem.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            delMenuItem.setDescription("Удалить продукт");
            panelCaption.addComponent(productMenu);

            addComponent(panelCaption);
            addComponent(createProductForm());
        }

        private Component createProductForm() {

            ExtaFormLayout form = new ExtaFormLayout();

            form.addComponent(new FormGroupHeader("Характеристики продукта"));
            productField = new ProdCreditField("Продукт", "Введите название продукта");
            productField.setRequired(true);
            productField.addValueChangeListener(e -> refreshProductFields());
            form.addComponent(productField);

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
            form.addComponent(downpaymentField);

            periodField = new ComboBox("Срок кредитования");
            periodField.setDescription("Введите период кредитования (срок кредита)");
            periodField.setImmediate(true);
            periodField.setNullSelectionAllowed(false);
            periodField.setRequired(true);
            periodField.setWidth(6, Unit.EM);
            // Наполняем возможными сроками кредита
            fillPeriodFieldItems();
            form.addComponent(periodField);

            summField = new EditField("Сумма кредита", "Введите сумму кредита (Также может рассчитываться автоматически)");
            summField.setRequired(true);
            form.addComponent(summField);

            // Ответственный со стороны банка
            responsibleField = new EmployeeField("Менеджер банка", "Укажите ответственного со стороны банка");
            responsibleField.setCompanySupplier(() -> {
                ProdCredit product = productField.getValue();
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

            // Now create a binder
            fieldGroup = new BeanFieldGroup<>(ProductInSale.class);
            fieldGroup.setItemDataSource(productInSaleItem);
            fieldGroup.setBuffered(true);
            fieldGroup.bindMemberFields(this);

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
            downpaymentField.addValueChangeListener(this::downPaymentChangeListener);
            periodField.addValueChangeListener(this::periodChangeListener);
            summField.addValueChangeListener(this::creditSummChangeListener);
            // Наполняем возможными сроками кредита
            fillPeriodFieldItems();
        }

        /**
         * Меняется продукт.
         */
        private void productChangeListener(Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем характеристики продукта.
            refreshProductFields();
            // Обновляем параметы продукта
            ProdCredit credit = productField.getValue();
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
            ProdCredit credit = productField.getValue();
            if (credit != null) {
                int start = credit.getMinPeriod();
                int end = credit.getMaxPeriod();
                int step = credit.getStep();
                Object curValue = periodField.getValue();
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
        private void downPaymentChangeListener(Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем характеристики
            refreshProductFields();
            // Обновляем сумму кредита
            final ProdCredit credit = productField.getValue();
            final BigDecimal price = priceSupplier.get();
            final BigDecimal downPayment = (BigDecimal) downpaymentField.getValue();
            final boolean canCalculate = credit != null && price != null && downPayment != null;
            if (canCalculate) {
                BigDecimal creditSum = lookup(LoanCalculator.class).calcCreditSum(credit, price, downPayment);
                summField.setConvertedValue(creditSum);
            }
            // Обновляем стоимость
            refreshCreditCosts();
        }

        /**
         * Меняется срок кредитования
         */
        private void periodChangeListener(Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем характеристики
            refreshProductFields();
            // Обновляем стоимость
            refreshCreditCosts();
        }

        /**
         * Меняется сумма
         */
        private void creditSummChangeListener(Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем первоначальный взнос чтобы получить цену техники с учетом новой суммы
            final ProdCredit credit = productField.getValue();
            final BigDecimal price = priceSupplier.get();
            final BigDecimal creditSum = (BigDecimal) summField.getConvertedValue();
            if (credit != null && price != null && creditSum != null) {
                BigDecimal downPayment = lookup(LoanCalculator.class).calcDownPayment(credit, price, creditSum);
                downpaymentField.setConvertedValue(downPayment);
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
                ProdCredit prod = productField.getValue();
                if (prod != null) {
                    BigDecimal newDownpayment = (BigDecimal) value;
                    BigDecimal minDownpayment = prod.getMinDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128);
                    BigDecimal maxDownpayment = prod.getMaxDownpayment().multiply(priceSupplier.get(), MathContext.DECIMAL128);
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
                ProdCredit prod = productField.getValue();
                if (prod != null) {
                    BigDecimal newSumm = (BigDecimal) value;
                    BigDecimal minSumm = prod.getMinSum();
                    BigDecimal maxSumm = prod.getMaxSum();
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
            final BigDecimal downPayment = (BigDecimal) downpaymentField.getValue();
            final Number period = (Number) periodField.getValue();
            final boolean canCalculate = credit != null && price != null && downPayment != null && period != null;
            final BigDecimal percent = getInterestRate(credit, period, downPayment);
            if (canCalculate) {
                LoanInfo loanInfo = lookup(LoanCalculator.class).calc(credit, price, downPayment, period.intValue());
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
            ProdCredit credit = productField.getValue();
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
            ProdCredit credit = productField.getValue();
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
            BigDecimal interest = getInterestRate();
            return interest == null ? "Рассчитывается..." : MessageFormat.format("{0, number, percent}", interest);
        }

        private BigDecimal getInterestRate() {
            ProdCredit credit = productField.getValue();
            Number period = (Number) periodField.getValue();
            BigDecimal downpaymentSum = (BigDecimal) downpaymentField.getValue();
            return getInterestRate(credit, period, downpaymentSum);
        }

        private BigDecimal getInterestRate(ProdCredit credit, Number period, BigDecimal downpaymentSum) {
            final boolean canCalculate = credit != null && period != null && downpaymentSum != null;
            if (canCalculate)
                return lookup(LoanCalculator.class)
                        .calcInterest(credit, downpaymentSum.divide(priceSupplier.get(), MathContext.DECIMAL128), period.intValue());
            else
                return null;
        }

        @Override
        public void commit() throws SourceException, Validator.InvalidValueException {
            try {
                fieldGroup.commit();
            } catch (FieldGroup.CommitException e) {
                throw new Validator.InvalidValueException(e.getMessage());
            }
        }

        @Override
        public boolean isModified() {
            return fieldGroup.isModified();
        }

        @Override
        public void validate() {
            for (final Field<?> field : fieldGroup.getFields())
                field.validate();

        }
    }
}
