package ru.extas.web.lead.embedded;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.lead.LeadRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.contacts.SalePointSimpleSelect;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.UiUtils.initUi;
import static ru.extas.web.UiUtils.showValidationError;

/**
 * Форма ввода лида для внедрения во внешние порталы
 *
 * @author Valery Orlov
 *         Date: 14.04.2014
 *         Time: 16:43
 * @version $Id: $Id
 * @since 0.4.2
 */
@Component
@Scope("session")
@Theme("extacrm")
@Title("Extreme Assistance CRM")
public class LeadInputFormUI extends UI {

    private final static Logger logger = LoggerFactory.getLogger(LeadInputFormUI.class);

    // Компоненты редактирования
    // Имя контакта
    @PropertyId("contactName")
    private EditField contactNameField;
    @PropertyId("contactPhone")
    private PhoneField cellPhoneField;
    // Эл. почта
    @PropertyId("contactEmail")
    private EmailField contactEmailField;
    // Регион проживания клиента
    @PropertyId("contactRegion")
    private RegionSelect contactRegionField;
    // Тип техники
    @PropertyId("motorType")
    private ComboBox motorTypeField;
    // Марка техники
    @PropertyId("motorBrand")
    private MotorBrandSelect motorBrandField;
    // Модель техники
    @PropertyId("motorModel")
    private EditField motorModelField;
    // Стоимость техники
    @PropertyId("motorPrice")
    private EditField mototPriceField;
    // Регион покупки техники
    @PropertyId("region")
    private RegionSelect regionField;
    // Мотосалон
    @PropertyId("pointOfSale")
    private EditField pointOfSaleField;
    @PropertyId("vendor")
    private SalePointSimpleSelect vendorField;
    @PropertyId("comment")
    private TextArea commentField;

    private FieldGroup fieldGroup;

    /** {@inheritDoc} */
    @Override
    protected void init(VaadinRequest request) {

        initUi(this);

        final Lead lead = new Lead();
        lead.setStatus(Lead.Status.NEW);

        // Прочитать параметры адресной строки
        Company company = null;
        Map<String, String[]> params = request.getParameterMap();
        String[] companyPrm = params.get("company");
        if (companyPrm != null && companyPrm.length > 0) {
            String companyId = companyPrm[0];
            if (!isNullOrEmpty(companyId)) {
                CompanyRepository companyRepository = lookup(CompanyRepository.class);
                company = companyRepository.findOne(companyId);
                if (company == null) {
                    Notification.show("Неверные параметры формы!",
                            "Неверно задан идентификатор компании в параметре 'company'.",
                            Notification.Type.ERROR_MESSAGE);
                    return;
                }
            } else {
                Notification.show("Неверные параметры формы!",
                        "Неверно задан идентификатор компании в параметре 'company'.",
                        Notification.Type.ERROR_MESSAGE);
                return;
            }
        } else {
            String[] salePointPrm = params.get("salepoint");
            if (salePointPrm != null && salePointPrm.length > 0) {
                String salePointId = salePointPrm[0];
                if (!isNullOrEmpty(salePointId)) {
                    SalePointRepository salePointRepository = lookup(SalePointRepository.class);
                    SalePoint salePoint = salePointRepository.findOne(salePointId);
                    if (salePoint == null) {
                        Notification.show("Неверные параметры формы!",
                                "Неверно задан идентификатор торговой точки в параметре 'salepoint'.",
                                Notification.Type.ERROR_MESSAGE);
                        return;
                    } else
                        lead.setVendor(salePoint);
                } else {
                    Notification.show("Неверные параметры формы!",
                            "Неверно задан идентификатор торговой точки в параметре 'salepoint'.",
                            Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        }

        FormLayout form = new FormLayout();

        contactNameField = new EditField("Имя", "Введите фамилию имя отчество");
        contactNameField.setInputPrompt("Фамилия Имя Отчество");
        contactNameField.setColumns(25);
        contactNameField.setRequired(true);
        contactNameField.setImmediate(true);
        form.addComponent(contactNameField);

        cellPhoneField = new PhoneField("Телефон");
        cellPhoneField.setRequired(true);
        cellPhoneField.setImmediate(true);
        form.addComponent(cellPhoneField);

        contactEmailField = new EmailField("E-Mail");
        contactEmailField.setRequired(true);
        contactEmailField.setImmediate(true);
        form.addComponent(contactEmailField);

        contactRegionField = new RegionSelect("Регион проживания");
        contactRegionField.setDescription("Укажите регион проживания");
        contactRegionField.setRequired(true);
        form.addComponent(contactRegionField);

        motorTypeField = new MotorTypeSelect();
        motorTypeField.setInputPrompt("Выберите тип...");
        motorTypeField.setRequired(true);
        form.addComponent(motorTypeField);

        motorBrandField = new MotorBrandSelect();
        motorBrandField.setInputPrompt("Выберите марку...");
        motorBrandField.setRequired(true);
        form.addComponent(motorBrandField);

        motorModelField = new EditField("Модель техники", "Введите модель техники");
        motorModelField.setColumns(15);
        motorModelField.setRequired(true);
        form.addComponent(motorModelField);

        mototPriceField = new EditField("Цена техники");
        mototPriceField.setRequired(true);
        form.addComponent(mototPriceField);

        regionField = new RegionSelect("Регион покупки");
        regionField.setDescription("Укажите регион покупки техники");
        if (company == null && lead.getVendor() == null) {
            regionField.setRequired(true);
            form.addComponent(regionField);
        }

        pointOfSaleField = new EditField("Мотосалон");
        pointOfSaleField.setColumns(25);
        pointOfSaleField.setImmediate(true);
        if (company == null && lead.getVendor() == null) {
            pointOfSaleField.setRequired(true);
            form.addComponent(pointOfSaleField);
        }

        vendorField = new SalePointSimpleSelect("Мотосалон", "Выберите мотосалон");
        vendorField.setInputPrompt("Выберите мотосалон...");
        vendorField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Property property = event.getProperty();
                if (property != null) {
                    Object value = property.getValue();
                    if (value != null) {
                        pointOfSaleField.setValue(((SalePoint) vendorField.getConvertedValue()).getName());
                        regionField.setValue(((SalePoint) vendorField.getConvertedValue()).getActualAddress().getRegion());
                    }
                }
            }
        });
        if (company != null && lead.getVendor() == null) {
            vendorField.setRequired(true);
            vendorField.setContainerFilter(company, null);
            form.addComponent(vendorField);
        }

        commentField = new TextArea("Комментарий");
        commentField.setInputPrompt("Укажите любую дополнительную информацию");
        commentField.setColumns(25);
        commentField.setRows(6);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        // Привязываем поля
        BeanItem<Lead> beanItem = new BeanItem<>(lead);
        fieldGroup = new FieldGroup(beanItem);
        fieldGroup.setBuffered(true);
        fieldGroup.bindMemberFields(this);

        VerticalLayout panel = new VerticalLayout(form);
        panel.setMargin(true);

        // Кнопка ввода
        Button submitBtn = new Button("Отправить", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {

                if (fieldGroup.isValid()) {
                    try {
                        fieldGroup.commit();
                        saveObject(lead);
                        Notification.show(
                                "Заявка отправлена!",
                                "В ближайшее время наш менеджер свяжется с вами для уточнения деталей.",
                                Notification.Type.HUMANIZED_MESSAGE);
                        LeadInputFormUI.this.setContent(new HorizontalLayout());
                    } catch (final FieldGroup.CommitException e) {
                        // TODO Correct error handling
                        logger.error("Can't apply form changes", e);
                        Notification.show("Невозможно отправить данные!", e.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    close();
                } else {
                    String caption = "Невозможно отправить данные!";
                    showValidationError(caption, fieldGroup);
                }
            }

        });

        submitBtn.setStyleName("icon-ok");
        submitBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER, ShortcutAction.ModifierKey.CTRL);

        panel.addComponent(submitBtn);

        setContent(panel);

    }

    private void saveObject(Lead lead) {
        LeadRepository leadRepository = lookup(LeadRepository.class);
        UserManagementService userService = lookup(UserManagementService.class);

        // Определить потенциального пользователя
        Person user = null;
        if (lead.getVendor() != null) {
            Set<Person> employees = lead.getVendor().getEmployees();
            if (!isEmpty(employees))
                user = employees.iterator().next();
        }
        if (user == null)
            user = userService.findUserContactByLogin("admin");

        leadRepository.permitAndSave(lead, user);
    }


}
