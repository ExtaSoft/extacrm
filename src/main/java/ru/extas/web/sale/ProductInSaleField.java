package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.sale.*;
import ru.extas.server.financial.LoanCalculator;
import ru.extas.server.financial.LoanInfo;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.server.product.ProdCreditRepository;
import ru.extas.server.product.ProdInstallmentsRepository;
import ru.extas.server.product.ProdInsuranceRepository;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.*;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdCreditField;
import ru.extas.web.product.ProdInSaleStateSelect;
import ru.extas.web.product.ProdInstallmentsField;
import ru.extas.web.product.ProdInsuranceField;

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
    private final SupplierSer<String> brandSupplier;
    private final Sale sale;
    private ExtaBeanContainer<ProductInSale> container;
    private ItemGrid productsContainer;

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     */
    public ProductInSaleField(final Sale sale, final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        this("Продукты в продаже", sale, priceSupplier, brandSupplier);
    }

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param caption       a {@link String} object.
     * @param sale
     * @param brandSupplier
     */
    public ProductInSaleField(final String caption, final Sale sale, final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier) {
        this.priceSupplier = priceSupplier;
        this.sale = sale;
        this.brandSupplier = brandSupplier;
        setWidth(100, Unit.PERCENTAGE);
        setCaption(caption);
        addValidator(value -> {
            if (productsContainer != null) {
                for (final Component component : newArrayList(productsContainer)) {
                    ((ProductItemComponent) component).validate();
                }
            }
        });

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
            final BeanItem<ProductInSale> item = container.getItem(pItemId);
            final Product product = item.getBean().getProduct();
            if (product instanceof ProdCredit)
                return new CreditItemComponent(pItemId);
            else if (product instanceof ProdInsurance)
                return new InsuranceItemComponent(pItemId);
            else if (product instanceof ProdInstallments)
                return new InstallmentItemComponent(pItemId);
            else
                return new Label("Неизвесный тип продукта!!!");
        });
        root.addComponent(productsContainer);

        final MenuBar productMenu = new MenuBar();
        productMenu.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
        productMenu.addStyleName(ExtaTheme.MENUBAR_SMALL);
        final MenuBar.MenuItem addBtn = productMenu.addItem("Добавить продукт", FontAwesome.PLUS, null);

        final MenuBar.MenuItem creditMn = addBtn.addItem("Кредит", FontAwesome.CREDIT_CARD, null);
        // TODO: Реализовать вызов формы калькулятора
//        creditMn.addItem("Подобрать (Кредииный калькулятор)", e -> {
//            new LoanCalculatorForm().showModal();
//        });
//        creditMn.addSeparator();
        for (final ProdCredit prod : lookup(ProdCreditRepository.class).findByActiveOrderByNameAsc(true))
            creditMn.addItem(prod.getName(), e -> addProduct(prod));

        final MenuBar.MenuItem instMn = addBtn.addItem("Рассрочка", FontAwesome.MONEY, null);
        for (final ProdInstallments prod : lookup(ProdInstallmentsRepository.class).findByActiveOrderByNameAsc(true))
            instMn.addItem(prod.getName(), e -> addProduct(prod));

        final MenuBar.MenuItem insurMn = addBtn.addItem("Страховка", FontAwesome.UMBRELLA, null);
        for (final ProdInsurance prod : lookup(ProdInsuranceRepository.class).findByActiveOrderByNameAsc(true))
            insurMn.addItem(prod.getName(), e -> addProduct(prod));

        root.addComponent(productMenu);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            addBtn.setVisible(!isRedOnly);
            productsContainer.setReadOnly(isRedOnly);
        });
        return root;
    }

    private void addProduct(final Product product) {
        final ProductInSale productInSale = new ProductInSale(sale);
        productInSale.setProduct(product);
        container.addBean(productInSale);
        setValue(container.getItemIds());
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
    public void commit() throws SourceException, Validator.InvalidValueException {
        if (productsContainer != null) {
            for (final Component component : newArrayList(productsContainer)) {
                ((ProductItemComponent) component).commit();
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

    private abstract class ProductItemComponent extends CssLayout {
        protected final Object itemId;
        protected final String panelCaption;
        protected final BeanItem<ProductInSale> productInSaleItem;
        protected BeanFieldGroup<ProductInSale> fieldGroup;

        public ProductItemComponent(final Object itemId, final String panelCaption, final FontIcon icon) {
            this.itemId = itemId;
            this.panelCaption = panelCaption;
            productInSaleItem = container.getItem(itemId);

            addStyleName(ExtaTheme.LAYOUT_CARD);
            setWidth(100, Unit.PERCENTAGE);

            final HorizontalLayout panelCaptionLayout = new HorizontalLayout();
            panelCaptionLayout.addStyleName(ExtaTheme.PANEL_CAPTION);
            panelCaptionLayout.setWidth(100, Unit.PERCENTAGE);
            final Label caption = new Label(icon.getHtml() + " " + this.panelCaption, ContentMode.HTML);
            panelCaptionLayout.addComponent(caption);
            panelCaptionLayout.setExpandRatio(caption, 1);

            final MenuBar productMenu = createMenuBar();
            panelCaptionLayout.addComponent(productMenu);

            addComponent(panelCaptionLayout);
            addComponent(createProductForm());
        }

        protected MenuBar createMenuBar() {
            final MenuBar productMenu = new MenuBar();
            productMenu.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            productMenu.addStyleName(ExtaTheme.MENUBAR_SMALL);
            final MenuBar.MenuItem delMenuItem = productMenu.addItem("", FontAwesome.TRASH_O, c -> {
                container.removeItem(itemId);
                setValue(container.getItemIds());
            });
            delMenuItem.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            delMenuItem.setDescription("Удалить продукт");
            return productMenu;
        }

        protected abstract AbstractComponent createProductForm();

        public void commit() throws SourceException, Validator.InvalidValueException {
            try {
                fieldGroup.commit();
            } catch (final FieldGroup.CommitException e) {
                throw new Validator.InvalidValueException(e.getMessage());
            }
        }

        public boolean isModified() {
            return fieldGroup.isModified();
        }

        public void validate() throws Validator.InvalidValueException {
            fieldGroup.getFields().forEach(Field::validate);
        }

        public void updateSubValue(final Property.ValueChangeEvent event) {
            fireValueChange(false);
        }
    }

    private class InsuranceItemComponent extends ProductItemComponent {

        // Компоненты редактирования
        @PropertyId("period")
        private ComboBox periodField;
        @PropertyId("product")
        private ProdInsuranceField productField;
        @PropertyId("responsible")
        private EmployeeField responsibleField;

        private Label vendorLabel;
        private Label tariffLabel;
        private Label premiumLabel;

        public InsuranceItemComponent(final Object itemId) {
            super(itemId, "Страховка", FontAwesome.UMBRELLA);
        }

        @Override
        protected AbstractComponent createProductForm() {
            final ExtaFormLayout form = new ExtaFormLayout();

            productField = new ProdInsuranceField("Продукт", "Введите название продукта");
            productField.setRequired(true);
            productField.addValueChangeListener(e -> refreshProductFields());

            form.addComponent(new FormGroupHeader("Характеристики продукта"));
            vendorLabel = new Label();
            vendorLabel.setCaption("Страховщик");
            form.addComponent(vendorLabel);

            tariffLabel = new Label();
            tariffLabel.setCaption("Тариф");
            tariffLabel.setConverter(lookup(StringToPercentConverter.class));
            form.addComponent(tariffLabel);

            form.addComponent(new FormGroupHeader("Параметры страховки"));
            periodField = new ComboBox("Период страхования");
            periodField.setDescription("Введите период страхования");
            periodField.setImmediate(true);
            periodField.setNullSelectionAllowed(false);
            periodField.setRequired(true);
            periodField.setWidth(6, Unit.EM);
            // Наполняем возможными сроками страховки
            fillPeriodFieldItems();
            form.addComponent(periodField);

            // Ответственный за оформление страховки
            responsibleField = new EmployeeField("Ответственный", "Укажите ответственного за оформление страховки");
            responsibleField.setCompanySupplier(() -> {
                final ProdInsurance product = productField.getValue();
                if (product != null) {
                    return product.getVendor();
                } else
                    return null;
            });
            form.addComponent(responsibleField);

            form.addComponent(new FormGroupHeader("Стоимость страховки"));
            // Размер страховой премии
            premiumLabel = new Label();
            premiumLabel.setCaption("Страховая премия");
            form.addComponent(premiumLabel);

            // Now create a binder
            fieldGroup = new BeanFieldGroup<>(ProductInSale.class);
            fieldGroup.setItemDataSource(productInSaleItem);
            fieldGroup.setBuffered(true);
            fieldGroup.bindMemberFields(this);

            // Обновление рассчетных полей
            refreshProductFields();
            refreshInsCosts();
            // Инициализация взаимосвязей
            initRelations();

            final VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.addComponent(new ExtaFormLayout(productField));
            final Disclosure disclosure = new Disclosure("Подробнее...", "Свернуть...", form);
            if(productInSaleItem.getBean().isNew())
                disclosure.open();
            mainLayout.addComponent(disclosure);

            return mainLayout;
        }

        private void initRelations() {
            productField.addValueChangeListener(this::productChangeListener);
            periodField.addValueChangeListener(this::periodChangeListener);
        }

        /**
         * Меняется продукт.
         */
        private void productChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем характеристики продукта.
            refreshProductFields();
            // Обновляем(Пересчитываем) стоимость страховки
            refreshInsCosts();
        }

        /**
         * Меняется срок страхования
         */
        private void periodChangeListener(final Property.ValueChangeEvent valueChangeEvent) {
            // Обновляем характеристики
            refreshProductFields();
            // Обновляем стоимость
            refreshInsCosts();
        }

        /**
         * Обновляем(Пересчитываем) стоимость страховки
         */
        private void refreshInsCosts() {

            final ProdInsurance insurance = productField.getValue();
            final BigDecimal price = priceSupplier.get();
            final String brand = brandSupplier.get();
            final Number numPeriod = (Number) periodField.getValue();
            final Insurance.PeriodOfCover period =
                    Optional.ofNullable(numPeriod).map(
                            n -> n.equals(6) ? Insurance.PeriodOfCover.HALF_A_YEAR : Insurance.PeriodOfCover.YEAR).orElse(null);
            boolean canCalculate = insurance != null && price != null && brand != null && period != null;
            if (canCalculate) {
                final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                final BigDecimal tarif = calc.findTarif(brand, period, false);
                if(tarif != null) {
                    final BigDecimal premium = calc.calcPropInsPremium(brand, price, period, false);
                    premiumLabel.setValue(MessageFormat.format("{0, number, currency}", premium));
                } else
                    canCalculate = false;
            }
            // Гасим поля, если нечего в них показывать
            premiumLabel.setVisible(canCalculate);
        }

        /**
         * Обновляем характеристики продукта.
         */
        public void refreshProductFields() {
            final ProdInsurance insurance = productField.getValue();
            final boolean canShowDetails = insurance != null;
            if (canShowDetails) {
                final BeanItem<ProdInsurance> beanItem = new BeanItem<>(Optional.ofNullable(insurance).orElse(new ProdInsurance()));
                beanItem.addNestedProperty("vendor.name");

                vendorLabel.setPropertyDataSource(beanItem.getItemProperty("vendor.name"));

                final String brand = brandSupplier.get();
                final Number numPeriod = (Number) periodField.getValue();
                final Insurance.PeriodOfCover period =
                        Optional.ofNullable(numPeriod).map(
                                n -> n.equals(6) ? Insurance.PeriodOfCover.HALF_A_YEAR : Insurance.PeriodOfCover.YEAR).orElse(null);
                final boolean canFindTarif = brand != null && period != null;
                if (canFindTarif) {
                    final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                    final BigDecimal premium = calc.findTarif(brand, period, false);
                    if(premium != null) {
                        tariffLabel.setPropertyDataSource(new ObjectProperty<>(premium));
                        tariffLabel.setVisible(true);
                    }
                } else
                    tariffLabel.setVisible(false);
                vendorLabel.setVisible(true);
            } else {
                vendorLabel.setVisible(false);
                tariffLabel.setVisible(false);
            }
        }

        private void fillPeriodFieldItems() {
            // Наполняем возможными сроками страхования
            periodField.removeAllItems();
            int period = 6;
            periodField.addItem(period);
            periodField.setItemCaption(period, "6 мес.");
            period = 12;
            periodField.addItem(period);
            periodField.setItemCaption(period, "12 мес.");
        }
    }

    private class InstallmentItemComponent extends ProductItemComponent {

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

        public InstallmentItemComponent(final Object itemId) {
            super(itemId, "Рассрочка", FontAwesome.MONEY);
        }

        @Override
        protected AbstractComponent createProductForm() {
            final ExtaFormLayout form = new ExtaFormLayout();

            productField = new ProdInstallmentsField("Продукт", "Введите название продукта");
            productField.setRequired(true);
            productField.addValueChangeListener(e -> refreshProductFields());

            stateField = new ProdInSaleStateSelect("Статус рассмотрения", "Укажите статус рассмотрения заявки на продукт");

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

            // Now create a binder
            fieldGroup = new BeanFieldGroup<>(ProductInSale.class);
            fieldGroup.setItemDataSource(productInSaleItem);
            fieldGroup.setBuffered(true);
            fieldGroup.bindMemberFields(this);

            // Инициализация валидаторов
            initValidators();
            // Обновление рассчетных полей
            refreshProductFields();
            refreshInstCosts();
            // Инициализация взаимосвязей
            initRelations();

            final VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.addComponent(new ExtaFormLayout(productField, stateField));
            final Disclosure disclosure = new Disclosure("Подробнее...", "Свернуть...", form);
            if (productInSaleItem.getBean().isNew())
                disclosure.open();
            mainLayout.addComponent(disclosure);

            return mainLayout;
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

    private class CreditItemComponent extends ProductItemComponent {

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
        private ProdInSaleStateSelect stateField;

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

        public CreditItemComponent(final Object itemId) {
            super(itemId, "Кредит", FontAwesome.CREDIT_CARD);
        }

        @Override
        protected MenuBar createMenuBar() {
            final MenuBar menuBar = super.createMenuBar();

            // TODO: Реализовать вызов формы калькулятора
//            final MenuBar.MenuItem editMenuItem = menuBar.addItemBefore("", FontAwesome.COMPASS, null, menuBar.getItems().get(0));
//            editMenuItem.setStyleName(ExtaTheme.BUTTON_ICON_ONLY);
//            editMenuItem.setDescription("Открыть форму подбора продукта (калькулятор продукта)");

            return menuBar;
        }

        @Override
        protected AbstractComponent createProductForm() {

            final ExtaFormLayout form = new ExtaFormLayout();

            productField = new ProdCreditField("Продукт", "Введите название продукта");
            productField.setRequired(true);
            productField.addValueChangeListener(e -> refreshProductFields());

            stateField = new ProdInSaleStateSelect("Статус рассмотрения", "Укажите статус рассмотрения заявки на продукт");

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

            final VerticalLayout mainLayout = new VerticalLayout();
            mainLayout.addComponent(new ExtaFormLayout(productField, stateField));
            final Disclosure disclosure = new Disclosure("Подробнее...", "Свернуть...", form);
            if (productInSaleItem.getBean().isNew())
                disclosure.open();
            mainLayout.addComponent(disclosure);

            return mainLayout;
        }

        private void initRelations() {
            productField.addValueChangeListener(this::productChangeListener);
            productField.addValueChangeListener(super::updateSubValue);
            downpaymentField.setBase(priceSupplier.get());
            downpaymentField.addValueChangeListener(this::downPaymentChangeListener);
            downpaymentField.addValueChangeListener(super::updateSubValue);
            periodField.addValueChangeListener(this::periodChangeListener);
            periodField.addValueChangeListener(super::updateSubValue);
            summField.addValueChangeListener(this::creditSummChangeListener);
            summField.addValueChangeListener(super::updateSubValue);
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
            final BigDecimal downPayment = downpaymentField.getValue();
            final Number period = (Number) periodField.getValue();
            final boolean canCalculate = credit != null && price != null && downPayment != null && period != null && period.intValue() != 0;
            final BigDecimal percent = getInterestRate(credit, period, downPayment);
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
}
