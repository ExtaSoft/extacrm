/**
 *
 */
package ru.extas.web.contacts.company;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.Company;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
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
    @PropertyId("www")
    private WebSiteLinkField wwwField;
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
    private CompanyEmployeesField employeeField;


    public CompanyEditForm(final Company company) {
        super(company.isNew() ?
                "Ввод новой компании в систему" :
                String.format("Редактирование компании: %s", company.getName()));
        final BeanItem<Company> beanItem = new BeanItem<>(company);

        setWinWidth(800, Unit.PIXELS);
        setWinHeight(600, Unit.PIXELS);

        initForm(beanItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initObject(final Company obj) {
        if (obj.isNew()) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Company saveObject(final Company obj) {
        logger.debug("Saving contact data...");
        final CompanyRepository contactRepository = lookup(CompanyRepository.class);
        contactRepository.secureSave(obj);
        NotificationUtil.showSuccess("Компания сохранена");
        return obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields(final Company obj) {
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

//        tabsheet.addSelectedTabChangeListener(e -> {
//            // Сохранить объект перед тем как переключаться на сложную вкладку
//            final Component tab = tabsheet.getSelectedTab();
//            if (getObject().isNew() && tabsheet.getTabPosition(tabsheet.getTab(tab)) != 0) {
//                tabsheet.setSelectedTab(tabsheet.getTab(0));
////                ConfirmDialog.show(UI.getCurrent(),
////                        "Необходимо сохранить объект...",
////                        "Необходимо сохранить компанию прежде чем продолжить редактирование. Сохранить сейчас?",
////                        "Сохранить", "Отменить",
////                        dialog -> {
////                            if (dialog.isConfirmed()) {
////                                save();
////                                tabsheet.setSelectedTab(tab);
////                            }
////                        });
//            }
//        });
//
        // Вкладка - "Общая информация"
        tabsheet.addTab(createMainForm(obj), "Общие данные");
        // Вкладка - "Торговые точки"
        tabsheet.addTab(createSalePointsForm(), "Торговые точки");
        // Вкладка - "Сотрудники"
        tabsheet.addTab(createEmployesForm(), "Сотрудники");
        // Вкладка - "Юр. лица"
        tabsheet.addTab(createLegalsForm(), "Юридические лица");
        // Вкладка - "Владельцы"
        tabsheet.addTab(createOwnerForm(), "Владельцы");
        return tabsheet;
    }

    private Component createLegalsForm() {
        final Company obj = getObject();
        legalsField = new LegalEntitiesField(obj);
        legalsField.setSizeFull();
        return legalsField;
    }

    private Component createEmployesForm() {
        final Company obj = getObject();
        employeeField = new CompanyEmployeesField(obj);
        employeeField.setSizeFull();
        return employeeField;
    }

    private Component createSalePointsForm() {
        final Company obj = getObject();
        salePointsField = new SalePointsField(obj);
        salePointsField.setSizeFull();
        return salePointsField;
    }

    private Component createOwnerForm() {
        final Company obj = getObject();
        ownersField = new CompanyOwnersField(obj);
        ownersField.setSizeFull();
        return ownersField;
    }

    private FormLayout createMainForm(final Company obj) {
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

        wwwField = new WebSiteLinkField("WWW", "Введите адрес сайта компании");
        formLayout.addComponent(wwwField);

        regionField = new RegionSelect();
        regionField.setDescription("Укажите регион регистрации");
        regionField.addValueChangeListener(event -> {
            final String newRegion = (String) event.getProperty().getValue();
            final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
            if (city != null)
                cityField.setValue(city);
        });
        formLayout.addComponent(regionField);

        cityField = new CitySelect();
        cityField.setDescription("Введите город регистрации");
        if (obj.getCity() != null) cityField.addItem(obj.getCity());
        cityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(SupplementService.class).findRegionByCity(newCity);
            if (region != null)
                regionField.setValue(region);
        });
        formLayout.addComponent(cityField);

        return formLayout;
    }
}
