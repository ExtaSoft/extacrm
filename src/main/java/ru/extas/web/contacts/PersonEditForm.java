/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Chars;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;
import ru.extas.web.util.ComponentUtil;

import java.util.*;

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
    private DocFilesEditor docFilesEditor;
    @PropertyId("birthPlace")
    private CitySelect birthPlaceField;
    @PropertyId("citizenship")
    private EditField citizenshipField;
    @PropertyId("changeName")
    private CheckBox changeNameField;
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


    public PersonEditForm(Person person) {
        super(person.isNew() ?
                "Ввод нового контакта в систему" :
                String.format("Редактирование контакта: %s", person.getName()));

        BeanItem<Person> beanItem = new BeanItem<>(person);
        beanItem.expandProperty("regAddress");
        beanItem.expandProperty("actualAddress");
        initForm(beanItem);
    }


    /** {@inheritDoc} */
    @Override
    protected void initObject(final Person obj) {
        if (obj.getRegAddress() == null)
            obj.setRegAddress(new AddressInfo());
        if (obj.isNew()) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
            // пользователя (регион, город)
            obj.setSex(Person.Sex.MALE);
            obj.setJobPosition(Person.Position.EMPLOYEE);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected Person saveObject(Person obj) {
        logger.debug("Saving contact data...");
        final PersonRepository contactRepository = lookup(PersonRepository.class);
        obj = contactRepository.secureSave(obj);
        NotificationUtil.showSuccess("Контакт сохранен");
        return obj;
    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final Person obj) {
    }


    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final Person obj) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Форма редактирования персональных данных
        final FormLayout personForm = new ExtaFormLayout();
        personForm.setSizeUndefined();
        personForm.setMargin(true);

        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Персональные данные"));
        nameField = new EditField("Имя");
        nameField.setColumns(30);
        nameField.setDescription("Введите имя (ФИО) контакта");
        nameField.setInputPrompt("Фамилия Имя (Отчество)");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя контакта не может быть пустым. Пожалуйста введите ФИО контакта.");
        personForm.addComponent(nameField);

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
        personForm.addComponent(sexField);

        birthdayField = new LocalDateField("Дата рождения", "Введите дату рождения контакта");
        birthdayField.setImmediate(true);
        birthdayField.setInputPrompt("31.12.1978");
        birthdayField.setDateFormat("dd.MM.yyyy");
        birthdayField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
        personForm.addComponent(birthdayField);

        birthPlaceField = new CitySelect("Место рождения");
        personForm.addComponent(birthPlaceField);

        citizenshipField = new EditField("Гражданство", "Введите гражданство");
        personForm.addComponent(citizenshipField);

        changeNameField = new CheckBox("Менялась ли фамилия", false);
        changeNameField.setDescription("Укажите менялась ли фамилия");
        changeNameField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean isChangeName = (Boolean) event.getProperty().getValue();
                if(isChangeName == null)
                    isChangeName = false;
                exNameField.setVisible(isChangeName);
                changeNameDateField.setVisible(isChangeName);
            }
        });
        personForm.addComponent(changeNameField);

        exNameField = new EditField("Прежняя фамилия", "Укажите прежнюю (до смены) фамилию");
        exNameField.setVisible(obj.isChangeName());
        personForm.addComponent(exNameField);

        changeNameDateField = new LocalDateField("Дата смены фамилии", "Укажите дату когда менялась фамилия");
        changeNameDateField.setVisible(obj.isChangeName());
        personForm.addComponent(changeNameDateField);


        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Контактные данные"));
        cellPhoneField = new PhoneField("Мобильный телефон");
        personForm.addComponent(cellPhoneField);

        workPhoneField = new PhoneField("Рабочий телефон");
        personForm.addComponent(workPhoneField);

        homePhoneField = new PhoneField("Домашний телефон");
        personForm.addComponent(homePhoneField);

        emailField = new EmailField("E-Mail");
        personForm.addComponent(emailField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Адрес по месту постоянной регистрации"));
        regRealtyKindField = new ComboBox("Отношение к недвижимости");
        regRealtyKindField.setWidth(15, Unit.EM);
        regRealtyKindField.setMultiSelect(false);
        regRealtyKindField.setDescription("Укажите отношение к объекту недвижимости по данному адресу");
        regRealtyKindField.setNullSelectionAllowed(false);
        regRealtyKindField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(regRealtyKindField, RealtyKind.class);
        personForm.addComponent(regRealtyKindField);

        regRegionField = new RegionSelect();
        regRegionField.setDescription("Укажите регион проживания");
        regRegionField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID1 = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newRegion = (String) event.getProperty().getValue();
                final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
                if (city != null)
                    regCityField.setValue(city);
            }
        });
        personForm.addComponent(regRegionField);

        regCityField = new CitySelect();
        regCityField.setDescription("Введите город проживания контакта");
        if (obj.getRegAddress().getCity() != null)
            regCityField.addItem(obj.getRegAddress().getCity());
        regCityField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID1 = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newCity = (String) event.getProperty().getValue();
                final String region = lookup(SupplementService.class).findRegionByCity(newCity);
                if (region != null)
                    regRegionField.setValue(region);
            }
        });
        personForm.addComponent(regCityField);

        regPostIndexField = new EditField("Почтовый индекс");
        regPostIndexField.setColumns(8);
        regPostIndexField.setInputPrompt("Индекс");
        regPostIndexField.setNullRepresentation("");
        personForm.addComponent(regPostIndexField);

        regStreetBldField = new TextArea("Адрес");
        regStreetBldField.setColumns(20);
        regStreetBldField.setRows(2);
        regStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        regStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        regStreetBldField.setNullRepresentation("");
        personForm.addComponent(regStreetBldField);

        regPeriodOfResidenceField = new ComboBox("Срок проживания");
        regPeriodOfResidenceField.setWidth(15, Unit.EM);
        regPeriodOfResidenceField.setMultiSelect(false);
        regPeriodOfResidenceField.setDescription("Укажите срок проживания по данному адресу");
        regPeriodOfResidenceField.setNullSelectionAllowed(false);
        regPeriodOfResidenceField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(regPeriodOfResidenceField, PeriodOfResidence.class);
        personForm.addComponent(regPeriodOfResidenceField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Адрес фактического проживания"));
        regNactIsSameField = new CheckBox("Совпадает с адресом постоянной регистрации");
        regNactIsSameField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean isRegIsAct = (Boolean) event.getProperty().getValue();
                if(isRegIsAct == null)
                    isRegIsAct = false;
                setActualAdressStatus(isRegIsAct);
            }
        });
        personForm.addComponent(regNactIsSameField);

        actRealtyKindField = new ComboBox("Отношение к недвижимости");
        actRealtyKindField.setWidth(15, Unit.EM);
        actRealtyKindField.setMultiSelect(false);
        actRealtyKindField.setDescription("Укажите отношение к объекту недвижимости по данному адресу");
        actRealtyKindField.setNullSelectionAllowed(false);
        actRealtyKindField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(actRealtyKindField, RealtyKind.class);
        personForm.addComponent(actRealtyKindField);

        actRegionField = new RegionSelect();
        actRegionField.setDescription("Укажите регион проживания");
        actRegionField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID1 = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newRegion = (String) event.getProperty().getValue();
                final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
                if (city != null)
                    actCityField.setValue(city);
            }
        });
        personForm.addComponent(actRegionField);

        actCityField = new CitySelect();
        actCityField.setDescription("Введите город проживания контакта");
        if (obj.getRegAddress().getCity() != null)
            actCityField.addItem(obj.getRegAddress().getCity());
        actCityField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID1 = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newCity = (String) event.getProperty().getValue();
                final String region = lookup(SupplementService.class).findRegionByCity(newCity);
                if (region != null)
                    actRegionField.setValue(region);
            }
        });
        personForm.addComponent(actCityField);

        actPostIndexField = new EditField("Почтовый индекс");
        actPostIndexField.setColumns(8);
        actPostIndexField.setInputPrompt("Индекс");
        actPostIndexField.setNullRepresentation("");
        personForm.addComponent(actPostIndexField);

        actStreetBldField = new TextArea("Адрес");
        actStreetBldField.setColumns(20);
        actStreetBldField.setRows(2);
        actStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        actStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        actStreetBldField.setNullRepresentation("");
        personForm.addComponent(actStreetBldField);

        actPeriodOfResidenceField = new ComboBox("Срок проживания");
        actPeriodOfResidenceField.setWidth(15, Unit.EM);
        actPeriodOfResidenceField.setMultiSelect(false);
        actPeriodOfResidenceField.setDescription("Укажите срок проживания по данному адресу");
        actPeriodOfResidenceField.setNullSelectionAllowed(false);
        actPeriodOfResidenceField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(actPeriodOfResidenceField, PeriodOfResidence.class);
        personForm.addComponent(actPeriodOfResidenceField);
        setActualAdressStatus(obj.isRegNactIsSame());

        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Паспорт"));
        passNumField = new EditField("Серия/номер");
        passNumField.setColumns(20);
        personForm.addComponent(passNumField);

        passIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        personForm.addComponent(passIssueDateField);

        passIssuedByField = new TextArea("Кем выдан");
        passIssuedByField.setDescription("Наименование органа выдавшего документ");
        passIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        passIssuedByField.setNullRepresentation("");
        passIssuedByField.setRows(2);
        passIssuedByField.setColumns(20);
        personForm.addComponent(passIssuedByField);

        passIssuedByNumField = new EditField("Код подразделения");
        personForm.addComponent(passIssuedByNumField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        personForm.addComponent(new FormGroupHeader("Водительское удостоверение"));
        dlNumField = new EditField("Серия/номер");
        dlNumField.setColumns(20);
        personForm.addComponent(dlNumField);

        dlIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        personForm.addComponent(dlIssueDateField);

        dlIssuedByField = new TextArea("Кем выдан");
        dlIssuedByField.setDescription("Наименование органа выдавшего документ");
        dlIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        dlIssuedByField.setNullRepresentation("");
        dlIssuedByField.setRows(2);
        dlIssuedByField.setColumns(20);
        personForm.addComponent(dlIssuedByField);

        periodOfDrivingField = new EditField("Водительский стаж");
        personForm.addComponent(periodOfDrivingField);

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
        personForm.addComponent(drivingCategoriesField);

        // Форма просмотра истории продаж
        final FormLayout salesForm = createSalesForm();
        tabsheet.addTab(salesForm).setCaption("История продаж");

        final Component docsForm = createDocsForm();
        tabsheet.addTab(docsForm).setCaption("Документы");

        return personForm;
    }

    public void setActualAdressStatus(Boolean isRegIsAct) {
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

    private Component createDocsForm() {
        docFilesEditor = new DocFilesEditor(PersonFileContainer.class);

        return docFilesEditor;
    }

    private static class DrivingCategoriesConverter implements Converter<Object, String> {

        @Override
        public String convertToModel(Object value, Class<? extends String> targetType, Locale locale) throws ConversionException {
            String s = null;
            if (value != null) {
                Set<Character> set = (Set<Character>) value;
                s = String.valueOf(Chars.toArray(set));
            }
            return s;
        }

        @Override
        public Object convertToPresentation(String value, Class<?> targetType, Locale locale) throws ConversionException {
            if (value != null) {
                final HashSet hashSet = new HashSet(Lists.charactersOf(value));
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
