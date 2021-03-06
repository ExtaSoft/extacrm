package ru.extas.web.insurance;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.*;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.Employee;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.insurance.InsuranceComment;
import ru.extas.model.insurance.InsuranceFileContainer;
import ru.extas.model.insurance.Policy;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.server.insurance.InsuranceRepository;
import ru.extas.server.insurance.PolicyRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.CommentsField;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.contacts.ClientField;
import ru.extas.web.contacts.salepoint.SalePointField;
import ru.extas.web.motor.MotorBrandSelect;
import ru.extas.web.motor.MotorTypeSelect;
import ru.extas.web.util.ComponentUtil;

import java.math.BigDecimal;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования имущественной страховки
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3.0
 */
public class InsuranceEditForm extends ExtaEditForm<Insurance> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    @PropertyId("regNum")
    private PolicySelect regNumField;
    @PropertyId("a7Num")
    private A7Select a7FormNumField;
    @PropertyId("date")
    private PopupDateField dateField;
    @PropertyId("client")
    private ClientField clientField;
    @PropertyId("beneficiary")
    private ComboBox beneficiaryField;
    @PropertyId("usedMotor")
    private CheckBox usedMotorField;
    @PropertyId("motorType")
    private MotorTypeSelect motorTypeField;
    @PropertyId("motorBrand")
    private MotorBrandSelect motorBrandField;
    @PropertyId("motorModel")
    private TextField motorModelField;
    @PropertyId("motorVin")
    private EditField motorVinField;
    @PropertyId("saleNum")
    private EditField saleNumField;
    @PropertyId("saleDate")
    private LocalDateField saleDateField;
    @PropertyId("riskSum")
    private TextField riskSumField;
    @PropertyId("coverTime")
    private OptionGroup coverTimeField;
    @PropertyId("premium")
    private TextField premiumField;
    @PropertyId("paymentDate")
    private PopupDateField paymentDateField;
    @PropertyId("startDate")
    private PopupDateField startDateField;
    @PropertyId("endDate")
    private PopupDateField endDateField;
    @PropertyId("dealer")
    private SalePointField dealerField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;
    @PropertyId("docComplete")
    private CheckBox docCompleteField;
    @PropertyId("comments")
    private CommentsField<InsuranceComment> commentsField;


    private Label tarifField;
    private ObjectProperty<BigDecimal> tarifDataSource;


    public InsuranceEditForm(final Insurance insurance) {
        super(insurance.isNew() ?
                        "Новый полис" :
                        "Редактировать полис",
                insurance);
        setWinWidth(930, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();
        form.setMargin(true);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Полис"));
        regNumField = new PolicySelect("Номер полиса",
                "Введите номер полиса страхования. Выбирается из справочника БСО.", getEntity().getRegNum());
        regNumField.setRequired(true);
        regNumField.addValueChangeListener(event -> {
            if (event.getProperty().getType() == Policy.class) {
                final Object selItemId = event.getProperty().getValue();
                if (selItemId != null) {
                    final BeanItem<Policy> policy = (BeanItem<Policy>) regNumField.getItem(selItemId);
                    // Зарезервировать полис в БСО
                    lookup(PolicyRepository.class).bookPolicy(policy.getBean());
                }
            }
        });
        form.addComponent(regNumField);

        a7FormNumField = new A7Select("Номер квитанции А-7",
                "Введите номер квитанции А-7. Выбирается из справочника БСО.", getEntity().getA7Num());
        // TODO a7FormNumField.setRequired(true);
        form.addComponent(a7FormNumField);

        dateField = new LocalDateField("Дата договора", "Введите дату оформления договора страхования");
        dateField.setRequired(true);
        dateField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID1 = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                // пересчитать дату оплаты страховки
                final LocalDate newDate = (LocalDate) dateField.getConvertedValue();
                if (newDate != null && paymentDateField.getPropertyDataSource() != null)
                    paymentDateField.setConvertedValue(newDate);
            }
        });
        form.addComponent(dateField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Страхователь/выгодопреобретатель"));
        final String caption = "Страхователь";
        clientField = new ClientField(caption);
        clientField.addValueChangeListener(event -> {
            final Client client = clientField.getValue();
            if (beneficiaryField.getPropertyDataSource() != null && client != null) {
                fillBeneficiariesChoice(client.getName());
                beneficiaryField.setValue(client.getName());
            }
        });
        form.addComponent(clientField);

        beneficiaryField = new ComboBox("Выгодопреобретатель");
        beneficiaryField.setDescription("Введите имя выгодопреобретателя по данному договору страхования");
        beneficiaryField.setNewItemsAllowed(true);
        beneficiaryField.setRequired(true);
        beneficiaryField.setWidth(25, Unit.EM);
        final Client client = getEntity().getClient();
        if(client != null)
            fillBeneficiariesChoice(client.getName());
        form.addComponent(beneficiaryField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Техника"));
        usedMotorField = new CheckBox("Б/у техника");
        usedMotorField.setDescription("Признак бывшей в употреблении техники");
        usedMotorField.addValueChangeListener(event -> {
            calculatePremium();
            updateTarifField();
        });
        form.addComponent(usedMotorField);

        motorTypeField = new MotorTypeSelect();
        motorTypeField.setRequired(true);
        form.addComponent(motorTypeField);

        motorBrandField = new MotorBrandSelect();
        motorBrandField.setRequired(true);
        motorBrandField.addValueChangeListener(event -> {
            calculatePremium();
            updateTarifField();
        });
        motorBrandField.linkToType(motorTypeField);
        form.addComponent(motorBrandField);

        tarifDataSource = new ObjectProperty<>(new BigDecimal(0));
        tarifField = new Label(tarifDataSource);
        tarifField.setCaption("Тариф");
        tarifField.setConverter(lookup(StringToPercentConverter.class));
        form.addComponent(tarifField);

        motorModelField = new EditField("Модель техники", "Введите модель техники");
        motorModelField.setRequired(true);
        motorModelField.setColumns(25);
        form.addComponent(motorModelField);

        motorVinField = new EditField("VIN", "Введите Идентификационный номер транспортного средства");
        motorVinField.setRequired(true);
        motorVinField.setColumns(20);
        form.addComponent(motorVinField);

        saleNumField = new EditField("Номер договора купли-продажи", "Введите номер договора купли-продажи");
        saleNumField.setRequired(true);
        saleNumField.setColumns(20);
        form.addComponent(saleNumField);

        saleDateField = new LocalDateField("Дата договора купли-продажи", "Введите дату договора купли-продажи");
        saleDateField.setRequired(true);
        form.addComponent(saleDateField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Параметры страхования"));
        riskSumField = new EditField("Страховая сумма", "Введите сумму возмещения в рублях");
        riskSumField.addValueChangeListener(event -> calculatePremium());
        riskSumField.setRequired(true);
        form.addComponent(riskSumField);

        // По умолчанию страховка на год.
        if (getEntity().getCoverTime() == null)
            getEntity().setCoverTime(Insurance.PeriodOfCover.YEAR);
        coverTimeField = new OptionGroup("Срок страхования");
        coverTimeField.setDescription("Укажите требуемый срок действия страхового полиса");
        coverTimeField.setRequired(true);
        coverTimeField.setNullSelectionAllowed(false);
        coverTimeField.setNewItemsAllowed(false);
        coverTimeField.setImmediate(true);
        ComponentUtil.fillSelectByEnum(coverTimeField, Insurance.PeriodOfCover.class);
        coverTimeField.addValueChangeListener(event -> {
            calculatePremium();
            calcEndDate();
            updateTarifField();
        });
        form.addComponent(coverTimeField);

        premiumField = new EditField("Страховая премия", "Введите стоимость страховки в рублях");
        premiumField.setRequired(true);
        form.addComponent(premiumField);

        paymentDateField = new LocalDateField("Дата оплаты", "Введите дату оплаты страховой премии");
        paymentDateField.setRequired(true);
        paymentDateField.addValueChangeListener(event -> {
            // пересчитать дату начала действия договора
            final LocalDate newDate = (LocalDate) paymentDateField.getConvertedValue();
            final LocalDate startDate = ((LocalDate) startDateField.getConvertedValue());
            if (newDate != null && startDate != null && newDate.isAfter(startDate))
                startDateField.setConvertedValue(newDate.plusDays(1));
        });
        form.addComponent(paymentDateField);

        startDateField = new LocalDateField("Дата начала договора", "Введите дату начала действия страхового договора");
        startDateField.setRequired(true);
        startDateField.addValueChangeListener(event -> calcEndDate());
        form.addComponent(startDateField);

        endDateField = new LocalDateField("Дата окончания договора", "Введите дату окончания");
        endDateField.setRequired(true);
        form.addComponent(endDateField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Точка продажи"));
        dealerField = new SalePointField("Точка продажи", "Название мотосалона где продана страховка");
        // dealerField.setRequired(true);
        form.addComponent(dealerField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(InsuranceFileContainer.class);
        form.addComponent(docFilesEditor);

        docCompleteField = new CheckBox("Полный комплект документов");
        docCompleteField.setDescription("Укажите когда все документы загружены");
        form.addComponent(docCompleteField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Коментарии"));
        commentsField = new CommentsField<>(InsuranceComment.class);
        commentsField.addValueChangeListener(forceModified);
        form.addComponent(commentsField);

        return form;
    }

    private void fillBeneficiariesChoice(final String clientName) {
        // Очищаем все
        beneficiaryField.removeAllItems();
        if (clientName != null)
            beneficiaryField.addItem(clientName);
        // Добавляем заданных выгодопреобретателей
        beneficiaryField.addItem("ВТБ24 (ПАО)");
        beneficiaryField.addItem("ООО \"Финпрайд\"");
        beneficiaryField.addItem("ООО КБ \"АйМаниБанк\"");
        beneficiaryField.addItem("ООО \"МОТОРАССРОЧКА\"");
        beneficiaryField.addItem("ООО \"Аренда Авто\"");
        beneficiaryField.addItem("ООО \"Аренда Транспорта\"");
        beneficiaryField.addItem("ООО \"Маурислизинг\"");
    }

    private void updateTarifField() {
        if (motorBrandField.getPropertyDataSource() != null &&
                coverTimeField.getContainerDataSource() != null &&
                usedMotorField.getPropertyDataSource() != null) {
            final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
            final String motorBrand = (String) motorBrandField.getValue();
            final Boolean isUsed = usedMotorField.getValue();
            if (coverPeriod != null && motorBrand != null && isUsed != null) {
                final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                final BigDecimal premium = calc.findTarif(motorBrand, coverPeriod, isUsed);
                tarifDataSource.setValue(premium);
            }
        }
    }

    private void calcEndDate() {
        // пересчитать дату окончания договора
        final LocalDate newDate = (LocalDate) startDateField.getConvertedValue();
        if (newDate != null && endDateField.getPropertyDataSource() != null) {
            final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
            if (coverPeriod == Insurance.PeriodOfCover.YEAR)
                endDateField.setConvertedValue(newDate.plusYears(1).minusDays(1));
            else
                endDateField.setConvertedValue(newDate.plusMonths(6).minusDays(1));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final Insurance insurance) {
        if (insurance.isNew()) {
            final LocalDate now = LocalDate.now(lookup(DateTimeZone.class));
            insurance.setDate(now);
            insurance.setPaymentDate(now);
            insurance.setCoverTime(Insurance.PeriodOfCover.YEAR);
            insurance.setStartDate(insurance.getPaymentDate().plusDays(1));
            insurance.setEndDate(insurance.getStartDate().plusYears(1).minusDays(1));
            final UserManagementService userService = lookup(UserManagementService.class);
            final Employee user = userService.getCurrentUserEmployee();
            if (user != null) {
                if (user.getWorkPlace() != null)
                    insurance.setDealer(user.getWorkPlace());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Insurance saveEntity(Insurance insurance) {
        insurance = lookup(InsuranceRepository.class).saveAndIssue(insurance);
        NotificationUtil.showSuccess("Полис сохранен");
        return insurance;
    }

    /**
     *
     */
    private void calculatePremium() {
        if (motorBrandField.getPropertyDataSource() != null &&
                premiumField.getPropertyDataSource() != null &&
                coverTimeField.getContainerDataSource() != null &&
                usedMotorField.getPropertyDataSource() != null) {
            final Insurance.PeriodOfCover coverPeriod = (Insurance.PeriodOfCover) coverTimeField.getConvertedValue();
            final BigDecimal riskSum = (BigDecimal) riskSumField.getConvertedValue();
            final String motorBrand = (String) motorBrandField.getValue();
            final boolean isUsedMotor = usedMotorField.getValue();
            if (riskSum != null && motorBrand != null) {
                final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                final BigDecimal premium = calc.calcPropInsPremium(new Insurance(motorBrand, riskSum, coverPeriod, isUsedMotor));
                premiumField.setConvertedValue(premium);
            }
        }
    }

}
