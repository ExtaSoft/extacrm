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
import ru.extas.server.ContactRepository;
import ru.extas.server.SupplementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.commons.window.AbstractEditForm;
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
public class SalePointEditForm extends AbstractEditForm<SalePoint> {

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
    private EditField emailField;
    @PropertyId("www")
    private EditField wwwField;
    @PropertyId("actualAddress.region")
    private ComboBox regionField;
    @PropertyId("actualAddress.city")
    private ComboBox cityField;
    @PropertyId("actualAddress.postIndex")
    private EditField postIndexField;
    @PropertyId("actualAddress.streetBld")
    private TextArea streetBldField;
    @PropertyId("legalEntities")
    private LegalEntitiesSelectField legalsField;
    @PropertyId("employes")
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

    /**
     * <p>Constructor for SalePointEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param obj     a {@link com.vaadin.data.util.BeanItem} object.
     */
    public SalePointEditForm(final String caption, final BeanItem<SalePoint> obj) {
        super(caption, obj);
        salePoint = obj.getBean();
    }

    /** {@inheritDoc} */
    @Override
    public void attach() {
        super.attach();

        if (salePoint.getCompany() == null) {
            companyField.setReadOnly(false);
            companyField.setVisible(true);
            companyField.setRequired(true);
        } else {
            companyField.setReadOnly(true);
            companyField.getPropertyDataSource().setReadOnly(true);
            if (salePoint.getCompany().getId() == null) {
                companyField.setVisible(false);
                companyField.setRequired(false);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final SalePoint obj) {
        if (obj.getId() == null) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
        }
        if (obj.getActualAddress() == null)
            obj.setActualAddress(new AddressInfo());
    }


    /** {@inheritDoc} */
    @Override
    protected void saveObject(final SalePoint obj) {
        if (obj.getCompany().getId() != null) {
            logger.debug("Saving contact data...");
            final ContactRepository contactRepository = lookup(ContactRepository.class);
            contactRepository.save(obj);
            Notification.show("Юр. лицо сохранено", Notification.Type.TRAY_NOTIFICATION);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final SalePoint obj) {
    }


    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final SalePoint obj) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Вкладка - "Общая информация"
        final FormLayout mainForm = createMainForm(obj);
        tabsheet.addTab(mainForm).setCaption("Общие данные");

        // Вкладка - "Сотрудники"
        final FormLayout employesForm = createEmployesForm();
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
        final FormLayout formLayout = new FormLayout();
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

    private FormLayout createEmployesForm() {
        final FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);

        employeeField = new ContactEmployeeField();
        formLayout.addComponent(employeeField);

        return formLayout;
    }

    private FormLayout createMainForm(final SalePoint obj) {
        final FormLayout formLayout = new FormLayout();
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

        companyField = new CompanySelect("Компания", "Введите или выберете компанию которой принадлежит торговая точка");
        companyField.setRequired(true);
        formLayout.addComponent(companyField);

        phoneField = new PhoneField("Телефон");
        formLayout.addComponent(phoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        wwwField = new EditField("WWW", "Введите адрес сайта торговой точки");
        wwwField.setColumns(20);
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
        if (obj.getActualAddress().getCity() != null) cityField.addItem(obj.getActualAddress().getCity());
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
        streetBldField.setColumns(30);
        streetBldField.setRows(5);
        streetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        streetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        streetBldField.setNullRepresentation("");
        formLayout.addComponent(streetBldField);

        return formLayout;
    }

}
