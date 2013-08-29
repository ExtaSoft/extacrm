/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Contact;
import ru.extas.model.PersonInfo;
import ru.extas.model.PersonInfo.Sex;
import ru.extas.server.ContactService;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.util.ComponentUtil;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
@SuppressWarnings("FieldCanBeLocal")
public class PersonEditForm extends AbstractEditForm<Contact> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final Logger logger = LoggerFactory.getLogger(PersonEditForm.class);

    // Компоненты редактирования
    // Основные персональные данные
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("personInfo.birthday")
    private PopupDateField birthdayField;
    @PropertyId("personInfo.sex")
    private ComboBox sexField;
    @PropertyId("cellPhone")
    private EditField cellPhoneField;
    @PropertyId("email")
    private EditField emailField;
    @PropertyId("actualAddress.region")
    private ComboBox regionField;
    @PropertyId("actualAddress.city")
    private ComboBox cityField;
    @PropertyId("actualAddress.postIndex")
    private EditField postIndexField;
    @PropertyId("actualAddress.streetBld")
    private TextArea streetBldField;

    // Компания
    @PropertyId("affiliation")
    private ContactSelect jobField;
    @PropertyId("personInfo.jobPosition")
    private ComboBox jobPositionField;
    @PropertyId("personInfo.jobDepartment")
    private EditField jobDepartmentField;

    // Паспортнве данные
    @PropertyId("personInfo.passNum")
    private EditField passNumField;
    @PropertyId("personInfo.passIssueDate")
    private LocalDateField passIssueDateField;
    @PropertyId("personInfo.passIssuedBy")
    private TextArea passIssuedByField;
    @PropertyId("personInfo.passIssuedByNum")
    private EditField passIssuedByNumField;


    /**
     * @param caption
     * @param obj
     */
    public PersonEditForm(final String caption, final BeanItem<Contact> obj) {
        super(caption, obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void initObject(final Contact obj) {
        if (obj.getKey() == null) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
            // пользователя (регион, город)
            obj.setType(Contact.Type.PERSON);
            obj.getPersonInfo().setSex(PersonInfo.Sex.MALE);
            obj.getPersonInfo().setJobPosition(PersonInfo.Position.EMPLOYEE);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Contact obj) {
        logger.debug("Saving contact data...");
        final ContactService contactService = lookup(ContactService.class);
        contactService.persistContact(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void checkBeforeSave(final Contact obj) {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Contact obj) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Форма редактирования персональных данных
        final FormLayout personForm = createMainForm(obj);
        tabsheet.addTab(personForm).setCaption("Общие данные");

        // Форма редактирования данных о компании
        final FormLayout companyForm = createCompanyForm();
        tabsheet.addTab(companyForm).setCaption("Компания");

        // Форма редактирования паспортных данных
        final FormLayout passForm = createPassForm();
        tabsheet.addTab(passForm).setCaption("Паспортные данные");

        return tabsheet;
    }

    private FormLayout createPassForm() {
        final FormLayout passForm = new FormLayout();
        passForm.setMargin(true);

        passNumField = new EditField("Номер паспорта");
        passNumField.setColumns(20);
        passForm.addComponent(passNumField);

        passIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        passForm.addComponent(passIssueDateField);

        passIssuedByField = new TextArea("Кем выдан");
        passIssuedByField.setDescription("Наименование органа выдавшего документ");
        passIssuedByField.setRows(3);
        passIssuedByField.setColumns(30);
        passForm.addComponent(passIssuedByField);

        passIssuedByNumField = new EditField("Код подразделения");
        passForm.addComponent(passIssuedByNumField);
        return passForm;
    }

    private FormLayout createCompanyForm() {
        final FormLayout companyForm = new FormLayout();
        companyForm.setMargin(true);

        jobField = new ContactSelect("Компания", Contact.Type.COMPANY);
        jobField.setDescription("Компания в которой работает контакт");
        companyForm.addComponent(jobField);

        jobPositionField = new ComboBox("Должность");
        jobPositionField.setWidth(15, Unit.EM);
        jobPositionField.setDescription("Укажите должность контакта");
        jobPositionField.setNullSelectionAllowed(false);
        jobPositionField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(jobPositionField, PersonInfo.Position.class);
        companyForm.addComponent(jobPositionField);

        jobDepartmentField = new EditField("Департамент");
        jobDepartmentField.setDescription("Подразделение в котором работает контакт");
        jobDepartmentField.setColumns(20);
        companyForm.addComponent(jobDepartmentField);
        return companyForm;
    }

    private FormLayout createMainForm(final Contact obj) {
        final FormLayout personForm = new FormLayout();
        personForm.setMargin(true);

        nameField = new EditField("Имя");
        nameField.setImmediate(true);
        nameField.setColumns(30);
        nameField.setDescription("Введите имя (ФИО) контакта");
        nameField.setInputPrompt("Имя (Отчество) Фамилия");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя контакта не может быть пустым. Пожалуйста введите ФИО контакта.");
        nameField.setNullRepresentation("");
        personForm.addComponent(nameField);

        sexField = new ComboBox("Пол");
        sexField.setDescription("Укажите пол контакта");
        sexField.setRequired(true);
        sexField.setNullSelectionAllowed(false);
        sexField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(sexField, Sex.class);
        personForm.addComponent(sexField);

        birthdayField = new PopupDateField("Дата рождения");
        birthdayField.setImmediate(true);
        birthdayField.setInputPrompt("31.12.1978");
        birthdayField.setDescription("Введите дату рождения контакта");
        birthdayField.setDateFormat("dd.MM.yyyy");
        birthdayField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
        personForm.addComponent(birthdayField);

        cellPhoneField = new EditField("Мобильный телефон");
        cellPhoneField.setImmediate(true);
        cellPhoneField.setColumns(20);
        cellPhoneField.setDescription("Введите мобильный телефон в формате +7 XXX XXX XXXX");
        cellPhoneField.setInputPrompt("+7 XXX XXX XXXX");
        cellPhoneField.setNullRepresentation("");
        // TODO: Добавить проверку правильности ввода телефона
        personForm.addComponent(cellPhoneField);

        emailField = new EditField("E-Mail");
        emailField.setImmediate(true);
        emailField.setColumns(20);
        emailField.setDescription("Введите имя e-mail контакта который будет использоваться для связи");
        emailField.setInputPrompt("e-mail");
        emailField.setNullRepresentation("");
        emailField.addValidator(new EmailValidator("{0} не является допустимым адресом электронной почты."));
        personForm.addComponent(emailField);

        regionField = new ComboBox("Регион");
        regionField.setDescription("Укажите регион проживания");
        regionField.setInputPrompt("Выберите или начните ввод...");
        regionField.setImmediate(true);
        regionField.setNullSelectionAllowed(false);
        regionField.setNewItemsAllowed(false);
        regionField.setFilteringMode(FilteringMode.CONTAINS);
        regionField.setWidth(18, Unit.EM);
        for (final String item : lookup(SupplementService.class).loadRegions())
            regionField.addItem(item);
        regionField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newRegion = (String) event.getProperty().getValue();
                final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
                if (city != null)
                    cityField.setValue(city);
            }
        });
        personForm.addComponent(regionField);

        cityField = new ComboBox("Город");
        cityField.setDescription("Введите город проживания контакта");
        cityField.setInputPrompt("Город");
        cityField.setImmediate(true);
        cityField.setNewItemsAllowed(true);
        cityField.setNullSelectionAllowed(false);
        cityField.setFilteringMode(FilteringMode.CONTAINS);
        for (final String item : lookup(SupplementService.class).loadCities())
            cityField.addItem(item);
        if (obj.getActualAddress().getCity() != null) cityField.addItem(obj.getActualAddress().getCity());
        cityField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final String newCity = (String) event.getProperty().getValue();
                final String region = lookup(SupplementService.class).findRegionByCity(newCity);
                if (region != null)
                    regionField.setValue(region);
            }
        });
        personForm.addComponent(cityField);

        postIndexField = new EditField("Почтовый индекс");
        postIndexField.setColumns(8);
        postIndexField.setInputPrompt("Индекс");
        postIndexField.setNullRepresentation("");
        personForm.addComponent(postIndexField);

        streetBldField = new TextArea("Адрес");
        streetBldField.setColumns(30);
        streetBldField.setRows(5);
        streetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        streetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        streetBldField.setNullRepresentation("");
        personForm.addComponent(streetBldField);
        return personForm;
    }
}
