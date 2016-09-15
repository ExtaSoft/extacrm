package ru.extas.web.sale;

import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.apache.commons.lang3.ArrayUtils;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.model.product.*;
import ru.extas.model.sale.Sale;
import ru.extas.server.product.ProductRepository;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

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
public class ProductInstancesField extends ExtaCustomField<List> {

    static final String PROD_CREDIT_CAPTION = "Кредит";
    static final String PROD_INSTALL_CAPTION = "Рассрочка";
    static final String PROD_INSUR_CAPTION = "Страховка";
    static final String PROD_HIRE_CAPTION = "Аренда с выкупом";

    private final SupplierSer<BigDecimal> priceSupplier;
    private final SupplierSer<String> brandSupplier;
    private final SupplierSer<SalePoint> salePointSupplier;
    private final Sale sale;
    private final Lead lead;
    private ExtaBeanContainer<ProductInstance> container;
    private Grid grid;
    private MenuBar.MenuItem addProductBtn;
    //    private ItemGrid productsContainer;

    /**
     */
    public ProductInstancesField(final Sale sale, final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier, final SupplierSer<SalePoint> salePointSupplier) {
        this("Продукты в продаже", null, sale, priceSupplier, brandSupplier, salePointSupplier);
    }

    /**
     *
     * @param caption           a {@link String} object.
     * @param sale
     * @param brandSupplier
     * @param salePointSupplier
     */
    public ProductInstancesField(final String caption, final Lead lead, final Sale sale, final SupplierSer<BigDecimal> priceSupplier, final SupplierSer<String> brandSupplier, final SupplierSer<SalePoint> salePointSupplier) {
        super(caption, "");
        this.priceSupplier = priceSupplier;
        this.sale = sale;
        this.lead = lead;
        this.brandSupplier = brandSupplier;
        this.salePointSupplier = salePointSupplier;
        setWidth(100, Unit.PERCENTAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final List<ProductInstance> list = getValue() != null ? (List<ProductInstance>) getValue() : new ArrayList<>();
        container = new ExtaBeanContainer<>(ProductInstance.class, list);
        final String productNamePath = MessageFormat.format("{0}.{1}", ProductInstance_.product.getName(), Product_.name.getName());
        container.addNestedContainerProperty(productNamePath);

        final VerticalLayout root = new VerticalLayout();
        root.setMargin(new MarginInfo(false, false, true, false));

        final MenuBar productMenu = new MenuBar();
        productMenu.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
        productMenu.addStyleName(ExtaTheme.MENUBAR_SMALL);

        // Продукты доступные для добавления в данную продажу
        addProductBtn = productMenu.addItem("Добавить продукт", FontAwesome.PLUS, null);
        fillProductItems();

        final MenuBar.MenuItem edMenuItem = productMenu.addItem("Редактировать", FontAwesome.EDIT, null);
        edMenuItem.setCommand(e -> {
            editProduct((ProductInstance) grid.getSelectedRow());
//            edMenuItem.setEnabled(false);
        });
        edMenuItem.setEnabled(false);
        final MenuBar.MenuItem delMenuItem = productMenu.addItem("Удалить", FontAwesome.TRASH_O, null);
        delMenuItem.setCommand(e -> {
            deleteProduct((ProductInstance) grid.getSelectedRow());
            delMenuItem.setEnabled(false);
        });
        delMenuItem.setEnabled(false);
        root.addComponent(productMenu);

        final String PROD_TYPE_ICON = "PROD_TYPE_ICON";
//        final String EDIT_BTN = "edit_btn";
//        final String DELETE_BTN = "delete_btn";
        final String[] visibleProps = new String[]{
                PROD_TYPE_ICON,
                productNamePath,
                ProductInstance_.state.getName()
        };
        final GeneratedPropertyContainer gridContainer = new GeneratedPropertyContainer(container);
        gridContainer.addGeneratedProperty(PROD_TYPE_ICON, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(final Item item, final Object itemId, final Object propertyId) {
                final ProductInstance productInstance = (ProductInstance) itemId;
                final Product product = productInstance.getProduct();
                if (product instanceof ProdCredit)
                    return FontAwesome.CREDIT_CARD.getHtml();
                else if (product instanceof ProdInsurance)
                    return FontAwesome.UMBRELLA.getHtml();
                else if (product instanceof ProdInstallments)
                    return FontAwesome.MONEY.getHtml();
                else if (product instanceof ProdHirePurchase)
                    return FontAwesome.AUTOMOBILE.getHtml();
                else
                    return "";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
//        gridContainer.addGeneratedProperty(DELETE_BTN, new PropertyValueGenerator<String>() {
//            @Override
//            public String getValue(Item item, Object itemId, Object propertyId) {
//                return "Удалить";
//            }
//
//            @Override
//            public Class<String> getType() {
//                return String.class;
//            }
//        });
//        gridContainer.addGeneratedProperty(EDIT_BTN, new PropertyValueGenerator<String>() {
//            @Override
//            public String getValue(Item item, Object itemId, Object propertyId) {
//                return "Редактировать";
//            }
//
//            @Override
//            public Class<String> getType() {
//                return String.class;
//            }
//        });


        gridContainer.getContainerPropertyIds().stream()
                .filter(p -> !ArrayUtils.contains(visibleProps, p))
                .forEach(p -> gridContainer.removeContainerProperty(p));
        grid = new Grid(gridContainer);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(container.size() == 0 ? 1 : container.size());
        grid.setEditorEnabled(false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(visibleProps);
        grid.getColumn(productNamePath).setHeaderCaption("Продукт");
        grid.getColumn(ProductInstance_.state.getName()).setHeaderCaption("Статус рассмотрения");
        grid.getColumn(PROD_TYPE_ICON).setHeaderCaption("");
        grid.getColumn(PROD_TYPE_ICON).setRenderer(new HtmlRenderer());
        grid.getColumn(PROD_TYPE_ICON).setWidth(40);
        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) { // Открываем форму ввода/редактирования
                final ProductInstance productInstance = (ProductInstance) e.getItemId();
                editProduct(productInstance);
            }
        });
        grid.addSelectionListener(e -> {
            edMenuItem.setEnabled(true);
            delMenuItem.setEnabled(true);
        });
//        grid.getColumn(EDIT_BTN).setRenderer(new ButtonRenderer(e -> editProduct((ProductInSale) e.getItemId())));
//        grid.getColumn(EDIT_BTN).setHeaderCaption("");
//        grid.getColumn(DELETE_BTN).setRenderer(new ButtonRenderer(e -> deleteProduct((ProductInSale) e.getItemId())));
//        grid.getColumn(DELETE_BTN).setHeaderCaption("");
        gridContainer.addItemSetChangeListener(e -> grid.setHeightByRows(container.size() == 0 ? 1 : container.size()));
        grid.setCellStyleGenerator(cellRef -> {
            if("state".equals(cellRef.getPropertyId())){
                final ProductInstance.State state = (ProductInstance.State) cellRef.getProperty().getValue();
                switch (state) {
                    case AGREED:
                        return "product-agreed-highlight";
                    case REJECTED:
                        return "product-rejected-highlight";
                }
            }
            return null;
        });
        root.addComponent(grid);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            addProductBtn.setVisible(!isRedOnly);
            grid.setReadOnly(isRedOnly);
            delMenuItem.setVisible(!isRedOnly);
        });

        addValueChangeListener(e -> {
            container.removeAllItems();
            final Collection<ProductInstance> value = (Collection<ProductInstance>) e.getProperty().getValue();
            if (value != null)
                container.addAll(value);
        });

        return root;
    }

    private void fillProductItems() {
        if (addProductBtn != null) {
            addProductBtn.removeChildren();

            final SalePoint salePoint = Optional.ofNullable(salePointSupplier).map(s -> s.get()).orElse(null);

            final Map<Product.Type, List<Product>> availableProducts = lookup(ProductRepository.class).findAvailableProducts(salePoint);
            final List<Product> credProducts = availableProducts.get(Product.Type.CREDIT);
            if (!credProducts.isEmpty()) {
                final MenuBar.MenuItem creditMn = addProductBtn.addItem(PROD_CREDIT_CAPTION, FontAwesome.CREDIT_CARD, null);
                // TODO: Реализовать вызов формы калькулятора
    //        creditMn.addItem("Подобрать (Кредииный калькулятор)", e -> {
    //            new LoanCalculatorForm().showModal();
    //        });
    //        creditMn.addSeparator();
                for (final Product prod : credProducts)
                    creditMn.addItem(prod.getName(), e -> addProduct(prod));
            }

            final List<Product> installProducts = availableProducts.get(Product.Type.PAYMENT_BY_INSTALLMENTS);
            if (!installProducts.isEmpty()) {
                final MenuBar.MenuItem instMn = addProductBtn.addItem(PROD_INSTALL_CAPTION, FontAwesome.MONEY, null);
                for (final Product prod : installProducts)
                    instMn.addItem(prod.getName(), e -> addProduct(prod));
            }

            final List<Product> insProducts = availableProducts.get(Product.Type.INSURANCE);
            if (!insProducts.isEmpty()) {
                final MenuBar.MenuItem insurMn = addProductBtn.addItem(PROD_INSUR_CAPTION, FontAwesome.UMBRELLA, null);
                for (final Product prod : insProducts)
                    insurMn.addItem(prod.getName(), e -> addProduct(prod));
            }

            final List<Product> hireProducts = availableProducts.get(Product.Type.HIRE_PURCHASE);
            if (!hireProducts.isEmpty()) {
                final MenuBar.MenuItem hireMn = addProductBtn.addItem(PROD_HIRE_CAPTION, FontAwesome.AUTOMOBILE, null);
                for (final Product prod : hireProducts)
                    hireMn.addItem(prod.getName(), e -> addProduct(prod));
            }
        }
    }

    private void deleteProduct(final ProductInstance itemId) {
        container.removeItem(itemId);
        setValue(newArrayList(container.getItemIds()));
    }

    private void editProduct(final ProductInstance productInstance) {
        final ExtaEditForm form = createEditForm(productInstance);
        form.setReadOnly(isReadOnly());
        form.addCloseFormListener(ev -> {
            if (form.isSaved()) {
                final ArrayList<ProductInstance> newFieldValue = newArrayList(container.getItemIds());
                container.removeAllItems();
                container.addAll(newFieldValue);
                setValue(newFieldValue);
                fireValueChange(false);
            }
        });
        FormUtils.showModalWin(form);
    }

    private ExtaEditForm createEditForm(final ProductInstance productInstance) {
        final ExtaEditForm form;
        final Product product = productInstance.getProduct();
        if (product instanceof ProdCredit) {
            form = new LoanInSaleEditForm("Кредитный продукт", productInstance, priceSupplier, brandSupplier);
        } else if (product instanceof ProdInsurance) {
            form = new InsuranceInSaleEditForm("Страховой продукт", productInstance, priceSupplier, brandSupplier);
        } else if (product instanceof ProdInstallments) {
            form = new InstallmentInSaleEditForm(PROD_INSTALL_CAPTION, productInstance, priceSupplier, brandSupplier);
        } else if (product instanceof ProdHirePurchase) {
            form = new HirePurchaseInSaleEditForm(PROD_HIRE_CAPTION, productInstance, priceSupplier, brandSupplier);
        } else
            form = null;
        return form;
    }

    private void addProduct(final Product product) {
        if (priceSupplier.get() != null && brandSupplier.get() != null) {
            final ProductInstance productInstance = new ProductInstance(lead, sale);
            productInstance.setProduct(product);
            final ExtaEditForm form = createEditForm(productInstance);
            form.setModified(true);
            form.addCloseFormListener(ev -> {
                if (form.isSaved()) {
                    container.addBean(productInstance);
                    setValue(newArrayList(container.getItemIds()));
                }
            });
            FormUtils.showModalWin(form);
        } else {
            NotificationUtil.showWarning("Невозможно добавить продукт",
                    "Для добавления продукта необходимо заполнить поля <b>'Цена техники'</b> и <b>'Макра техники'</b>");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    public void refreshSalePoint() {
        fillProductItems();
    }
}
