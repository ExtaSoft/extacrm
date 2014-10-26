/**
 *
 */
package ru.extas.web.contacts.legalentity;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.*;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
import ru.extas.web.contacts.AddressInfoField;
import ru.extas.web.contacts.company.CompanyField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.motor.BrandsField;

import java.util.Optional;

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
    private CompanyField companyField;
    @PropertyId("name")
    private EditField nameField;
    @PropertyId("ogrnOgrip")
    private EditField ogrnOgripField;
    @PropertyId("inn")
    private EditField innField;
    @PropertyId("phone")
    private PhoneField phoneField;
    @PropertyId("email")
    private EmailField emailField;
    @PropertyId("www")
    private WebSiteLinkField wwwField;
    @PropertyId("regAddress")
    private AddressInfoField regAddressField;
    @PropertyId("director")
    private EmployeeField directorField;
    @PropertyId("accountant")
    private EmployeeField accountantField;
    @PropertyId("credProducts")
    private LegalProductsField productsField;
    @PropertyId("motorBrands")
    private BrandsField brandsField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;

    @PropertyId("kpp")
    private EditField kppField;

    @PropertyId("regNpstIsSame")
    private CheckBox regNpstIsSameField;
    @PropertyId("postAddress")
    private AddressInfoField postAddressField;

    @PropertyId("settlementAccount")
    private EditField settlementAccountField;
    @PropertyId("loroAccount")
    private EditField loroAccountField;
    @PropertyId("bankName")
    private EditField bankNameField;
    @PropertyId("biс")
    private EditField biсField;

    private SupplierSer<Company> companySupplier;

    public LegalEntityEditForm(final LegalEntity legalEntity) {
        super(legalEntity.isNew() ?
                "Ввод нового юр. лица в систему" :
                String.format("Редактирование юр. лица: %s", legalEntity.getName()), legalEntity);

        setWinWidth(860, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach() {
        super.attach();

        if (getObject().getCompany() == null) {
            companyField.setReadOnly(false);
            companyField.setVisible(true);
            companyField.setRequired(true);
        } else {
            companyField.setReadOnly(true);
            companyField.getPropertyDataSource().setReadOnly(true);
            if (getObject().getCompany().isNew()) {
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
            if (companySupplier != null)
                obj.setCompany(companySupplier.get());
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

        companyField = new CompanyField("Компания", "Введите или выберите компанию которой принадлежит юридическое лицо");
        companyField.setRequired(true);
        companyField.addValueChangeListener(e -> {
            final Company cmp = (Company) companyField.getConvertedValue();
            directorField.changeCompany();
            accountantField.changeCompany();
        });
        formLayout.addComponent(companyField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Ответственные лица"));
        directorField = new EmployeeField("Генеральный директор",
                "Выберите или введите геннерального деректора юридического лица");
        directorField.setCompanySupplier(Optional.ofNullable(companySupplier).orElse(() -> companyField.getValue()));
        directorField.setLegalEntitySupplier(super::getObject);
        directorField.addValueChangeListener(e -> {
            Employee emp = directorField.getValue();
            Company cmp = companyField.getValue();
            if (cmp == null)
                companyField.setValue(emp.getCompany());
        });
        formLayout.addComponent(directorField);
        accountantField = new EmployeeField("Главный бухгалтер",
                "Выберите или введите главного бухгалтера юридического лица");
        accountantField.setCompanySupplier(Optional.ofNullable(companySupplier).orElse(() -> companyField.getValue()));
        accountantField.setLegalEntitySupplier(super::getObject);
        accountantField.addValueChangeListener(e -> {
            Employee emp = accountantField.getValue();
            Company cmp = companyField.getValue();
            if (cmp == null)
                companyField.setValue(emp.getCompany());
        });
        formLayout.addComponent(accountantField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Контакты"));
        phoneField = new PhoneField("Телефон");
        formLayout.addComponent(phoneField);

        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        wwwField = new WebSiteLinkField("WWW", "Введите адрес сайта юридического лица");
        formLayout.addComponent(wwwField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Юридический адрес"));
        regAddressField = new AddressInfoField();
        formLayout.addComponent(regAddressField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Почтовый адрес"));
        regNpstIsSameField = new CheckBox("Совпадает с юридическим адресом");
        regNpstIsSameField.addValueChangeListener(event -> {
            Boolean isRegIsAct = (Boolean) event.getProperty().getValue();
            if (isRegIsAct == null)
                isRegIsAct = false;
            setPostAddressStatus(isRegIsAct);
        });
        formLayout.addComponent(regNpstIsSameField);

        postAddressField = new AddressInfoField();
        formLayout.addComponent(postAddressField);

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
        settlementAccountField = new EditField("Расчетный счет", "Введите Расчетный счет юридического лицав рублях");
        ;
        formLayout.addComponent(settlementAccountField);
        // Корреспондентский счет
        loroAccountField = new EditField("Корреспондентский счет", "Введите Корреспондентский счет юридического лица");
        ;
        formLayout.addComponent(loroAccountField);
        // Полное наименование банка
        bankNameField = new EditField("Наименование банка", "Введите наименование банка юридического лица");
        ;
        formLayout.addComponent(bankNameField);
        // БИК банка
        biсField = new EditField("БИК банка", "Введите БИК банка юридического лица");
        ;
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

    private void setPostAddressStatus(final Boolean isRegIsAct) {
        postAddressField.setVisible(!isRegIsAct);
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
