package ru.extas.web.contacts.employee;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.contacts.*;
import ru.extas.server.contacts.EmployeeRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.*;
import ru.extas.web.contacts.AddressInfoField;
import ru.extas.web.contacts.company.CompanyField;
import ru.extas.web.contacts.legalentity.LegalEntityField;
import ru.extas.web.contacts.salepoint.SalePointField;

import java.text.MessageFormat;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Панель ввода/редактирования сотрудника
 *
 * @author Valery Orlov
 *         Date: 19.10.2014
 *         Time: 16:45
 */
public class EmployeeEditForm extends ExtaEditForm<Employee> {

    private final Company company;

    // Общие данные
    @PropertyId("name")
    private EditField nameField;

    // Контакты
    @PropertyId("email")
    private EmailField emailField;
    @PropertyId("phone")
    private PhoneField phoneField;
    @PropertyId("workPhone")
    private PhoneField workPhoneField;

    // Работодатель
    @PropertyId("company")
    private CompanyField companyField;
    @PropertyId("workPlace")
    private SalePointField workPlaceField;
    @PropertyId("jobPosition")
    private EditField jobPositionField;
    @PropertyId("jobDepartment")
    private EditField jobDepartmentField;
    @PropertyId("legalWorkPlace")
    private LegalEntityField legalWorkPlaceField;

    // Паспортные данные
    @PropertyId("birthday")
    private LocalDateField birthdayField;
    @PropertyId("birthPlace")
    private EditField birthPlaceField;
    @PropertyId("passNum")
    private EditField passNumField;
    @PropertyId("passIssueDate")
    private LocalDateField passIssueDateField;
    @PropertyId("passIssuedBy")
    private TextArea passIssuedByField;
    @PropertyId("passIssuedByNum")
    private EditField passIssuedByNumField;

    // Адрес регистрации
    @PropertyId("regAddress")
    private AddressInfoField regAddressField;

    public EmployeeEditForm(Employee employee, Company company) {
        super(employee.isNew() ?
                "Новый сотрудник" :
                MessageFormat.format("Сотрудник: {0}", employee.getName()));
        this.company = company;

        final BeanItem<Employee> beanItem = new BeanItem<>(employee);
        initForm(beanItem);
    }

    @Override
    protected void initObject(Employee obj) {
        if (obj.getRegAddress() == null)
            obj.setRegAddress(new AddressInfo());
        if(company != null)
            obj.setCompany(company);
    }

    @Override
    protected Employee saveObject(Employee obj) {
        final EmployeeRepository employeeRepository = lookup(EmployeeRepository.class);
        obj = employeeRepository.secureSave(obj);
        NotificationUtil.showSuccess("Сотрудник сохранен");
        return obj;
    }

    @Override
    protected ComponentContainer createEditFields(Employee obj) {
        final FormLayout formLayout = new ExtaFormLayout();
        formLayout.setMargin(true);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Общие данные"));
        nameField = new EditField("Имя");
        nameField.setColumns(15);
        nameField.setDescription("Введите имя (ФИО) сотрудника");
        nameField.setInputPrompt("Фамилия Имя (Отчество)");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя сотрудника не может быть пустым. Пожалуйста введите ФИО.");
        formLayout.addComponent(nameField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Контакты"));
        emailField = new EmailField("E-Mail");
        formLayout.addComponent(emailField);

        phoneField = new PhoneField("Мобильный телефон");
        formLayout.addComponent(phoneField);

        workPhoneField = new PhoneField("Рабочий телефон");
        formLayout.addComponent(workPhoneField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Работодатель"));
        companyField = new CompanyField("Компания");
        companyField.setRequired(true);
        companyField.addValueChangeListener(e -> {
            final Company company = (Company) companyField.getConvertedValue();
            workPlaceField.setCompany(company);
            legalWorkPlaceField.setCompany(company);
        });
        companyField.setVisible(company == null);
        formLayout.addComponent(companyField);

        workPlaceField = new SalePointField("Торговая точка", "Укажите торговую точку сотрудника", obj.getCompany());
        workPlaceField.addValueChangeListener(e -> {
            final SalePoint salePoint = (SalePoint) e.getProperty().getValue();
            if (salePoint != null)
                companyField.setValue(salePoint.getCompany());
        });
        formLayout.addComponent(workPlaceField);

//        jobDepartmentField = new;
//        formLayout.addComponent(jobDepartmentField);

        legalWorkPlaceField = new LegalEntityField("Юр. лицо", "Укажите Юридическое лицо в котором оформлен сотрудник", obj.getCompany());
        legalWorkPlaceField.addValueChangeListener(e -> {
            final LegalEntity legalEntity = (LegalEntity) e.getProperty().getValue();
            if (legalEntity != null)
                companyField.setValue(legalEntity.getCompany());
        });
        formLayout.addComponent(legalWorkPlaceField);

        jobPositionField = new EditField("Должность", "Укажите должность сотрудника");
        formLayout.addComponent(jobPositionField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Паспортные данные"));
        birthdayField = new LocalDateField("Дата рождения", "Введите дату рождения сотрудника");
        birthdayField.setImmediate(true);
        birthdayField.setInputPrompt("31.12.1978");
        birthdayField.setDateFormat("dd.MM.yyyy");
        birthdayField.setConversionError("{0} не является допустимой датой. Формат даты: ДД.ММ.ГГГГ");
        formLayout.addComponent(birthdayField);

        birthPlaceField = new EditField("Место рождения");
        formLayout.addComponent(birthPlaceField);

        passNumField = new EditField("Серия/номер");
        formLayout.addComponent(passNumField);

        passIssueDateField = new LocalDateField("Дата выдачи", "Дата выдачи документа");
        formLayout.addComponent(passIssueDateField);

        passIssuedByField = new TextArea("Кем выдан");
        passIssuedByField.setDescription("Наименование органа выдавшего документ");
        passIssuedByField.setInputPrompt("Наименование органа выдавшего документ");
        passIssuedByField.setNullRepresentation("");
        passIssuedByField.setRows(2);
        formLayout.addComponent(passIssuedByField);

        passIssuedByNumField = new EditField("Код подразделения");
        formLayout.addComponent(passIssuedByNumField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        formLayout.addComponent(new FormGroupHeader("Адрес регистрации"));
        regAddressField = new AddressInfoField();
        formLayout.addComponent(regAddressField);

        return formLayout;
    }
}
