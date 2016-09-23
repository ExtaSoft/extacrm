/**
 *
 */
package ru.extas.web.contacts.company;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.Company;
import ru.extas.server.common.AddressAccessService;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
import ru.extas.web.contacts.employee.EmployeeFieldMulty;
import ru.extas.web.contacts.legalentity.LegalEntitiesField;
import ru.extas.web.contacts.salepoint.SalePointsField;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>CompanyEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@SuppressWarnings("FieldCanBeLocal")
public class CompanyEditForm extends ExtaEditForm<Company> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final static Logger logger = LoggerFactory.getLogger(CompanyEditForm.class);

    // Компоненты редактирования

    // Вкладка - "Общая информация"
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("categories")
    private CompanyCategoriesField categoriesField;
    @PropertyId("phone")
    private PhoneField phoneField;
    @PropertyId("email")
    private EmailField emailField;
    @PropertyId("www")
    private WebSiteLinkField wwwField;
    @PropertyId("facebook")
    private WebSiteLinkField facebookField;
    @PropertyId("bk")
    private WebSiteLinkField bkField;
    @PropertyId("instagram")
    private WebSiteLinkField instagramField;
    @PropertyId("youtube")
    private WebSiteLinkField youtubeField;
    @PropertyId("region")
    private ComboBox regionField;
    @PropertyId("city")
    private ComboBox cityField;

    // Вкладка - "Владельцы"
    @PropertyId("owners")
    private CompanyOwnersField ownersField;

    // Вкладка - "Юр. лица"
    @PropertyId("legalEntities")
    private LegalEntitiesField legalsField;

    // Вкладка - "Торговые точки"
    @PropertyId("salePoints")
    private SalePointsField salePointsField;

    // Вкладка - "Сотрудники"
    @PropertyId("employees")
    private EmployeeFieldMulty employeeField;


    public CompanyEditForm(final Company company) {
        super(company.isNew() ?
                "Ввод новой компании в систему" :
                String.format("Редактирование компании: %s", company.getName()), company);

        setWinWidth(800, Unit.PIXELS);
        setWinHeight(600, Unit.PIXELS);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final Company company) {
        if (company.isNew()) {
            // Инициализируем новый объект
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Company saveEntity(final Company company) {
        logger.debug("Saving contact data...");
        final CompanyRepository contactRepository = lookup(CompanyRepository.class);
        contactRepository.secureSave(company);
        NotificationUtil.showSuccess("Компания сохранена");
        return company;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        final ConfirmTabSheet tabsheet = new ConfirmTabSheet(
                () -> !getEntity().isNew(),
                () -> save());
        tabsheet.setSizeFull();

        // Вкладка - "Общая информация"
        tabsheet.addTab(createMainForm(), "Общие данные");
        // Вкладка - "Торговые точки"
        tabsheet.addConfirmTab(createSalePointsForm(), "Торговые точки");
        // Вкладка - "Сотрудники"
        tabsheet.addConfirmTab(createEmployeesForm(), "Сотрудники");
        // Вкладка - "Юр. лица"
        tabsheet.addConfirmTab(createLegalsForm(), "Юридические лица");
        // Вкладка - "Владельцы"
        tabsheet.addConfirmTab(createOwnerForm(), "Владельцы");
        return tabsheet;
    }

    private Component createLegalsForm() {
        legalsField = new LegalEntitiesField();
        legalsField.setCompanySupplier(super::getEntity);
        legalsField.setSizeFull();
        return legalsField;
    }

    private Component createEmployeesForm() {
        employeeField = new EmployeeFieldMulty();
        employeeField.setCompanySupplier(super::getEntity);
        employeeField.setSizeFull();
        return employeeField;
    }

    private Component createSalePointsForm() {
        salePointsField = new SalePointsField();
        salePointsField.setCompanySupplier(super::getEntity);
        salePointsField.setSizeFull();
        return salePointsField;
    }

    private Component createOwnerForm() {
        ownersField = new CompanyOwnersField();
        ownersField.setCompanySupplier(super::getEntity);
        ownersField.setSizeFull();
        return ownersField;
    }

    private FormLayout createMainForm() {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);
        formLayout.setSizeFull();

        nameField = new EditField("Название");
        nameField.setRequired(true);
        nameField.setImmediate(true);
        nameField.setColumns(20);
        nameField.setDescription("Введите Название компании");
        nameField.setInputPrompt("Рога и Копыта");
        nameField.setRequiredError("Название компании не может быть пустым.");
        nameField.setNullRepresentation("");
        formLayout.addComponent(nameField);

        categoriesField = new CompanyCategoriesField("Категория");
        formLayout.addComponent(categoriesField);

        regionField = new RegionSelect();
        regionField.setDescription("Укажите регион регистрации");
        regionField.addValueChangeListener(event -> {
            final String newRegion = (String) event.getProperty().getValue();
            final String city = lookup(AddressAccessService.class).findCityByRegion(newRegion);
            if (city != null)
                cityField.setValue(city);
        });
        formLayout.addComponent(regionField);

        cityField = new CitySelect();
        cityField.setDescription("Введите город регистрации");
        if (getEntity().getCity() != null) cityField.addItem(getEntity().getCity());
        cityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(AddressAccessService.class).findRegionByCity(newCity);
            if (region != null)
                regionField.setValue(region);
        });
        formLayout.addComponent(cityField);

        phoneField = new PhoneField("Телефон");
        formLayout.addComponent(phoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        wwwField = new WebSiteLinkField("WWW", "Введите адрес сайта компании");
        formLayout.addComponent(wwwField);

        facebookField = new WebSiteLinkField("Facebook", "Введите ссылку на группу компании в Facebook");
        formLayout.addComponent(facebookField);

        bkField = new WebSiteLinkField("ВКонтакте", "Введите ссылку на группу компании ВКонтакте");
        formLayout.addComponent(bkField);

        instagramField = new WebSiteLinkField("Instagram", "Введите ссылку на страницу компании в Instagram");
        formLayout.addComponent(instagramField);

        youtubeField = new WebSiteLinkField("YouTube", "Введите ссылку на канал компании в YouTube");
        formLayout.addComponent(youtubeField);

        return formLayout;
    }
}
