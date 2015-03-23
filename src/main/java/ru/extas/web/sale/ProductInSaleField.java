package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.sale.*;
import ru.extas.server.financial.LoanCalculator;
import ru.extas.server.financial.LoanInfo;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.server.product.ProdCreditRepository;
import ru.extas.server.product.ProdInstallmentsRepository;
import ru.extas.server.product.ProdInsuranceRepository;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
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
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
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
    private Grid grid;
    //    private ItemGrid productsContainer;

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
//        addValidator(value -> {
//            if (productsContainer != null) {
//                for (final Component component : newArrayList(productsContainer)) {
//                    ((ProductItemComponent) component).validate();
//                }
//            }
//        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final List<ProductInSale> list = getValue() != null ? (List<ProductInSale>) getValue() : new ArrayList<>();
        container = new ExtaBeanContainer<>(ProductInSale.class, list);
        final String productNamePath = MessageFormat.format("{0}.{1}", ProductInSale_.product.getName(), Product_.name.getName());
        container.addNestedContainerProperty(productNamePath);

        final VerticalLayout root = new VerticalLayout();
        root.setMargin(new MarginInfo(false, false, true, false));

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

        final String PROD_TYPE_ICON = "PROD_TYPE_ICON";
//        final String EDIT_BTN = "edit_btn";
        final String DELETE_BTN = "delete_btn";
        String visibleProps[] = new String[]{
                PROD_TYPE_ICON,
                productNamePath,
                ProductInSale_.state.getName(),
//                EDIT_BTN,
                DELETE_BTN
        };
        GeneratedPropertyContainer gridContainer = new GeneratedPropertyContainer(container);
        gridContainer.addGeneratedProperty(PROD_TYPE_ICON, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                ProductInSale productInSale = (ProductInSale) itemId;
                Product product = productInSale.getProduct();
                if (product instanceof ProdCredit)
                    return FontAwesome.CREDIT_CARD.getHtml();
                else if (product instanceof ProdInsurance)
                    return FontAwesome.UMBRELLA.getHtml();
                else if (product instanceof ProdInstallments)
                    return FontAwesome.MONEY.getHtml();
                else
                    return "";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        gridContainer.addGeneratedProperty(DELETE_BTN, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Удалить";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
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
        grid.setHeightByRows(container.size());
        grid.setEditorEnabled(false);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setColumnOrder(visibleProps);
        grid.getColumn(productNamePath).setHeaderCaption("Продукт");
        grid.getColumn(ProductInSale_.state.getName()).setHeaderCaption("Статус рассмотрения");
        grid.getColumn(PROD_TYPE_ICON).setHeaderCaption("");
        grid.getColumn(PROD_TYPE_ICON).setRenderer(new HtmlRenderer());
        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) { // Открываем форму ввода/редактирования
                ProductInSale productInSale = (ProductInSale) e.getItemId();
                editProduct(productInSale);
            }
        });
//        grid.getColumn(EDIT_BTN).setRenderer(new ButtonRenderer(e -> editProduct((ProductInSale) e.getItemId())));
//        grid.getColumn(EDIT_BTN).setHeaderCaption("");
        grid.getColumn(DELETE_BTN).setRenderer(new ButtonRenderer(e -> deleteProduct((ProductInSale) e.getItemId())));
        grid.getColumn(DELETE_BTN).setHeaderCaption("");
        gridContainer.addItemSetChangeListener(e -> grid.setHeightByRows(container.size()));
        root.addComponent(grid);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            addBtn.setVisible(!isRedOnly);
            grid.setReadOnly(isRedOnly);
        });

        return root;
    }

    private void deleteProduct(ProductInSale itemId) {
        container.removeItem(itemId);
    }

    private void editProduct(ProductInSale productInSale) {
        ExtaEditForm form = createEditForm(productInSale);
        form.addCloseFormListener(ev -> {
            if (form.isSaved()) {
                setValue(newArrayList(container.getItemIds()));
                grid.markAsDirty();
            }
        });
        FormUtils.showModalWin(form);
    }

    private ExtaEditForm createEditForm(ProductInSale productInSale) {
        ExtaEditForm form;
        Product product = productInSale.getProduct();
        if (product instanceof ProdCredit) {
            form = new LoanInSaleEditForm("Кредитный продукт", productInSale, priceSupplier, brandSupplier);
        } else if (product instanceof ProdInsurance) {
            form = new InsuranceInSaleEditForm("Страховой продукт", productInSale, priceSupplier, brandSupplier);
        } else if (product instanceof ProdInstallments) {
            form = new InstallmentInSaleEditForm("Рассрочка", productInSale, priceSupplier, brandSupplier);
        } else
            form = null;
        return form;
    }

    private void addProduct(final Product product) {
        if (priceSupplier.get() != null && brandSupplier.get() != null) {
            final ProductInSale productInSale = new ProductInSale(sale);
            productInSale.setProduct(product);
            ExtaEditForm form = createEditForm(productInSale);
            form.addCloseFormListener(ev -> {
                if (form.isSaved()) {
                    container.addBean(productInSale);
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

}
