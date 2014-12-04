package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import ru.extas.model.sale.*;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.product.ProductSelect;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

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

    private final Sale sale;
    private FilterTable productTable;
    private ExtaBeanContainer<ProductInSale> container;
    private ItemGrid productsContainer;

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param sale a {@link ru.extas.model.sale.Sale} object.
     */
    public ProductInSaleField(final Sale sale) {
        this("Продукты в продаже", sale);
    }

    /**
     * <p>Constructor for ProductInSaleGrid.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param sale    a {@link ru.extas.model.sale.Sale} object.
     */
    public ProductInSaleField(final String caption, final Sale sale) {
        this.sale = sale;
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
        private ProductSelect productField;
        private Label vendorField;
        private Label programTypeField;
        private Label sumField;
        private Label downpaymentField;
        private Label periodField;
        private Label percentField;
        private Label documentsField;

        public CreditItemComponent(AbstractItemLayout pSource, Object itemId) {
            this.itemId = itemId;
            final Product product = container.getItem(itemId).getBean().getProduct();
            addStyleName(ExtaTheme.LAYOUT_CARD);
            setWidth(100, Unit.PERCENTAGE);

            HorizontalLayout panelCaption = new HorizontalLayout();
            panelCaption.addStyleName(ExtaTheme.PANEL_CAPTION);
            panelCaption.setWidth(100, Unit.PERCENTAGE);
            Label label = new Label("Кредит: " + product.getName());
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
            addComponent(createProductForm(product));
        }

        private Component createProductForm(Product product) {

            ProdCredit credit = (ProdCredit) product;
            ExtaFormLayout form = new ExtaFormLayout();

            form.addComponent(new FormGroupHeader("Характеристики продукта"));
            productField = new ProductSelect("Продукт", "Введите название продукта", null);
            productField.addValueChangeListener(e -> refreshProductFields((ProdCredit) productField.getConvertedValue()));
            form.addComponent(productField);

            vendorField = new Label();
            vendorField.setCaption("Банк");
            programTypeField = new Label();
            programTypeField.setCaption("Тип программы");
            sumField = new Label();
            sumField.setCaption("Сумма кредита");
            downpaymentField = new Label();
            downpaymentField.setCaption("Первоначальный взнос");
            periodField = new Label();
            periodField.setCaption("Период кредитования");
            documentsField = new Label();
            documentsField.setCaption("Пакет документов");
            documentsField.setContentMode(ContentMode.HTML);
            percentField = new Label();
            percentField.setCaption("Процент");

            productField.setValue(credit);
            form.addComponents(vendorField, programTypeField, sumField, downpaymentField, periodField, percentField, documentsField);

            refreshProductFields(credit);
            return form;
        }

        public void refreshProductFields(final ProdCredit credit) {

            final BeanItem<ProdCredit> beanItem = new BeanItem<>(Optional.ofNullable(credit).orElse(new ProdCredit()));
            beanItem.addNestedProperty("vendor.name");

            vendorField.setPropertyDataSource(beanItem.getItemProperty("vendor.name"));
            programTypeField.setPropertyDataSource(beanItem.getItemProperty("programType"));
            sumField.setValue(
                    MessageFormat.format("от {0, number, currency} до {1, number, currency}",
                            credit.getMinSum(), credit.getMaxSum()));
            downpaymentField.setValue(
                    MessageFormat.format("от {0, number, percent} до {1, number, percent}",
                            credit.getMinDownpayment(), credit.getMaxDownpayment()));
            periodField.setValue(
                    MessageFormat.format("от {0} до {1} мес.",
                            credit.getMinPeriod(), credit.getMaxPeriod()));
            percentField.setValue(getInterestRate(credit));
            documentsField.setValue(getDocumentsList(credit));
        }

        private String getDocumentsList(ProdCredit credit) {
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

        private String getInterestRate(ProdCredit credit) {
            final List<ProdCreditPercent> percents = newArrayList(credit.getPercents());
            if (percents.size() < 2)
                return percents.stream()
                        .findFirst()
                        .map(p -> MessageFormat.format("{0, number, percent}", p.getPercent()))
                        .orElse("Нет");
            else
                return "Рассчитывается...";
        }
    }
}
