package ru.extas.web.sale;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.sale.ProdInsurance;
import ru.extas.model.sale.ProductInSale;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.component.Disclosure;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.product.ProdInsuranceField;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 23.03.2015
 *         Time: 18:24
 */
public class InsuranceInSaleEditForm extends ExtaEditForm<ProductInSale> {
    private final SupplierSer<BigDecimal> priceSupplier;
    private final SupplierSer<String> brandSupplier;
//    private final Sale sale;

    @PropertyId("period")
    private ComboBox periodField;
    @PropertyId("product")
    private ProdInsuranceField productField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;

    private Label vendorLabel;
    private Label tariffLabel;
    private Label premiumLabel;

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *  @param caption       a {@link String} object.
     * @param productInSale
     * @param priceSupplier
     * @param brandSupplier
     */
    protected InsuranceInSaleEditForm(String caption, ProductInSale productInSale, SupplierSer<BigDecimal> priceSupplier, SupplierSer<String> brandSupplier) {
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
        productField = new ProdInsuranceField("Продукт", "Введите название продукта");
        productField.setRequired(true);
        productField.addValueChangeListener(e -> refreshProductFields());
        form.addComponent(productField);

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

        // Обновление рассчетных полей
        refreshProductFields();
        refreshInsCosts();
        // Инициализация взаимосвязей
        initRelations();

        return form;
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
            if (tarif != null) {
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
                if (premium != null) {
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
