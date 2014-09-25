/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.LegalEntityFile;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.motor.BrandsField;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования юридического лица
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@SuppressWarnings("FieldCanBeLocal")
public class LegalEntityEditForm extends ExtaEditForm<LegalEntity> {

    private static final long serialVersionUID = -7787385620289376599L;
    private final static Logger logger = LoggerFactory.getLogger(LegalEntityEditForm.class);

    // Компоненты редактирования

    // Вкладка - "Общая информация"
    @PropertyId("company")
    private CompanySelect companyField;
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("ogrnOgrip")
    private EditField ogrnOgripField;
    @PropertyId("inn")
    private EditField innField;
    @PropertyId("phone")
    private PhoneField phoneField;
    @PropertyId("email")
    private EditField emailField;
    @PropertyId("www")
    private EditField wwwField;
    @PropertyId("regAddress.region")
    private ComboBox regionField;
    @PropertyId("regAddress.city")
    private ComboBox cityField;
    @PropertyId("regAddress.postIndex")
    private EditField postIndexField;
    @PropertyId("regAddress.streetBld")
    private TextArea streetBldField;
    @PropertyId("director")
    private PersonSelect directorField;
    @PropertyId("accountant")
    private PersonSelect accountantField;
    @PropertyId("credProducts")
    private LegalProductsField productsField;
    @PropertyId("motorBrands")
    private BrandsField brandsField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;

    private LegalEntity legalEntity;

    @PropertyId("kpp")
    private EditField kppField;

    @PropertyId("regNpstIsSame")
    private CheckBox regNpstIsSameField;
    @PropertyId("postAddress.region")
    private RegionSelect pstRegionField;
    @PropertyId("postAddress.city")
    private CitySelect pstCityField;
    @PropertyId("postAddress.postIndex")
    private EditField pstPostIndexField;
    @PropertyId("postAddress.streetBld")
    private TextArea pstStreetBldField;

    @PropertyId("settlementAccount")
    private EditField settlementAccountField;
    @PropertyId("loroAccount")
    private EditField loroAccountField;
    @PropertyId("bankName")
    private EditField bankNameField;
    @PropertyId("biс")
    private EditField biсField;

    public LegalEntityEditForm(LegalEntity legalEntity) {
        super(legalEntity.isNew() ?
                "Ввод нового юр. лица в систему" :
                String.format("Редактирование юр. лица: %s", legalEntity.getName()));

        final BeanItem<LegalEntity> beanItem = new BeanItem<>(legalEntity);
        beanItem.expandProperty("regAddress");
        beanItem.expandProperty("actualAddress");
        beanItem.expandProperty("postAddress");

        this.legalEntity = legalEntity;
        initForm(beanItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach() {
        super.attach();

        if (legalEntity.getCompany() == null) {
            companyField.setReadOnly(false);
            companyField.setVisible(true);
            companyField.setRequired(true);
        } else {
            companyField.setReadOnly(true);
            companyField.getPropertyDataSource().setReadOnly(true);
            if (legalEntity.getCompany().isNew()) {
                companyField.setVisible(false);
                companyField.setRequired(false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initObject(final LegalEntity obj) {
        if (obj.isNew()) {
            // Инициализируем новый объект
            // TODO: Инициализировать клиента в соответствии с локацией текущего
        }
        if (obj.getRegAddress() == null)
            obj.setRegAddress(new AddressInfo());
        if (obj.getPostAddress() == null)
            obj.setPostAddress(new AddressInfo());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected LegalEntity saveObject(LegalEntity obj) {
        if (!obj.getCompany().isNew()) {
            logger.debug("Saving contact data...");
            final LegalEntityRepository contactRepository = lookup(LegalEntityRepository.class);
            obj = contactRepository.secureSave(obj);
            NotificationUtil.showSuccess("Юр. лицо сохранено");
        }
        return obj;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields(final LegalEntity obj) {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // "Общая информация"
        formLayout.addComponent(new FormGroupHeader("Общая информация"));
        nameField = new EditField("Название");
        nameField.setRequired(true);
        nameField.setImmediate(true);
        nameField.setColumns(30);
        nameField.setDescription("Введите Название юридического лица");
        nameField.setInputPrompt("OOO \"Рога и Копыта\"");
        nameField.setRequiredError("Название юридического лица не может быть пустым.");
        nameField.setNullRepresentation("");
        formLayout.addComponent(nameField);

        companyField = new CompanySelect("Компания", "Введите или выберите компанию которой принадлежит юридическое лицо");
        companyField.setRequired(true);
        formLayout.addComponent(companyField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Ответственные лица"));
        directorField = new PersonSelect("Генеральный директор", "Выберите или введите геннерального деректора юридического лица");
        formLayout.addComponent(directorField);
        accountantField = new PersonSelect("Главный бухгалтер", "Выберите или введите главного бухгалтера юридического лица");
        formLayout.addComponent(accountantField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Контакты"));
        phoneField = new PhoneField("Телефон");
        formLayout.addComponent(phoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        wwwField = new EditField("WWW", "Введите адрес сайта юридического лица");
        wwwField.setColumns(20);
        formLayout.addComponent(wwwField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Юридический адрес"));
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
        if (obj.getRegAddress().getCity() != null) cityField.addItem(obj.getRegAddress().getCity());
        cityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(SupplementService.class).findRegionByCity(newCity);
            if (region != null)
                regionField.setValue(region);
        });
        formLayout.addComponent(cityField);

        postIndexField = new EditField("Почтовый индекс");
        postIndexField.setColumns(8);
        postIndexField.setInputPrompt("Индекс");
        postIndexField.setNullRepresentation("");
        formLayout.addComponent(postIndexField);

        streetBldField = new TextArea("Адрес");
        streetBldField.setColumns(30);
        streetBldField.setRows(2);
        streetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        streetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        streetBldField.setNullRepresentation("");
        formLayout.addComponent(streetBldField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Почтовый адрес"));
        regNpstIsSameField = new CheckBox("Совпадает с юридическим адресом");
        regNpstIsSameField.addValueChangeListener(event -> {
            Boolean isRegIsAct = (Boolean) event.getProperty().getValue();
            if (isRegIsAct == null)
                isRegIsAct = false;
            setPostAdressStatus(isRegIsAct);
        });
        formLayout.addComponent(regNpstIsSameField);

        pstRegionField = new RegionSelect();
        pstRegionField.addValueChangeListener(event -> {
            final String newRegion = (String) event.getProperty().getValue();
            final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
            if (city != null)
                pstCityField.setValue(city);
        });
        formLayout.addComponent(pstRegionField);

        pstCityField = new CitySelect();
        if (obj.getRegAddress().getCity() != null)
            pstCityField.addItem(obj.getRegAddress().getCity());
        pstCityField.addValueChangeListener(event -> {
            final String newCity = (String) event.getProperty().getValue();
            final String region = lookup(SupplementService.class).findRegionByCity(newCity);
            if (region != null)
                pstRegionField.setValue(region);
        });
        formLayout.addComponent(pstCityField);

        pstPostIndexField = new EditField("Почтовый индекс");
        pstPostIndexField.setColumns(8);
        pstPostIndexField.setInputPrompt("Индекс");
        pstPostIndexField.setNullRepresentation("");
        formLayout.addComponent(pstPostIndexField);

        pstStreetBldField = new TextArea("Адрес");
        pstStreetBldField.setColumns(20);
        pstStreetBldField.setRows(2);
        pstStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
        pstStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
        pstStreetBldField.setNullRepresentation("");
        formLayout.addComponent(pstStreetBldField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Идентификация"));
        ogrnOgripField = new EditField("ОГРН/ОГРИП", "Введите ОГРН/ОГРИП код юридического лица");
        formLayout.addComponent(ogrnOgripField);

        innField = new EditField("ИНН", "Введите ИНН юридического лица");
        formLayout.addComponent(innField);

        kppField = new EditField("КПП", "Введите КПП юридического лица");
        formLayout.addComponent(kppField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Банковские реквизиты"));
        // Расчетный счет в рублях
        settlementAccountField = new EditField("Расчетный счет", "Введите Расчетный счет юридического лицав рублях");;
        formLayout.addComponent(settlementAccountField);
        // Корреспондентский счет
        loroAccountField = new EditField("Корреспондентский счет", "Введите Корреспондентский счет юридического лица");;
        formLayout.addComponent(loroAccountField);
        // Полное наименование банка
        bankNameField = new EditField("Наименование банка", "Введите наименование банка юридического лица");;
        formLayout.addComponent(bankNameField);
        // БИК банка
        biсField = new EditField("БИК банка", "Введите БИК банка юридического лица");;
        formLayout.addComponent(biсField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // "Продукты"
        formLayout.addComponent(new FormGroupHeader("Продукты"));
        productsField = new LegalProductsField();
        formLayout.addComponent(productsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Вкладка - "Бренды"
        formLayout.addComponent(new FormGroupHeader("Бренды"));
        brandsField = new BrandsField();
        formLayout.addComponent(brandsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(LegalEntityFile.class);
        formLayout.addComponent(docFilesEditor);

        return formLayout;
    }

    private void setPostAdressStatus(Boolean isRegIsAct) {
        pstRegionField.setVisible(!isRegIsAct);
        pstCityField.setVisible(!isRegIsAct);
        pstPostIndexField.setVisible(!isRegIsAct);
        pstStreetBldField.setVisible(!isRegIsAct);
    }

}
