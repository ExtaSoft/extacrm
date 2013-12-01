package ru.extas.web.lead;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.AddressInfo;
import ru.extas.model.Company;
import ru.extas.model.Lead;
import ru.extas.model.Person;
import ru.extas.server.LeadService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.CompanySelect;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 */
public class LeadEditForm extends AbstractEditForm<Lead> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    // Имя контакта
    @PropertyId("contactName")
    private EditField contactNameField;
    @PropertyId("client")
    private PersonSelect clientField;
    @PropertyId("contactPhone")
    private PhoneField cellPhoneField;
    // Эл. почта
    @PropertyId("contactEmail")
    private EmailField contactEmailField;
    // Регион покупки техники
    @PropertyId("region")
    private RegionSelect regionField;
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
    // Мотосалон
    @PropertyId("pointOfSale")
    private EditField pointOfSaleField;
    @PropertyId("vendor")
    private CompanySelect vendorField;
    @PropertyId("comment")
    private TextArea commentField;

    private boolean qualifyForm;

    public LeadEditForm(final String caption, final BeanItem<Lead> obj, boolean qualifyForm) {
        super(caption, obj);
        this.qualifyForm = qualifyForm;
        setFoeldsStatus();
    }

    private void setFoeldsStatus() {
        contactNameField.setEnabled(!qualifyForm);
        clientField.setVisible(qualifyForm);
        clientField.setRequired(qualifyForm);
        cellPhoneField.setEnabled(!qualifyForm);
        contactEmailField.setEnabled(!qualifyForm);
        regionField.setEnabled(!qualifyForm);
        motorTypeField.setEnabled(!qualifyForm);
        motorBrandField.setEnabled(!qualifyForm);
        motorModelField.setEnabled(!qualifyForm);
        mototPriceField.setEnabled(!qualifyForm);
        pointOfSaleField.setEnabled(!qualifyForm);
        vendorField.setVisible(qualifyForm);
        vendorField.setRequired(qualifyForm);
        commentField.setEnabled(!qualifyForm);
    }

    private Person createPersonFromLead(Lead lead) {
        Person person = new Person();
        person.setName(lead.getContactName());
        person.setCellPhone(lead.getContactPhone());
        person.setEmail(lead.getContactEmail());
        person.setActualAddress(new AddressInfo(lead.getRegion(), null, null, null));
        return person;

    }

    private Company createCompanyFromLead(Lead lead) {
        Company company = new Company();
        company.setFullName(lead.getPointOfSale());
        company.setName(lead.getPointOfSale());
        company.setActualAddress(new AddressInfo(lead.getRegion(), null, null, null));
        return company;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Lead obj) {
        final FormLayout form = new FormLayout();

        contactNameField = new EditField("Клиент", "Введите имя клиента");
        contactNameField.setColumns(25);
        contactNameField.setRequired(true);
        contactNameField.setRequiredError("Имя контакта не может быть пустым.");
        form.addComponent(contactNameField);

        clientField = new PersonSelect("Связан с клиентом", createPersonFromLead(obj));
        form.addComponent(clientField);

        cellPhoneField = new PhoneField("Телефон");
        form.addComponent(cellPhoneField);

        contactEmailField = new EmailField("E-Mail");
        form.addComponent(contactEmailField);

        regionField = new RegionSelect();
        regionField.setDescription("Укажите регион услуги");
        form.addComponent(regionField);

        motorTypeField = new MotorTypeSelect();
        form.addComponent(motorTypeField);

        motorBrandField = new MotorBrandSelect();
        form.addComponent(motorBrandField);

        motorModelField = new EditField("Модель техники", "Введите модель техники");
        motorModelField.setColumns(15);
        form.addComponent(motorModelField);

        mototPriceField = new EditField("Цена техники");
        form.addComponent(mototPriceField);

        pointOfSaleField = new EditField("Мотосалон");
        form.addComponent(pointOfSaleField);

        Company company = createCompanyFromLead(obj);
        vendorField = new CompanySelect("Связан с контрагентом", company);
//        vendorField.addFocusListener(new FieldEvents.FocusListener() {
//            @Override
//            public void focus(FieldEvents.FocusEvent event) {
//                if(vendorField.getValue() == null)
//                    vendorField.setConvertedValue();
//            }
//        });
        form.addComponent(vendorField);

        commentField = new TextArea("Комментарий");
        commentField.setColumns(25);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        return form;
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void initObject(final Lead obj) {
        if (obj.getId() == null) {
            obj.setStatus(Lead.Status.NEW);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Lead obj) {
        final LeadService leadService = lookup(LeadService.class);
        if (qualifyForm)
            leadService.qualify(obj);
        else
            leadService.persist(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void checkBeforeSave(final Lead obj) {
    }

}
