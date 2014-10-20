package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>SalePointEditForm class.</p>
 *
 * @author Valery Orlov
 *         Date: 19.02.14
 *         Time: 13:08
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointEditForm extends ExtaEditForm<SalePoint> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final static Logger logger = LoggerFactory.getLogger(LegalEntityEditForm.class);

    // Компоненты редактирования

    // Вкладка - "Общая информация"
    @PropertyId("company")
    private CompanySelect companyField;
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("phone")
    private PhoneField phoneField;
    @PropertyId("email")
    private EmailField emailField;
    @PropertyId("www")
    private WebSiteLinkField wwwField;
    @PropertyId("regAddress.region")
    private ComboBox regionField;
    @PropertyId("regAddress.city")
    private ComboBox cityField;
    @PropertyId("regAddress.postIndex")
    private EditField postIndexField;
    @PropertyId("regAddress.streetBld")
    private TextArea streetBldField;
    @PropertyId("legalEntities")
    private LegalEntitiesSelectField legalsField;
    @PropertyId("employees")
    private ContactEmployeeField employeeField;

    @PropertyId("alphaCode")
    private EditField alphaCodeField;
    @PropertyId("homeCode")
    private EditField homeCodeField;
    @PropertyId("setelemCode")
    private EditField setelemCodeField;
    @PropertyId("extaCode")
    private EditField extaCodeField;

    private SalePoint salePoint;

    public SalePointEditForm(final SalePoint salePoint) {
        super(salePoint.isNew() ? "Ввод новой торговой точки в систему" : "Редактирование данных торговой точки");
        final BeanItem beanItem = new BeanItem<>(salePoint);
        beanItem.expandProperty("regAddress");

        initForm(beanItem);
    }

    /** {@inheritDoc} */
    @Override
    public void attach() {
        super.attach();

        if (salePoint != null) {
            if (salePoint.getCompany() == null) {
                companyField.setReadOnly(false);
                companyField.setVisible(true);
                companyField.setRequired(true);
            } else {
                companyField.setReadOnly(true);
                companyField.getPropertyDataSource().setReadOnly(true);
                if (salePoint.getCompany().isNew()) {
                    companyField.setVisible(false);
                    companyField.setRequired(false);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final SalePoint obj) {
        if (obj.isNew()) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
        }
        if (obj.getRegAddress() == null)
            obj.setRegAddress(new AddressInfo());
    }


    /** {@inheritDoc} */
    @Override
    protected SalePoint saveObject(SalePoint obj) {
        if (!obj.getCompany().isNew()) {
            logger.debug("Saving contact data...");
            final SalePointRepository contactRepository = lookup(SalePointRepository.class);
            obj = contactRepository.secureSave(obj);
            NotificationUtil.showSuccess("Торговая точка сохранена");
        }
        return obj;
    }


    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final SalePoint obj) {
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Вкладка - "Общая информация"
        final FormLayout mainForm = createMainForm(obj);
        tabsheet.addTab(mainForm).setCaption("Общие данные");

        // Вкладка - "Сотрудники"
        final Component employesForm = createEmployesForm();
        tabsheet.addTab(employesForm).setCaption("Сотрудники");

        // Вкладка - "Юр.лица"
        final Component legalsForm = createLegalsForm(obj.getCompany());
        tabsheet.addTab(legalsForm).setCaption("Юридические лица");

        // Вкладка - "Идентификация"
        final Component identityForm = createIdentityForm();
        tabsheet.addTab(identityForm).setCaption("Идентификация");

        return tabsheet;
    }

    private Component createIdentityForm() {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);

        extaCodeField = new EditField("Код Экстрим Ассистанс", "Введите идентификационный Код Экстрим Ассистанс");
        extaCodeField.setColumns(20);
        formLayout.addComponent(extaCodeField);

        alphaCodeField = new EditField("Код Альфа Банка", "Введите идентификационный Код Альфа Банка");
        alphaCodeField.setColumns(20);
        formLayout.addComponent(alphaCodeField);

        homeCodeField = new EditField("Код HomeCredit Банка", "Введите идентификационный Код HomeCredit Банка");
        homeCodeField.setColumns(20);
        formLayout.addComponent(homeCodeField);

        setelemCodeField = new EditField("Код Банка СЕТЕЛЕМ", "Введите идентификационный Код Банка СЕТЕЛЕМ");
        setelemCodeField.setColumns(20);
        formLayout.addComponent(setelemCodeField);

        return formLayout;
    }

    private Component createLegalsForm(final Company obj) {
        legalsField = new LegalEntitiesSelectField(obj);

        return legalsField;
    }

    private Component createEmployesForm() {
        return employeeField = new ContactEmployeeField();
    }

    private FormLayout createMainForm(final SalePoint obj) {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);

        nameField = new EditField("Название");
        nameField.setRequired(true);
        nameField.setImmediate(true);
        nameField.setColumns(30);
        nameField.setDescription("Введите Название торговой точки");
        nameField.setInputPrompt("Рога и Копыта - Север");
        nameField.setRequiredError("Название торговой точки не может быть пустым.");
        nameField.setNullRepresentation("");
        formLayout.addComponent(nameField);

        companyField = new CompanySelect("Компания", "Введите или выберите компанию которой принадлежит торговая точка");
        companyField.setRequired(true);
        formLayout.addComponent(companyField);

        phoneField = new PhoneField("Телефон");
        formLayout.addComponent(phoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        wwwField = new WebSiteLinkField("WWW", "Введите адрес сайта торговой точки");
        formLayout.addComponent(wwwField);

        regionField = new RegionSelect();
        regionField.setDescription("Укажите регион");
        regionField.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final String newRegion = (String) event.getProperty().getValue();
                final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
                if (city != null)
                    cityField.setValue(city);
            }
        });
        formLayout.addComponent(regionField);

        cityField = new CitySelect();
        cityField.setDescription("Введите город регистрации");
        if (obj.getRegAddress().getCity() != null) cityField.addItem(obj.getRegAddress().getCity());
        cityField.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final String newCity = (String) event.getProperty().getValue();
                final String region = lookup(SupplementService.class).findRegionByCity(newCity);
                if (region != null)
                    regionField.setValue(region);
            }
        });
        formLayout.addComponent(cityField);

        postIndexField = new EditField("Почтовый индекс");
        postIndexField.setColumns(8);
        postIndexField.setInputPrompt("Индекс");
        postIndexField.setNullRepresentation("");
        formLayout.addComponent(postIndexField);

        streetBldField = new TextArea("Адрес");
        streetBldField.setRows(3);
        streetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        streetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        streetBldField.setNullRepresentation("");
        formLayout.addComponent(streetBldField);

        return formLayout;
    }

}
