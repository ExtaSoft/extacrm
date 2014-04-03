/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person.Sex;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;
import ru.extas.web.util.ComponentUtil;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>PersonEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@SuppressWarnings("FieldCanBeLocal")
public class PersonEditForm extends AbstractEditForm<Person> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final static Logger logger = LoggerFactory.getLogger(PersonEditForm.class);
    // Компоненты редактирования
// Основные персональные данные
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("birthday")
    private PopupDateField birthdayField;
    @PropertyId("sex")
    private ComboBox sexField;
    @PropertyId("phone")
    private EditField cellPhoneField;
    @PropertyId("workPhone")
    private EditField workPhoneField;
    @PropertyId("homePhone")
    private EditField homePhoneField;
    @PropertyId("email")
    private EmailField emailField;
    @PropertyId("actualAddress.region")
    private RegionSelect regionField;
    @PropertyId("actualAddress.city")
    private CitySelect cityField;
    @PropertyId("actualAddress.postIndex")
    private EditField postIndexField;
    @PropertyId("actualAddress.streetBld")
    private TextArea streetBldField;

    // Паспортнве данные
    @PropertyId("passNum")
    private EditField passNumField;
    @PropertyId("passIssueDate")
    private LocalDateField passIssueDateField;
    @PropertyId("passIssuedBy")
    private TextArea passIssuedByField;
    @PropertyId("passIssuedByNum")
    private EditField passIssuedByNumField;
    @PropertyId("passRegAdress")
    private TextArea passRegAdressField;


    /**
     * <p>Constructor for PersonEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param obj     a {@link com.vaadin.data.util.BeanItem} object.
     */
    public PersonEditForm(final String caption, final BeanItem<Person> obj) {
        super(caption, obj);
    }


    /** {@inheritDoc} */
    @Override
    protected void initObject(final Person obj) {
        if (obj.getActualAddress() == null)
            obj.setActualAddress(new AddressInfo());
        if (obj.getId() == null) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
            // пользователя (регион, город)
            obj.setSex(Person.Sex.MALE);
            obj.setJobPosition(Person.Position.EMPLOYEE);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void saveObject(final Person obj) {
        logger.debug("Saving contact data...");
        final PersonRepository contactRepository = lookup(PersonRepository.class);
        contactRepository.secureSave(obj);
        Notification.show("Контакт сохранен", Notification.Type.TRAY_NOTIFICATION);
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
        final FormLayout personForm = createMainForm(obj);
        tabsheet.addTab(personForm).setCaption("Общие данные");

        // Форма редактирования паспортных данных
        final FormLayout passForm = createPassForm();
        tabsheet.addTab(passForm).setCaption("Паспортные данные");

        // Форма просмотра истории продаж
        final FormLayout salesForm = createSalesForm();
        tabsheet.addTab(salesForm).setCaption("История продаж");

        return tabsheet;
    }

    private FormLayout createSalesForm() {
        final FormLayout form = new FormLayout();
        form.setMargin(true);

        return form;
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
        passIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        passIssuedByField.setNullRepresentation("");
        passIssuedByField.setRows(3);
        passIssuedByField.setColumns(30);
        passForm.addComponent(passIssuedByField);

        passIssuedByNumField = new EditField("Код подразделения");
        passForm.addComponent(passIssuedByNumField);

        passRegAdressField = new TextArea("Адрес регистрации");
        passRegAdressField.setDescription("Введите адрес регистрации (прописки) контакта");
        passRegAdressField.setInputPrompt("Регион, Город, Улица, Дом...");
        passRegAdressField.setNullRepresentation("");
        passRegAdressField.setRows(3);
        passRegAdressField.setColumns(30);
        passForm.addComponent(passRegAdressField);

        return passForm;
    }

    private FormLayout createMainForm(final Person obj) {
        final FormLayout personForm = new FormLayout();
        personForm.setMargin(true);

        nameField = new EditField("Имя");
        nameField.setColumns(30);
        nameField.setDescription("Введите имя (ФИО) контакта");
        nameField.setInputPrompt("Фамилия Имя (Отчество)");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя контакта не может быть пустым. Пожалуйста введите ФИО контакта.");
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

        cellPhoneField = new PhoneField("Мобильный телефон");
        personForm.addComponent(cellPhoneField);

        workPhoneField = new PhoneField("Рабочий телефон");
        personForm.addComponent(workPhoneField);

        homePhoneField = new PhoneField("Домашний телефон");
        personForm.addComponent(homePhoneField);

        emailField = new EmailField("E-Mail");
        personForm.addComponent(emailField);

        regionField = new RegionSelect();
        regionField.setDescription("Укажите регион проживания");
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

        cityField = new CitySelect();
        cityField.setDescription("Введите город проживания контакта");
        if (obj.getActualAddress().getCity() != null)
            cityField.addItem(obj.getActualAddress().getCity());
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
