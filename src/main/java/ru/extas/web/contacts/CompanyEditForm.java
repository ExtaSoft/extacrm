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
import ru.extas.model.AddressInfo;
import ru.extas.model.Company;
import ru.extas.model.Contact;
import ru.extas.server.ContactService;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
@SuppressWarnings("FieldCanBeLocal")
public class CompanyEditForm extends AbstractEditForm<Company> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final Logger logger = LoggerFactory.getLogger(CompanyEditForm.class);
    // Компоненты редактирования
    // Основные персональные данные
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("fullName")
    private EditField fullNameField;
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
    // Орг. структура
    @PropertyId("affiliation")
    private AbstractContactSelect affiliationField;

    // Реквизиты юр. лица


    /**
     * @param caption
     * @param obj
     */
    public CompanyEditForm(final String caption, final BeanItem<Company> obj) {
        super(caption, obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void initObject(final Company obj) {
        if (obj.getKey() == null) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
        }
        if (obj.getActualAddress() == null)
            obj.setActualAddress(new AddressInfo());
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Company obj) {
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
    protected void checkBeforeSave(final Company obj) {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Company obj) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Форма редактирования персональных данных
        final FormLayout mainForm = createMainForm(obj);
        tabsheet.addTab(mainForm).setCaption("Общие данные");

        // Форма редактирования данных о компании
        final FormLayout companyForm = createCompanyForm();
        tabsheet.addTab(companyForm).setCaption("Организационная структура");

        // Форма редактирования паспортных данных
        final FormLayout refForm = createRefForm();
        tabsheet.addTab(refForm).setCaption("Реквизиты");

        return tabsheet;
    }

    private FormLayout createRefForm() {
        final FormLayout refForm = new FormLayout();
        refForm.setMargin(true);

        refForm.addComponent(new Label("ИНН"));
        refForm.addComponent(new Label("ОГРН"));
        refForm.addComponent(new Label("..."));
        refForm.addComponent(new Label("Расчетные счета"));

        return refForm;
    }

    private FormLayout createCompanyForm() {
        final FormLayout companyForm = new FormLayout();
        companyForm.setMargin(true);

        affiliationField = new CompanySelect("Вышестоящая организация");
        affiliationField.setDescription("Компания в которой работает контакт");
        companyForm.addComponent(affiliationField);

        return companyForm;
    }

    private FormLayout createMainForm(final Contact obj) {
        final FormLayout personForm = new FormLayout();
        personForm.setMargin(true);

        nameField = new EditField("Название");
        nameField.setRequired(true);
        nameField.setImmediate(true);
        nameField.setColumns(30);
        nameField.setDescription("Введите Название компании");
        nameField.setInputPrompt("Рога и Копыта");
        nameField.setRequiredError("Название компании не может быть пустым.");
        nameField.setNullRepresentation("");
        personForm.addComponent(nameField);

        fullNameField = new EditField("Официальное название");
        fullNameField.setRequired(true);
        fullNameField.setImmediate(true);
        fullNameField.setColumns(30);
        fullNameField.setDescription("Официальное название компании");
        fullNameField.setInputPrompt("ООО \"Рога и Копыта\"");
        fullNameField.setRequiredError("Официальное Название компании не может быть пустым.");
        personForm.addComponent(fullNameField);

        cellPhoneField = new EditField("Телефон");
        cellPhoneField.setImmediate(true);
        cellPhoneField.setColumns(20);
        cellPhoneField.setDescription("Введите телефон в формате +7 XXX XXX XXXX");
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
