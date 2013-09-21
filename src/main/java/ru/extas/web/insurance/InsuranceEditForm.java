package ru.extas.web.insurance;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.joda.time.LocalDate;
import ru.extas.model.Insurance;
import ru.extas.model.Policy;
import ru.extas.server.InsuranceCalculator;
import ru.extas.server.InsuranceRepository;
import ru.extas.server.PolicyRegistry;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.ContactSelect;

import java.math.BigDecimal;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования имущественной страховки
 *
 * @author Valery Orlov
 */
public class InsuranceEditForm extends AbstractEditForm<Insurance> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    @PropertyId("regNum")
    private PolicySelect regNumField;
    @PropertyId("a7Num")
    private A7Select a7FormNumField;
    @PropertyId("date")
    private PopupDateField dateField;
    @PropertyId("client")
    private ComboBox clientNameField;
    @PropertyId("motorType")
    private ComboBox motorTypeField;
    @PropertyId("motorBrand")
    private ComboBox motorBrandField;
    @PropertyId("motorModel")
    private TextField motorModelField;
    @PropertyId("riskSum")
    private TextField riskSumField;
    @PropertyId("premium")
    private TextField premiumField;
    @PropertyId("paymentDate")
    private PopupDateField paymentDateField;
    @PropertyId("startDate")
    private PopupDateField startDateField;
    @PropertyId("endDate")
    private PopupDateField endDateField;
    @PropertyId("pointOfSale")
    private TextField pointOfSaleField;

    public InsuranceEditForm(final String caption, final BeanItem<Insurance> obj) {
        super(caption, obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Insurance obj) {
        final FormLayout form = new FormLayout();

        regNumField = new PolicySelect("Номер полиса",
                "Введите номер полиса страхования. Выбирается из справочника БСО.", obj.getRegNum());
        regNumField.setRequired(true);
        // regNumField.setConverter(String.class);
        regNumField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(final ValueChangeEvent event) {
                if (event.getProperty().getType() == Policy.class) {
                    final Object selItemId = event.getProperty().getValue();
                    if (selItemId != null) {
                        final BeanItem<Policy> policy = (BeanItem<Policy>) regNumField.getItem(selItemId);
                        // Зарезервировать полис в БСО
                        lookup(PolicyRegistry.class).bookPolicy(policy.getBean());
                    }

                }
            }
        });
        form.addComponent(regNumField);

        a7FormNumField = new A7Select("Номер квитанции А-7",
                "Введите номер квитанции А-7. Выбирается из справочника БСО.", obj.getA7Num());
        // TODO a7FormNumField.setRequired(true);
        // form.addComponent(a7FormNumField);

        dateField = new LocalDateField("Дата договора", "Введите дату оформления договора страхования");
        dateField.setRequired(true);
        dateField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // пересчитать дату оплаты страховки
                final LocalDate newDate = (LocalDate) dateField.getConvertedValue();
                if (newDate != null && paymentDateField.getPropertyDataSource() != null)
                    paymentDateField.setConvertedValue(newDate);
            }
        });
        form.addComponent(dateField);

        // FIXME Ограничить выбор контакта только клиентами
        clientNameField = new ContactSelect("Страхователь");
        clientNameField.setRequired(true);
        form.addComponent(clientNameField);

        motorTypeField = new ComboBox("Тип техники");
        motorTypeField.setDescription("Укажите тип страхуемой техники");
        motorTypeField.setInputPrompt("Выберите или начните ввод...");
        motorTypeField.setImmediate(true);
        motorTypeField.setNullSelectionAllowed(false);
        motorTypeField.setNewItemsAllowed(false);
        motorTypeField.setFilteringMode(FilteringMode.CONTAINS);
        motorTypeField.setWidth(13, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadMotorTypes())
            motorTypeField.addItem(item);
        motorTypeField.setRequired(true);
        form.addComponent(motorTypeField);

        motorBrandField = new ComboBox("Марка техники");
        motorBrandField.setDescription("Укажите марку страхуемой техники");
        motorBrandField.setInputPrompt("Выберите или начните ввод...");
        motorBrandField.setImmediate(true);
        motorBrandField.setNullSelectionAllowed(false);
        motorBrandField.setNewItemsAllowed(false);
        motorBrandField.setFilteringMode(FilteringMode.CONTAINS);
        motorBrandField.setWidth(13, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadMotorBrands())
            motorBrandField.addItem(item);
        motorBrandField.setRequired(true);
        motorBrandField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                calculatePremium();
            }
        });
        form.addComponent(motorBrandField);

        motorModelField = new EditField("Модель техники", "Введите модель техники");
        motorModelField.setRequired(true);
        motorModelField.setColumns(13);
        form.addComponent(motorModelField);

        riskSumField = new EditField("Страховая сумма", "Введите сумму возмещения в рублях");
        riskSumField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                calculatePremium();
            }
        });
        riskSumField.setRequired(true);
        form.addComponent(riskSumField);

        premiumField = new EditField("Страховая премия", "Введите стоимость страховки в рублях");
        premiumField.setRequired(true);
        form.addComponent(premiumField);

        paymentDateField = new LocalDateField("Дата оплаты", "Введите дату оплаты страховой премии");
        paymentDateField.setRequired(true);
        paymentDateField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // пересчитать дату начала действия договора
                final LocalDate newDate = (LocalDate) paymentDateField.getConvertedValue();
                final LocalDate startDate = ((LocalDate) startDateField.getConvertedValue());
                if (newDate != null && startDate != null && newDate.isAfter(startDate))
                    startDateField.setConvertedValue(newDate.plusDays(1));
            }
        });
        form.addComponent(paymentDateField);

        startDateField = new LocalDateField("Дата начала договора", "Введите дату начала действия страхового договора");
        startDateField.setRequired(true);
        startDateField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // пересчитать дату окончания договора
                final LocalDate newDate = (LocalDate) startDateField.getConvertedValue();
                if (newDate != null && endDateField.getPropertyDataSource() != null)
                    endDateField.setConvertedValue(newDate.plusYears(1).minusDays(1));
            }
        });
        form.addComponent(startDateField);

        endDateField = new LocalDateField("Дата окончания договора", "Введите дату окончания ");
        endDateField.setRequired(true);
        form.addComponent(endDateField);

        pointOfSaleField = new EditField("Точка продажи", "Название автосоалона где продана страховка");
        pointOfSaleField.setColumns(15);
        pointOfSaleField.setRequired(true);
        form.addComponent(pointOfSaleField);

        return form;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void initObject(final Insurance obj) {
        if (obj.getId() == null) {
            final LocalDate now = LocalDate.now();
            obj.setDate(now);
            obj.setPaymentDate(now);
            obj.setStartDate(obj.getPaymentDate().plusDays(1));
            obj.setEndDate(obj.getStartDate().plusYears(1).minusDays(1));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Insurance obj) {
        final InsuranceRepository insuranceRepository = lookup(InsuranceRepository.class);
        insuranceRepository.persist(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void checkBeforeSave(final Insurance obj) {
    }

    /**
     *
     */
    private void calculatePremium() {
        if (motorBrandField.getPropertyDataSource() != null && premiumField.getPropertyDataSource() != null) {
            final BigDecimal riskSum = (BigDecimal) riskSumField.getConvertedValue();
            final String motorBrand = (String) motorBrandField.getValue();
            if (riskSum != null && motorBrand != null) {
                final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                final BigDecimal premium = calc.calcPropInsPremium(new Insurance(motorBrand, riskSum));
                premiumField.setConvertedValue(premium);
            }
        }
    }

}
