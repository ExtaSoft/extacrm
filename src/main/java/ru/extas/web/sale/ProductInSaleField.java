package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;
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
import ru.extas.web.product.ProdCreditField;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.ArrayList;
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
    private FilterTable productTable;
    private ExtaBeanContainer<ProductInSale> container;
    private ItemGrid productsContainer;

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param sale a {@link ru.extas.model.sale.Sale} object.
     */
    public ProductInSaleField(final SupplierSer<BigDecimal> priceSupplier) {
        this("Продукты в продаже", priceSupplier);
    }

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param sale    a {@link ru.extas.model.sale.Sale} object.
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
//        final GridLayout panel = new GridLayout(1, 2);
//        panel.setSizeFull();
//
//        panel.setRowExpandRatio(1, 1);
//        panel.setMargin(true);
//
//        panel.setSpacing(true);
//        final MenuBar commandBar = new MenuBar();
//        commandBar.setAutoOpen(true);
//        commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
//        commandBar.addStyleName(ExtaTheme.MENUBAR_SMALL);
//
//        final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {
//
//            final ProductInSale productInSale = new ProductInSale(sale);
//            final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Новый продукт в продаже", productInSale);
//            editWin.addCloseFormListener(event1 -> {
//                if (editWin.isSaved()) {
//                    container.addBean(productInSale);
//                }
//            });
//            FormUtils.showModalWin(editWin);
//        });
//        addProdBtn.setDescription("Добавить продукт в продажу");
//        addProdBtn.setIcon(Fontello.DOC_NEW);
//
//        final MenuBar.MenuItem edtProdBtn = commandBar.addItem("Изменить", event -> {
//            if (productTable.getValue() != null) {
//                final BeanItem<ProductInSale> prodItem = (BeanItem<ProductInSale>) productTable.getItem(productTable.getValue());
//                final ProdInSaleEditForm editWin = new ProdInSaleEditForm("Редактирование продукта в продаже", prodItem.getBean());
//                FormUtils.showModalWin(editWin);
//            }
//        });
//        edtProdBtn.setDescription("Изменить выделенный в списке продукт");
//        edtProdBtn.setIcon(Fontello.EDIT_3);
//
//        final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
//            if (productTable.getValue() != null) {
//                productTable.removeItem(productTable.getValue());
//            }
//        });
//        delProdBtn.setDescription("Удалить продукт из продажи");
//        delProdBtn.setIcon(Fontello.TRASH);
//
//        panel.addComponent(commandBar);
//
//        addReadOnlyStatusChangeListener(e -> {
//            final boolean isReadOnly = isReadOnly();
//            commandBar.setVisible(!isReadOnly);
//            panel.setSpacing(!isReadOnly);
//        });
//
//        productTable = new FilterTable();
//        productTable.setWidth(100, Unit.PERCENTAGE);
//        productTable.setPageLength(3);
//        productTable.addStyleName(ExtaTheme.TABLE_SMALL);
//        productTable.addStyleName(ExtaTheme.TABLE_COMPACT);
//        productTable.setRequired(true);
//        productTable.setSelectable(true);
//        final Property dataSource = getPropertyDataSource();
//        final List<ProductInSale> productInSaleList = dataSource != null ? (List<ProductInSale>) dataSource.getValue() : sale.getProductInSales();
//        container = new ExtaBeanContainer<>(ProductInSale.class);
//        container.addNestedContainerProperty("product.name");
//        if (productInSaleList != null) {
//            for (final ProductInSale productInSale : productInSaleList) {
//                container.addBean(productInSale);
//            }
//        }
//        productTable.setContainerDataSource(container);
//        productTable.addItemSetChangeListener(event -> setValue(newArrayList(productTable.getItemIds())));
//        // Колонки таблицы
//        productTable.setVisibleColumns("product.name", "summ", "period");
//        productTable.setColumnHeader("product.name", "Продукт");
//        productTable.setColumnHeader("summ", "Сумма");
//        productTable.setColumnHeader("period", "Срок");
//        panel.addComponent(productTable);
//
//        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class CreditItemComponent extends CssLayout {

        private final Object itemId;

        // Компоненты редактирования
        @PropertyId("summ")
        private EditField summField;
        @PropertyId("downpayment")
        private EditField downpaymentField;
        @PropertyId("period")
        private EditField periodField;
        @PropertyId("product")
        private ProdCreditField productField;

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
            downpaymentField = new EditField("Первоначальный взнос", "Введите сумму первоначального взноса по кредиту");
            downpaymentField.setRequired(true);
            downpaymentField.addValidator(value -> {
                ProdCredit prod = productField.getValue();
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
            });
            form.addComponent(downpaymentField);

            periodField = new EditField("Срок кредитования", "Введите период кредитования (сток кредита)");
            periodField.setRequired(true);
            form.addComponent(periodField);

            summField = new EditField("Сумма кредита", "Введите сумму кредита (Также может рассчитываться автоматически)");
            summField.setRequired(true);
            form.addComponent(summField);

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
            BeanFieldGroup<ProductInSale> fieldGroup = new BeanFieldGroup<>(ProductInSale.class);
            fieldGroup.setItemDataSource(productInSaleItem);
            fieldGroup.setBuffered(true);
            fieldGroup.bindMemberFields(this);

            refreshProductFields();
            refreshCreditCosts();

            return form;
        }

        private void refreshCreditCosts() {

            LoanInfo loanInfo = lookup(LoanCalculator.class).calc(
                    (ProdCredit) productField.getValue(),
                    priceSupplier.get(),
                    (BigDecimal) downpaymentField.getConvertedValue(),
                    (int) periodField.getConvertedValue());
            monthlyPayLabel.setValue(MessageFormat.format("{0, number, currency}", loanInfo.getMonthlyPay()));
            overpaymentLabel.setValue(MessageFormat.format("{0, number, currency}", loanInfo.getOverpayment()));
            yearlyRiseLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", loanInfo.getYearlyRise()));
            monthlyRiseLabel.setValue(MessageFormat.format("{0, number, #,##.##%}", loanInfo.getMonthlyRise()));
        }

        public void refreshProductFields() {
            ProdCredit credit = productField.getValue();

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
                    MessageFormat.format("от {0} до {1} мес.",
                            credit.getMinPeriod(), credit.getMaxPeriod()));
            percentLabel.setValue(getInterestRateText());
            documentsLabel.setValue(getDocumentsList());
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
            BigDecimal downpaymentSum = (BigDecimal) downpaymentField.getConvertedValue();
            int period = (int) periodField.getConvertedValue();

            return lookup(LoanCalculator.class)
                    .calcInterest(credit, downpaymentSum.divide(priceSupplier.get(), MathContext.DECIMAL128), period);
        }
    }
}
