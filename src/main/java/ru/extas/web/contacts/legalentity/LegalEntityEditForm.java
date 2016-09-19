/**
 *
 */
package ru.extas.web.contacts.legalentity;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.LegalEntityFile;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.LegalEntityRepository;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.PredictConfirmedAction;
import ru.extas.web.commons.component.*;
import ru.extas.web.commons.component.address.AddressSuggestingComboBox;
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
    @PropertyId("legalAddress")
    private AddressSuggestingComboBox legalAddressComboBox;
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
    private AddressSuggestingComboBox postAddressField;

    @PropertyId("settlementAccount")
    private EditField settlementAccountField;
    @PropertyId("loroAccount")
    private EditField loroAccountField;
    @PropertyId("bankName")
    private EditField bankNameField;
    @PropertyId("bic")
    private EditField bicField;

    private SupplierSer<Company> companySupplier;

    public LegalEntityEditForm(final LegalEntity legalEntity) {
        super(legalEntity.isNew() ?
                "Ввод нового юр. лица в систему" :
                String.format("Редактирование юр. лица: %s", legalEntity.getName()), legalEntity);

        setWinWidth(860, Unit.PIXELS);

        addAttachListener(e -> {
            if (getEntity().getCompany() == null) {
                companyField.setReadOnly(false);
                companyField.setVisible(true);
                companyField.setRequired(true);
            } else {
                companyField.setReadOnly(true);
                companyField.getPropertyDataSource().setReadOnly(true);
                if (getEntity().getCompany().isNew()) {
                    companyField.setVisible(false);
                    companyField.setRequired(false);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final LegalEntity legalEntity) {
        if (legalEntity.isNew()) {
            // Инициализируем новый объект
            if (companySupplier != null)
                legalEntity.setCompany(companySupplier.get());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected LegalEntity saveEntity(LegalEntity legalEntity) {
        if (!legalEntity.getCompany().isNew()) {
            logger.debug("Saving contact data...");
            final LegalEntityRepository contactRepository = lookup(LegalEntityRepository.class);
            legalEntity = contactRepository.secureSave(legalEntity);
            NotificationUtil.showSuccess("Юр. лицо сохранено");
        }
        return legalEntity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);

        final PredictConfirmedAction predictConfirmedAction = new PredictConfirmedAction(
                "Необходимо сохранить Юр. лицо...",
                "Необходимо сохранить Юр. лицо прежде чем продолжить редактирование. Сохранить сейчас?",
                () -> !getEntity().isNew(), () -> save()
        );

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
        directorField.setLegalEntitySupplier(super::getEntity);
        directorField.addValueChangeListener(e -> {
            final Employee emp = directorField.getValue();
            final Company cmp = companyField.getValue();
            if (cmp == null)
                companyField.setValue(emp.getCompany());
        });
        directorField.setNewEmployeePrecondition(predictConfirmedAction);
        formLayout.addComponent(directorField);

        accountantField = new EmployeeField("Главный бухгалтер",
                "Выберите или введите главного бухгалтера юридического лица");
        accountantField.setCompanySupplier(Optional.ofNullable(companySupplier).orElse(() -> companyField.getValue()));
        accountantField.setLegalEntitySupplier(super::getEntity);
        accountantField.addValueChangeListener(e -> {
            final Employee emp = accountantField.getValue();
            final Company cmp = companyField.getValue();
            if (cmp == null)
                companyField.setValue(emp.getCompany());
        });
        accountantField.setNewEmployeePrecondition(predictConfirmedAction);
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
/*        regAddressField = new AddressInfoField();
        formLayout.addComponent(regAddressField);*/
        legalAddressComboBox = new AddressSuggestingComboBox();
        formLayout.addComponent(legalAddressComboBox);

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

        postAddressField = new AddressSuggestingComboBox();
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
        formLayout.addComponent(settlementAccountField);
        // Корреспондентский счет
        loroAccountField = new EditField("Корреспондентский счет", "Введите Корреспондентский счет юридического лица");
        formLayout.addComponent(loroAccountField);
        // Полное наименование банка
        bankNameField = new EditField("Наименование банка", "Введите наименование банка юридического лица");
        formLayout.addComponent(bankNameField);
        // БИК банка
        bicField = new EditField("БИК банка", "Введите БИК банка юридического лица");
        formLayout.addComponent(bicField);

        final boolean isDealerOrDistrib = lookup(CompanyRepository.class)
                .isDealerOrDistributor(getEntity().getCompany());
        ////////////////////////////////////////////////////////////////////////////////////////////
        // "Продукты"
        if (isDealerOrDistrib)
            formLayout.addComponent(new FormGroupHeader("Продукты"));
        productsField = new LegalProductsField();
        productsField.setVisible(isDealerOrDistrib);
        formLayout.addComponent(productsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Вкладка - "Бренды"
        if (isDealerOrDistrib)
            formLayout.addComponent(new FormGroupHeader("Бренды"));
        brandsField = new BrandsField();
        brandsField.setVisible(isDealerOrDistrib);
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

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
