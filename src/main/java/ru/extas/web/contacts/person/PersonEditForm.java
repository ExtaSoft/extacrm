/**
 *
 */
package ru.extas.web.contacts.person;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.*;
import ru.extas.model.contacts.Person.Sex;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.*;
import ru.extas.web.commons.converters.StringToPercentConverter;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;
import ru.extas.web.util.ComponentUtil;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>PersonEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@SuppressWarnings("FieldCanBeLocal")
public class PersonEditForm extends ExtaEditForm<Person> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final static Logger logger = LoggerFactory.getLogger(PersonEditForm.class);
    // Компоненты редактирования
    // Основные персональные данные
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("birthday")
    private PopupDateField birthdayField;
    @PropertyId("sex")
    private OptionGroup sexField;
    @PropertyId("phone")
    private PhoneField cellPhoneField;
    @PropertyId("secondPhone")
    private PhoneField secondPhoneField;
    @PropertyId("workPhone")
    private PhoneField workPhoneField;
    @PropertyId("homePhone")
    private PhoneField homePhoneField;
    @PropertyId("email")
    private EmailField emailField;

    @PropertyId("regAddress.region")
    private RegionSelect regRegionField;
    @PropertyId("regAddress.city")
    private CitySelect regCityField;
    @PropertyId("regAddress.postIndex")
    private EditField regPostIndexField;
    @PropertyId("regAddress.streetBld")
    private TextArea regStreetBldField;
    @PropertyId("regAddress.realtyKind")
    private ComboBox regRealtyKindField;
    @PropertyId("regAddress.periodOfResidence")
    private ComboBox regPeriodOfResidenceField;

    @PropertyId("actualAddress.region")
    private RegionSelect actRegionField;
    @PropertyId("actualAddress.city")
    private CitySelect actCityField;
    @PropertyId("actualAddress.postIndex")
    private EditField actPostIndexField;
    @PropertyId("actualAddress.streetBld")
    private TextArea actStreetBldField;
    @PropertyId("actualAddress.realtyKind")
    private ComboBox actRealtyKindField;
    @PropertyId("actualAddress.periodOfResidence")
    private ComboBox actPeriodOfResidenceField;

    // Паспортнве данные
    @PropertyId("passNum")
    private EditField passNumField;
    @PropertyId("passIssueDate")
    private LocalDateField passIssueDateField;
    @PropertyId("passIssuedBy")
    private TextArea passIssuedByField;

    @PropertyId("passIssuedByNum")
    private EditField passIssuedByNumField;

    @PropertyId("files")
    private FilesManageField docFilesEditor;
    @PropertyId("birthPlace")
    private CitySelect birthPlaceField;
    @PropertyId("citizenship")
    private EditField citizenshipField;
    @PropertyId("changeName")
    private YesNoSelect changeNameField;
    @PropertyId("exName")
    private EditField exNameField;
    @PropertyId("changeNameDate")
    private LocalDateField changeNameDateField;
    @PropertyId("regNactIsSame")
    private CheckBox regNactIsSameField;

    @PropertyId("dlNum")
    private EditField dlNumField;
    @PropertyId("dlIssueDate")
    private LocalDateField dlIssueDateField;
    @PropertyId("dlIssuedBy")
    private TextArea dlIssuedByField;
    @PropertyId("periodOfDriving")
    private EditField periodOfDrivingField;
    @PropertyId("drivingCategories")
    private OptionGroup drivingCategoriesField;

    @PropertyId("maritalStatus")
    private ComboBox maritalStatusField;
    @PropertyId("marriageСontract")
    private YesNoSelect marriageСontractField;
    @PropertyId("children")
    private PersonChildrenField childrenField;
    @PropertyId("livingTogether")
    private EditField livingTogetherField;
    @PropertyId("dependants")
    private EditField dependantsField;
    @PropertyId("spouseName")
    private EditField spouseNameField;
    @PropertyId("spouseBirthday")
    private LocalDateField spouseBirthdayField;
    @PropertyId("spouseBirthPlace")
    private CitySelect spouseBirthPlaceField;
    @PropertyId("spouseCitizenship")
    private EditField spouseCitizenshipField;
    private FormGroupSubHeader spouseHeader;
    @PropertyId("eduKind")
    private ComboBox eduKindField;
    @PropertyId("eduFinish")
    private PopupDateField eduFinishField;
    @PropertyId("eduInstName")
    private EditField eduInstNameField;
    @PropertyId("eduInstINN")
    private EditField eduInstINNField;
    @PropertyId("speciality")
    private EditField specialityField;

    @PropertyId("typeOfEmployment")
    private ComboBox typeOfEmploymentField;
    @PropertyId("tempJobPeriod")
    private EditField tempJobPeriodField;
    @PropertyId("practiceType")
    private EditField practiceTypeField;
    @PropertyId("employerScope")
    private EditField employerScopeField;
    @PropertyId("employerName")
    private EditField employerNameField;
    @PropertyId("employerINN")
    private EditField employerINNField;
    @PropertyId("employerPhone")
    private PhoneField employerPhoneField;
    @PropertyId("officePosition")
    private EditField officePositionField;
    @PropertyId("employerAdress")
    private TextArea employerAdressField;
    @PropertyId("employerWww")
    private EditField employerWwwField;
    @PropertyId("employerDirectorName")
    private EditField employerDirectorNameField;
    @PropertyId("employerAccountantName")
    private EditField employerAccountantNameField;
    @PropertyId("salary")
    private EditField salaryField;
    @PropertyId("lastExperience")
    private EditField lastExperienceField;
    @PropertyId("genExperience")
    private EditField genExperienceField;
    @PropertyId("jobsFor3years")
    private EditField jobsFor3yearsField;

    @PropertyId("businessOwner")
    private YesNoSelect businessOwnerField;
    @PropertyId("businessScope")
    private EditField businessScopeField;
    @PropertyId("businessName")
    private EditField businessNameField;
    @PropertyId("businessINNF")
    private EditField businessINNField;
    @PropertyId("businessPhone")
    private PhoneField businessPhoneField;
    @PropertyId("businessAdress")
    private TextArea businessAdressField;
    @PropertyId("businessPart")
    private EditField businessPartField;
    @PropertyId("businessMumbEmp")
    private EditField businessMumbersEmployedField;
    @PropertyId("businessBalance")
    private EditField businessBalanceField;
    @PropertyId("businessYearlySales")
    private EditField businessYearlySalesField;

    @PropertyId("anotherCredit")
    private YesNoSelect anotherCreditField;
    @PropertyId("anotherCreditBank")
    private EditField anotherCreditBankField;

    @PropertyId("marketingChannel")
    private ComboBox marketingChannelField;
    @PropertyId("accounting4Psychiatrist")
    private YesNoSelect accountingForPsychiatristField;
    @PropertyId("criminalLiability")
    private YesNoSelect criminalLiabilityField;
    @PropertyId("receivership")
    private YesNoSelect receivershipField;
    @PropertyId("collateralProperty")
    private YesNoSelect collateralPropertyField;

    @PropertyId("closeRelativeName")
    private EditField closeRelativeNameField;
    @PropertyId("closeRelativeFiliation")
    private EditField closeRelativeFiliationField;
    @PropertyId("closeRelativeMobPhone")
    private PhoneField closeRelativeMobPhoneField;
    @PropertyId("closeRelativeHomePhone")
    private PhoneField closeRelativeHomePhoneField;

    @PropertyId("realties")
    private PersonRealtyField realtiesField;
    @PropertyId("autos")
    private PersonAutosField autosField;
    @PropertyId("incomes")
    private PersonIncomeField incomesField;
    @PropertyId("expenses")
    private PersonExpensesField expensesField;


    public PersonEditForm(final Person person) {
        super(person.isNew() ?
                "Ввод нового контакта в систему" :
                String.format("Редактирование контакта: %s", person.getName()), person);

        setWinWidth(930, Unit.PIXELS);
    }

    @Override
    protected BeanItem<Person> createBeanItem(final Person bean) {
        final BeanItem<Person> beanItem = new BeanItem<>(bean);
        beanItem.expandProperty("regAddress");
        beanItem.expandProperty("actualAddress");

        return beanItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final Person person) {
        if (person.getRegAddress() == null)
            person.setRegAddress(new AddressInfo());
        if (person.getActualAddress() == null)
            person.setActualAddress(new AddressInfo());
        if (person.isNew()) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
            // пользователя (регион, город)
            person.setSex(Person.Sex.MALE);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Person saveEntity(Person person) {
        logger.debug("Saving contact data...");
        final PersonRepository contactRepository = lookup(PersonRepository.class);
        person = contactRepository.secureSave(person);
        NotificationUtil.showSuccess("Контакт сохранен");
        return person;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        // Форма редактирования персональных данных
        final TabSheet tabsheet = new TabSheet();

        // Вкладка - "Общая информация"
        tabsheet.addTab(createMainForm(), "Общие данные");
        tabsheet.addTab(createQuestionnaireForm(), "Анкета");

        return tabsheet;
    }

    private Component createQuestionnaireForm() {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);
//        formLayout.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Паспортные данные"));
        birthPlaceField = new CitySelect("Место рождения");
        formLayout.addComponent(birthPlaceField);

        citizenshipField = new EditField("Гражданство", "Введите гражданство");
        formLayout.addComponent(citizenshipField);

        passNumField = new EditField("Серия/номер");
        passNumField.setColumns(20);
        formLayout.addComponent(passNumField);

        passIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        formLayout.addComponent(passIssueDateField);

        passIssuedByField = new TextArea("Кем выдан");
        passIssuedByField.setDescription("Наименование органа выдавшего документ");
        passIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        passIssuedByField.setNullRepresentation("");
        passIssuedByField.setRows(2);
        formLayout.addComponent(passIssuedByField);

        passIssuedByNumField = new EditField("Код подразделения");
        formLayout.addComponent(passIssuedByNumField);

        changeNameField = new YesNoSelect("Менялась ли фамилия");
        changeNameField.setDescription("Укажите менялась ли фамилия");
        changeNameField.addValueChangeListener(event -> {
            Boolean isChangeName = (Boolean) event.getProperty().getValue();
            if (isChangeName == null)
                isChangeName = false;
            exNameField.setVisible(isChangeName);
            changeNameDateField.setVisible(isChangeName);
        });
        formLayout.addComponent(changeNameField);

        exNameField = new EditField("Прежняя фамилия", "Укажите прежнюю (до смены) фамилию");
        exNameField.setVisible(getEntity().isChangeName());
        formLayout.addComponent(exNameField);

        changeNameDateField = new LocalDateField("Дата смены фамилии", "Укажите дату когда менялась фамилия");
        changeNameDateField.setVisible(getEntity().isChangeName());
        formLayout.addComponent(changeNameDateField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Адрес по месту постоянной регистрации"));
        regRealtyKindField = new ComboBox("Отношение к недвижимости");
        regRealtyKindField.setWidth(15, Unit.EM);
        regRealtyKindField.setDescription("Укажите отношение к объекту недвижимости по данному адресу");
        regRealtyKindField.setNullSelectionAllowed(false);
        regRealtyKindField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(regRealtyKindField, RealtyKind.class);
        formLayout.addComponent(regRealtyKindField);

        regRegionField = new RegionSelect();
        regRegionField.setDescription("Укажите регион проживания");
        regRegionField.addValueChangeListener(event -> {
            final String newRegion = (String) event.getProperty().getValue();
            final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
            if (city != null)
                regCityField.setValue(city);
        });
        formLayout.addComponent(regRegionField);

        regCityField = new CitySelect();
        regCityField.setDescription("Введите город проживания контакта");
        if (getEntity().getRegAddress().getCity() != null)
            regCityField.addItem(getEntity().getRegAddress().getCity());
        regCityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(SupplementService.class).findRegionByCity(newCity);
            if (region != null)
                regRegionField.setValue(region);
        });
        formLayout.addComponent(regCityField);

        regPostIndexField = new EditField("Почтовый индекс");
        regPostIndexField.setColumns(8);
        regPostIndexField.setInputPrompt("Индекс");
        regPostIndexField.setNullRepresentation("");
        formLayout.addComponent(regPostIndexField);

        regStreetBldField = new TextArea("Адрес");
        regStreetBldField.setRows(2);
        regStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        regStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        regStreetBldField.setNullRepresentation("");
        formLayout.addComponent(regStreetBldField);

        regPeriodOfResidenceField = new ComboBox("Срок проживания");
        regPeriodOfResidenceField.setWidth(15, Unit.EM);
        regPeriodOfResidenceField.setDescription("Укажите срок проживания по данному адресу");
        regPeriodOfResidenceField.setNullSelectionAllowed(false);
        regPeriodOfResidenceField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(regPeriodOfResidenceField, PeriodOfResidence.class);
        formLayout.addComponent(regPeriodOfResidenceField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Адрес фактического проживания"));
        regNactIsSameField = new CheckBox("Совпадает с адресом постоянной регистрации");
        regNactIsSameField.addValueChangeListener(event -> {
            Boolean isRegIsAct = (Boolean) event.getProperty().getValue();
            if (isRegIsAct == null)
                isRegIsAct = false;
            setActualAdressStatus(isRegIsAct);
        });
        formLayout.addComponent(regNactIsSameField);

        actRealtyKindField = new ComboBox("Отношение к недвижимости");
        actRealtyKindField.setWidth(15, Unit.EM);
        actRealtyKindField.setDescription("Укажите отношение к объекту недвижимости по данному адресу");
        actRealtyKindField.setNullSelectionAllowed(false);
        actRealtyKindField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(actRealtyKindField, RealtyKind.class);
        formLayout.addComponent(actRealtyKindField);

        actRegionField = new RegionSelect();
        actRegionField.setDescription("Укажите регион проживания");
        actRegionField.addValueChangeListener(event -> {
            final String newRegion = (String) event.getProperty().getValue();
            final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
            if (city != null)
                actCityField.setValue(city);
        });
        formLayout.addComponent(actRegionField);

        actCityField = new CitySelect();
        actCityField.setDescription("Введите город проживания контакта");
        if (getEntity().getRegAddress().getCity() != null)
            actCityField.addItem(getEntity().getRegAddress().getCity());
        actCityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(SupplementService.class).findRegionByCity(newCity);
            if (region != null)
                actRegionField.setValue(region);
        });
        formLayout.addComponent(actCityField);

        actPostIndexField = new EditField("Почтовый индекс");
        actPostIndexField.setColumns(8);
        actPostIndexField.setInputPrompt("Индекс");
        actPostIndexField.setNullRepresentation("");
        formLayout.addComponent(actPostIndexField);

        actStreetBldField = new TextArea("Адрес");
        actStreetBldField.setRows(2);
        actStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        actStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        actStreetBldField.setNullRepresentation("");
        formLayout.addComponent(actStreetBldField);

        actPeriodOfResidenceField = new ComboBox("Срок проживания");
        actPeriodOfResidenceField.setWidth(15, Unit.EM);
        actPeriodOfResidenceField.setDescription("Укажите срок проживания по данному адресу");
        actPeriodOfResidenceField.setNullSelectionAllowed(false);
        actPeriodOfResidenceField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(actPeriodOfResidenceField, PeriodOfResidence.class);
        formLayout.addComponent(actPeriodOfResidenceField);
        setActualAdressStatus(getEntity().isRegNactIsSame());

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Водительское удостоверение"));
        dlNumField = new EditField("Серия/номер");
        dlNumField.setColumns(20);
        formLayout.addComponent(dlNumField);

        dlIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        formLayout.addComponent(dlIssueDateField);

        dlIssuedByField = new TextArea("Кем выдан");
        dlIssuedByField.setDescription("Наименование органа выдавшего документ");
        dlIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        dlIssuedByField.setNullRepresentation("");
        dlIssuedByField.setRows(2);
        formLayout.addComponent(dlIssuedByField);

        periodOfDrivingField = new EditField("Водительский стаж");
        formLayout.addComponent(periodOfDrivingField);

        drivingCategoriesField = new OptionGroup("Открытые категории");
        drivingCategoriesField.setMultiSelect(true);
        drivingCategoriesField.addStyleName(ExtaTheme.OPTIONGROUP_HORIZONTAL);
        drivingCategoriesField.setDescription("Укажите категории открытые в водительском удостоверении");
        drivingCategoriesField.setNullSelectionAllowed(false);
        drivingCategoriesField.setNewItemsAllowed(false);
        drivingCategoriesField.addItem('A');
        drivingCategoriesField.addItem('B');
        drivingCategoriesField.addItem('C');
        drivingCategoriesField.addItem('D');
        drivingCategoriesField.addItem('E');
        drivingCategoriesField.setConverter(new DrivingCategoriesConverter());
        formLayout.addComponent(drivingCategoriesField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Данные о семье"));
        maritalStatusField = new ComboBox("Семейное положение");
        maritalStatusField.setDescription("Укажите семейное положение");
        maritalStatusField.setWidth(15, Unit.EM);
        maritalStatusField.setNullSelectionAllowed(false);
        maritalStatusField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(maritalStatusField, MaritalStatus.class);
        maritalStatusField.addValueChangeListener(event -> {
            MaritalStatus maritalStatus = (MaritalStatus) event.getProperty().getValue();
            setMaritalStatusUI(maritalStatus);
        });
        formLayout.addComponent(maritalStatusField);

        marriageСontractField = new YesNoSelect("Наличие брачного договора");
        formLayout.addComponent(marriageСontractField);

        childrenField = new PersonChildrenField("Дети", getEntity());
        formLayout.addComponent(childrenField);

        livingTogetherField = new EditField("Кол-во проживающих совместно");
        formLayout.addComponent(livingTogetherField);

        dependantsField = new EditField("Кол-во иждевенцев");
        formLayout.addComponent(dependantsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        spouseHeader = new FormGroupSubHeader("Сведения о супруге");
        formLayout.addComponent(spouseHeader);

        spouseNameField = new EditField("Ф.И.О.");
        nameField.setColumns(25);
        nameField.setDescription("Введите имя (ФИО) супруги(а)");
        nameField.setInputPrompt("Фамилия Имя (Отчество)");
        formLayout.addComponent(spouseNameField);

        spouseBirthdayField = new LocalDateField("Дата рождения");
        formLayout.addComponent(spouseBirthdayField);

        spouseBirthPlaceField = new CitySelect("Место рождения");
        formLayout.addComponent(spouseBirthPlaceField);

        spouseCitizenshipField = new EditField("Гражданство", "Введите гражданство супруги(а)");
        formLayout.addComponent(spouseCitizenshipField);
        setMaritalStatusUI(getEntity().getMaritalStatus());

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Образование"));

        eduKindField = new ComboBox("Образование");
        eduKindField.setDescription("Укажите уровень образования");
        eduKindField.setWidth(15, Unit.EM);
        eduKindField.setNullSelectionAllowed(false);
        eduKindField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(eduKindField, EducationKind.class);
        formLayout.addComponent(eduKindField);

        eduFinishField = new YearField("Год окончания");
        formLayout.addComponent(eduFinishField);

        eduInstNameField = new EditField("Название учебного заведения", "Укажите наименование учебного заведения");
        formLayout.addComponent(eduInstNameField);

        eduInstINNField = new EditField("ИНН", " Укажите ИНН учебного заведения");
        formLayout.addComponent(eduInstINNField);

        specialityField = new EditField("Специальность", "Укажите специальнось по которой проводилось обучения");
        formLayout.addComponent(specialityField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Информация о занятости"));
        formLayout.addComponent(new FormGroupSubHeader("Место работы"));
        // Вид занятости: Работа по трудовому договору/контракту/ИП
        typeOfEmploymentField = new ComboBox("Вид занятости");
        typeOfEmploymentField.setDescription("Укажите уровень образования");
        typeOfEmploymentField.setWidth(15, Unit.EM);
        typeOfEmploymentField.setNullSelectionAllowed(false);
        typeOfEmploymentField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(typeOfEmploymentField, TypeOfEmployment.class);
        typeOfEmploymentField.addValueChangeListener(event -> {
            TypeOfEmployment type = (TypeOfEmployment) event.getProperty().getValue();
            setWorkInfoStatus(type);
        });
        formLayout.addComponent(typeOfEmploymentField);
        // Срок трудового договора (мес.)
        tempJobPeriodField = new EditField("Срок трудового договора (мес.)", "Укажите срок трудового договора в месяцах");
        formLayout.addComponent(tempJobPeriodField);
        // Вид частной практики
        practiceTypeField = new EditField("Вид частной практики", "Укажите подробнее вид частной практики");
        formLayout.addComponent(practiceTypeField);
        // Сфера деятельности
        employerScopeField = new EditField("Сфера деятельности", "Укажите сферу деятельности по месту занятости");
        formLayout.addComponent(employerScopeField);
        // Название организации
        employerNameField = new EditField("Название организации", "Укажите название компании работодателя");
        formLayout.addComponent(employerNameField);
        // ИНН
        employerINNField = new EditField("ИНН", "Укажите ИНН работодателя");
        formLayout.addComponent(employerINNField);
        // Тел/факс:
        employerPhoneField = new PhoneField("Тел/факс", "Укажите телефон или факс компании работодателя");
        formLayout.addComponent(employerPhoneField);
        // Должность
        officePositionField = new EditField("Должность", "Укажите должность");
        formLayout.addComponent(officePositionField);
        // Адрес (фактич.)
        employerAdressField = new TextArea("Адрес (фактич.)");
        employerAdressField.setRows(3);
        employerAdressField.setInputPrompt("Город, Улица, Дом ...");
        employerAdressField.setNullRepresentation("");
        formLayout.addComponent(employerAdressField);
        // Сайт компании
        employerWwwField = new EditField("Сайт компании", "Укажите сайт компании");
        employerWwwField.setInputPrompt("http://...");
        formLayout.addComponent(employerWwwField);
        // Ф.И.О. руководителя
        employerDirectorNameField = new EditField("Ф.И.О. руководителя");
        formLayout.addComponent(employerDirectorNameField);
        // Ф.И.О. гл. бухгалтера
        employerAccountantNameField = new EditField("Ф.И.О. гл. бухгалтера");
        formLayout.addComponent(employerAccountantNameField);
        // Среднемесячный доход
        salaryField = new EditField("Среднемесячный доход", "Укажите, приблизительно, среднемесячный доход");
        formLayout.addComponent(salaryField);
        // Стаж на последнем месте работы
        lastExperienceField = new EditField("Стаж на последнем месте работы (годы)");
        formLayout.addComponent(lastExperienceField);
        // Общий стаж
        genExperienceField = new EditField("Общий рабочий стаж (годы)");
        formLayout.addComponent(genExperienceField);
        // Количество мест за последнии 3 года
        jobsFor3yearsField = new EditField("Число компаний (последние 3 года)");
        formLayout.addComponent(jobsFor3yearsField);
        setWorkInfoStatus(getEntity().getTypeOfEmployment());

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupSubHeader("Бизнес"));
        // Владелец бизнеса
        businessOwnerField = new YesNoSelect("Владелец бизнеса");
        businessOwnerField.addValueChangeListener(e -> {
            Boolean isOwner = (Boolean) e.getProperty().getValue();
            setBusinessInfoStatus(isOwner);
        });
        formLayout.addComponent(businessOwnerField);
        // Сфера деятельности
        businessScopeField = new EditField("Сфера деятельности", "Укажите сферу деятельности компании");
        formLayout.addComponent(businessScopeField);
        // Название организации
        businessNameField = new EditField("Название организации", "Укажите название компании");
        formLayout.addComponent(businessNameField);
        // ИНН
        businessINNField = new EditField("ИНН", "Укажите ИНН компании");
        formLayout.addComponent(businessINNField);
        // Тел/факс:
        businessPhoneField = new PhoneField("Тел/факс", "Укажите телефон или факс компании");
        formLayout.addComponent(businessPhoneField);
        // Адрес (фактич.)
        businessAdressField = new TextArea("Адрес (фактич.)");
        businessAdressField.setRows(3);
        businessAdressField.setInputPrompt("Город, Улица, Дом ...");
        businessAdressField.setNullRepresentation("");
        formLayout.addComponent(businessAdressField);
        // Доля в уставном капитале, %
        businessPartField = new EditField("Доля в уставном капитале, %");
        businessPartField.setConverter(lookup(StringToPercentConverter.class));
        formLayout.addComponent(businessPartField);
        // кол-во работников
        businessMumbersEmployedField = new EditField("Число сотрудников");
        formLayout.addComponent(businessMumbersEmployedField);
        // Валюта баланса, руб.
        businessBalanceField = new EditField("Валюта баланса, руб.");
        formLayout.addComponent(businessBalanceField);
        // Годовой оборот или годовая выручка, руб
        businessYearlySalesField = new EditField("Годовой оборот или годовая выручка, руб");
        formLayout.addComponent(businessYearlySalesField);
        setBusinessInfoStatus(getEntity().isBusinessOwner());

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Информация о собственности"));

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupSubHeader("Недвижимость"));
        realtiesField = new PersonRealtyField(getEntity());
        //realtiesField.setWidth(25, Unit.EM);
        formLayout.addComponent(realtiesField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupSubHeader("Автотранспорт"));
        autosField = new PersonAutosField(getEntity());
        formLayout.addComponent(autosField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Информация о доходах/расходах"));
        formLayout.addComponent(new FormGroupSubHeader("Доходы"));
        incomesField = new PersonIncomeField(getEntity());
        formLayout.addComponent(incomesField);

        formLayout.addComponent(new FormGroupSubHeader("Расходы"));
        expensesField = new PersonExpensesField(getEntity());
        formLayout.addComponent(expensesField);


        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Кредиты банков и/или других организаций"));
        // Пользуетесь ли Вы кредитом
        anotherCreditField = new YesNoSelect("Пользуетесь ли Вы кредитом");
        anotherCreditField.addValueChangeListener(e -> anotherCreditBankField.setVisible((Boolean) e.getProperty().getValue()));
        formLayout.addComponent(anotherCreditField);
        // Наименование Банка
        anotherCreditBankField = new EditField("Наименование Банка");
        anotherCreditBankField.setVisible(getEntity().isAnotherCredit());
        formLayout.addComponent(anotherCreditBankField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Дополнительные сведения"));
        // Откуда Вы о нас узнали
        marketingChannelField = new ComboBox("Маркетинговый источник",
                newArrayList("СМИ", "Автосалон", "Партнеры КА", "Печатная продукция", "Наружная реклама", "Повторное обращение"));
        marketingChannelField.setDescription("Укажите источник из которого клиент узнал о Экстрим Ассистанс");
        marketingChannelField.setWidth(15, Unit.EM);
        marketingChannelField.setNullSelectionAllowed(false);
        marketingChannelField.setNewItemsAllowed(true);
        formLayout.addComponent(marketingChannelField);
        // Состоите (состояли) ли Вы на учете у врача нарколога?
        accountingForPsychiatristField = new YesNoSelect("На учете у врача нарколога");
        accountingForPsychiatristField.setDescription("Состоите (состояли) ли Вы на учете у врача нарколога?");
        formLayout.addComponent(accountingForPsychiatristField);
        // Привлекались ли Вы или привлекаетесь в настоящее к уголовной ответственности
        criminalLiabilityField = new YesNoSelect("Привлекался к уг. ответственности");
        criminalLiabilityField.setDescription("Привлекался ли или привлекается в настоящее к уголовной ответственности?");
        formLayout.addComponent(criminalLiabilityField);
        // Являетесь ли Вы ответчиком по имущественным спорам?
        receivershipField = new YesNoSelect("Ответчик по имущ. спорам");
        receivershipField.setDescription("Является ли ответчиком по имущественным спорам?");
        formLayout.addComponent(receivershipField);
        // Является ли ваше имущество предметом залога, наложен ли на него арест?
        collateralPropertyField = new YesNoSelect("Имущество в залоге/аресте");
        collateralPropertyField.setDescription("Является ли имущество предметом залога, наложен ли на него арест?");
        formLayout.addComponent(collateralPropertyField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Ближайший родственник для экстренной связи"));
        // Фамилия Имя Отчество
        closeRelativeNameField = new EditField("Ф.И.О.");
        formLayout.addComponent(closeRelativeNameField);
        // Степень родства
        closeRelativeFiliationField = new EditField("Степень родства");
        formLayout.addComponent(closeRelativeFiliationField);
        // Контактный телефон моб.
        closeRelativeMobPhoneField = new PhoneField("Контактный телефон моб.");
        formLayout.addComponent(closeRelativeMobPhoneField);
        // Контактный телефон дом.
        closeRelativeHomePhoneField = new PhoneField("Контактный телефон моб.");
        formLayout.addComponent(closeRelativeHomePhoneField);

        return formLayout;
    }

    private Component createMainForm() {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);
//        formLayout.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Персональные данные"));
        nameField = new EditField("Имя");
        nameField.setColumns(30);
        nameField.setDescription("Введите имя (ФИО) контакта");
        nameField.setInputPrompt("Фамилия Имя (Отчество)");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя контакта не может быть пустым. Пожалуйста введите ФИО контакта.");
        formLayout.addComponent(nameField);

        sexField = new OptionGroup("Пол");
        sexField.setMultiSelect(false);
        sexField.addStyleName(ExtaTheme.OPTIONGROUP_HORIZONTAL);
        sexField.setDescription("Укажите пол контакта");
        sexField.setRequired(true);
        sexField.setNullSelectionAllowed(false);
        sexField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(sexField, Sex.class);
        sexField.setItemIcon(Sex.MALE, Fontello.MALE);
        sexField.setItemIcon(Sex.FEMALE, Fontello.FEMALE);
        formLayout.addComponent(sexField);

        birthdayField = new LocalDateField("Дата рождения", "Введите дату рождения контакта");
        birthdayField.setImmediate(true);
        birthdayField.setInputPrompt("31.12.1978");
        birthdayField.setDateFormat("dd.MM.yyyy");
        birthdayField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
        formLayout.addComponent(birthdayField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Контактные данные"));
        cellPhoneField = new PhoneField("Мобильный телефон");
        formLayout.addComponent(cellPhoneField);

        secondPhoneField = new PhoneField("Доп. мобильный телефон");
        formLayout.addComponent(secondPhoneField);

        workPhoneField = new PhoneField("Рабочий телефон");
        formLayout.addComponent(workPhoneField);

        homePhoneField = new PhoneField("Домашний телефон");
        formLayout.addComponent(homePhoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Документы контакта
        formLayout.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(PersonFileContainer.class);
        formLayout.addComponent(docFilesEditor);

        return formLayout;
    }

    private void setBusinessInfoStatus(final Boolean isOwner) {
        businessScopeField.setVisible(isOwner);
        businessNameField.setVisible(isOwner);
        businessINNField.setVisible(isOwner);
        businessPhoneField.setVisible(isOwner);
        businessAdressField.setVisible(isOwner);
        businessPartField.setVisible(isOwner);
        businessMumbersEmployedField.setVisible(isOwner);
        businessBalanceField.setVisible(isOwner);
        businessYearlySalesField.setVisible(isOwner);
    }

    private void setWorkInfoStatus(final TypeOfEmployment type) {
        final boolean jobPeriodVisible = type == TypeOfEmployment.TEMPORARY;
        final boolean practiceTypeVisible = type == TypeOfEmployment.PRACTICE;
        final boolean jobInfoVisible = type != null && type != TypeOfEmployment.UNEMPLOYED;
        tempJobPeriodField.setVisible(jobPeriodVisible);
        practiceTypeField.setVisible(practiceTypeVisible);
        employerScopeField.setVisible(jobInfoVisible);
        employerNameField.setVisible(jobInfoVisible);
        employerINNField.setVisible(jobInfoVisible);
        employerPhoneField.setVisible(jobInfoVisible);
        officePositionField.setVisible(jobInfoVisible);
        employerAdressField.setVisible(jobInfoVisible);
        employerWwwField.setVisible(jobInfoVisible);
        employerDirectorNameField.setVisible(jobInfoVisible);
        employerAccountantNameField.setVisible(jobInfoVisible);
        salaryField.setVisible(jobInfoVisible);
        lastExperienceField.setVisible(jobInfoVisible);
        genExperienceField.setVisible(jobInfoVisible);
        jobsFor3yearsField.setVisible(jobInfoVisible);
    }

    private void setMaritalStatusUI(final MaritalStatus maritalStatus) {
        final boolean hasntFamily = maritalStatus == null || maritalStatus == MaritalStatus.SINGLE;
        marriageСontractField.setVisible(!hasntFamily);
        childrenField.setVisible(!hasntFamily);
        livingTogetherField.setVisible(!hasntFamily);
        dependantsField.setVisible(!hasntFamily);
        spouseNameField.setVisible(!hasntFamily);
        spouseBirthdayField.setVisible(!hasntFamily);
        spouseBirthPlaceField.setVisible(!hasntFamily);
        spouseCitizenshipField.setVisible(!hasntFamily);
        spouseHeader.setVisible(!hasntFamily);
    }

    private void setActualAdressStatus(final Boolean isRegIsAct) {
        actRegionField.setVisible(!isRegIsAct);
        actCityField.setVisible(!isRegIsAct);
        actPostIndexField.setVisible(!isRegIsAct);
        actStreetBldField.setVisible(!isRegIsAct);
        actRealtyKindField.setVisible(!isRegIsAct);
        actPeriodOfResidenceField.setVisible(!isRegIsAct);
    }

    private FormLayout createSalesForm() {
        final FormLayout form = new ExtaFormLayout();
        form.setMargin(true);

        return form;
    }

    private static class DrivingCategoriesConverter implements Converter<Object, String> {

        @Override
        public String convertToModel(final Object value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
            String s = null;
            if (value != null) {
                final Set<Character> set = (Set<Character>) value;
                s = String.valueOf(Chars.toArray(set));
            }
            return s;
        }

        @Override
        public Object convertToPresentation(final String value, final Class<?> targetType, final Locale locale) throws ConversionException {
            if (value != null) {
                final HashSet hashSet = newHashSet(Lists.charactersOf(value));
                return hashSet;
            }
            return null;
        }

        @Override
        public Class<String> getModelType() {
            return String.class;
        }

        @Override
        public Class<Object> getPresentationType() {
            return Object.class;
        }
    }
}
