package ru.extas.web.lead;

import com.google.common.base.Strings;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.server.lead.LeadRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.commons.AbstractEditForm;
import ru.extas.web.contacts.*;
import ru.extas.web.motor.MotorBrandSelect;
import ru.extas.web.motor.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;
import static ru.extas.web.commons.TableUtils.initTableColumns;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LeadEditForm extends AbstractEditForm<Lead> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    // Имя контакта
    @PropertyId("contactName")
    private EditField contactNameField;
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
    private SalePointSelect vendorField;
    @PropertyId("comment")
    private TextArea commentField;

    private boolean qualifyForm;
    private ExtaDataContainer<SalePoint> vendorsContainer;
    private ExtaDataContainer<Person> clientsContainer;

    public LeadEditForm(Lead lead, boolean qualifyForm) {
        super(lead.isNew() ? "Ввод нового лида в систему" : "Редактирование лида");
        this.qualifyForm = qualifyForm;
        initForm(new BeanItem<>(lead));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach() {
        super.attach();
        setFieldsStatus();
    }

    private void setFieldsStatus() {
//        contactNameField.setReadOnly(qualifyForm);
//        cellPhoneField.setReadOnly(qualifyForm);
//        contactEmailField.setReadOnly(qualifyForm);
//        regionField.setReadOnly(qualifyForm);
//        motorTypeField.setReadOnly(qualifyForm);
//        motorBrandField.setReadOnly(qualifyForm);
//        motorModelField.setReadOnly(qualifyForm);
//        mototPriceField.setReadOnly(qualifyForm);
//        pointOfSaleField.setReadOnly(qualifyForm);
//        commentField.setReadOnly(qualifyForm);
    }

    private Person createPersonFromLead(Lead lead) {
        Person person = new Person();
        person.setName(lead.getContactName());
        person.setPhone(lead.getContactPhone());
        person.setEmail(lead.getContactEmail());
        person.setActualAddress(new AddressInfo(lead.getRegion(), null, null, null));
        return person;

    }

    private SalePoint createCompanyFromLead(Lead lead) {
        SalePoint salePoint = new SalePoint();
        salePoint.setName(lead.getPointOfSale());
        salePoint.setActualAddress(new AddressInfo(lead.getRegion(), null, null, null));
        return salePoint;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields(final Lead obj) {
        final FormLayout form = new ExtaFormLayout();

        contactNameField = new EditField("Клиент", "Введите имя клиента");
        contactNameField.setColumns(25);
        contactNameField.setRequired(true);
        contactNameField.setRequiredError("Имя контакта не может быть пустым.");
        contactNameField.setImmediate(true);
        contactNameField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (qualifyForm)
                    setClientsFilter(event.getText());
            }
        });
        contactNameField.addValueChangeListener(new ConactChangeListener());
        form.addComponent(contactNameField);

        cellPhoneField = new PhoneField("Телефон");
        cellPhoneField.setImmediate(true);
        cellPhoneField.addValueChangeListener(new ConactChangeListener());
        form.addComponent(cellPhoneField);

        contactEmailField = new EmailField("E-Mail");
        contactEmailField.setImmediate(true);
        contactEmailField.addValueChangeListener(new ConactChangeListener());
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
        pointOfSaleField.setImmediate(true);
        pointOfSaleField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (qualifyForm)
                    setVendorsFilter();
            }
        });
        vendorField = new SalePointSelect("Мотосалон", "Название мотосалона", null);
        vendorField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Property property = event.getProperty();
                if (property != null) {
                    Object value = property.getValue();
                    if (value != null)
                        pointOfSaleField.setValue(((SalePoint) value).getName());
                }
            }
        });
        if (obj.getVendor() == null) {
            form.addComponent(pointOfSaleField);
        } else {
            form.addComponent(vendorField);
        }


        commentField = new TextArea("Комментарий");
        commentField.setColumns(25);
        commentField.setRows(6);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        if (qualifyForm) {
            HorizontalLayout layout = new HorizontalLayout(form, createQualifyForm(obj));
            layout.setSpacing(true);
            return layout;
        } else
            return form;
    }

    private Component createQualifyForm(Lead lead) {
        Component clientPanel = createClientPanel(lead);
        Component vendorPanel = createVendorPanel(lead);
        VerticalLayout qForm = new VerticalLayout(clientPanel, vendorPanel);
        qForm.setSpacing(true);
        qForm.setExpandRatio(clientPanel, 1);
        qForm.setExpandRatio(vendorPanel, 1);
        return qForm;
    }

    private Panel createVendorPanel(final Lead lead) {
        VerticalLayout panel = new VerticalLayout();
        panel.setSpacing(true);

        final Table table = new Table();
        table.setRequired(true);
        // Запрос данных
        vendorsContainer = new ExtaDataContainer<>(SalePoint.class);
        vendorsContainer.addNestedContainerProperty("actualAddress.region");
        setVendorsFilter();

        Button newBtn = new Button("Новый");
        newBtn.setIcon(Fontello.DOC_NEW);
        newBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final SalePoint newObj = createCompanyFromLead(lead);

                final SalePointEditForm editWin = new SalePointEditForm(newObj);
                editWin.setModified(true);

                editWin.addCloseFormListener(new AbstractEditForm.CloseFormListener() {
                    @Override
                    public void closeForm(AbstractEditForm.CloseFormEvent event) {
                        if (editWin.isSaved()) {
                            vendorsContainer.refresh();
                            table.setValue(newObj.getId());
                        }
                    }
                });
                FormUtils.showModalWin(editWin);
            }
        });
        panel.addComponent(newBtn);

        // Общие настройки таблицы
        table.setContainerDataSource(vendorsContainer);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
        table.setHeight(10, Unit.EM);
        // Настройка столбцов таблицы
        table.setColumnHeaderMode(Table.ColumnHeaderMode.EXPLICIT);
        GridDataDecl dataDecl = new ContactDataDecl();
        initTableColumns(table, dataDecl);
        table.setColumnCollapsed("phone", true);
        table.setColumnCollapsed("email", true);
        // Обрабатываем выбор контакта
        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                final SalePoint curObj = extractBean(table.getItem(table.getValue()));
                lead.setVendor(curObj);
                pointOfSaleField.setValue(curObj.getName());
                setModified(true);
            }
        });
        panel.addComponent(table);

        return new Panel("Мото салон", panel);
    }

    private void setVendorsFilter() {
        vendorsContainer.removeAllContainerFilters();
        String name = pointOfSaleField.getValue();
        if (!Strings.isNullOrEmpty(name))
            vendorsContainer.addContainerFilter(new Like("name", MessageFormat.format("%{0}%", name), false));
    }

    private Panel createClientPanel(final Lead lead) {
        VerticalLayout panel = new VerticalLayout();
        panel.setSpacing(true);

        final Table table = new Table();
        table.setRequired(true);

        // Запрос данных
        clientsContainer = new ExtaDataContainer<>(Person.class);
        clientsContainer.addNestedContainerProperty("actualAddress.region");
        setClientsFilter(lead.getContactName());

        Button newBtn = new Button("Новый");
        newBtn.setIcon(Fontello.DOC_NEW);
        newBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final Person newObj = createPersonFromLead(lead);

                final PersonEditForm editWin = new PersonEditForm(newObj);
                editWin.setModified(true);

                editWin.addCloseFormListener(new AbstractEditForm.CloseFormListener() {
                    @Override
                    public void closeForm(AbstractEditForm.CloseFormEvent event) {
                        if (editWin.isSaved()) {
                            clientsContainer.refresh();
                            table.setValue(newObj.getId());
                        }
                    }
                });
                FormUtils.showModalWin(editWin);
            }
        });
        panel.addComponent(newBtn);

        // Общие настройки таблицы
        table.setContainerDataSource(clientsContainer);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
        table.setHeight(10, Unit.EM);
        // Настройка столбцов таблицы
        table.setColumnHeaderMode(Table.ColumnHeaderMode.EXPLICIT);
        GridDataDecl dataDecl = new PersonDataDecl();
        initTableColumns(table, dataDecl);
        table.setColumnCollapsed("sex", true);
        table.setColumnCollapsed("birthday", true);
        table.setColumnCollapsed("email", true);
        // Обрабатываем выбор контакта
        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                final Person curObj = extractBean(table.getItem(table.getValue()));
                lead.setClient(curObj);
                fillLeadFromClient(curObj);
                setModified(true);
            }
        });
        panel.addComponent(table);


        return new Panel("Клиент", panel);
    }

    private void fillLeadFromClient(Person contact) {
        contactNameField.setValue(contact.getName());
        cellPhoneField.setValue(contact.getPhone());
        contactEmailField.setValue(contact.getEmail());
    }

    private void setClientsFilter(String name) {
        //String name = contactNameField.getValue();
        String email = contactEmailField.getValue();
        String cellPhone = cellPhoneField.getValue();
        clientsContainer.removeAllContainerFilters();
        List<Container.Filter> filters = newArrayList();
        if (!Strings.isNullOrEmpty(name)) {
            filters.add(new Like("name", MessageFormat.format("%{0}%", name), false));
        }
        if (!Strings.isNullOrEmpty(cellPhone)) {
            filters.add(new Like("phone", MessageFormat.format("%{0}%", cellPhone), false));
        }
        if (!Strings.isNullOrEmpty(email)) {
            filters.add(new Like("email", MessageFormat.format("%{0}%", email), false));
        }
        if (!filters.isEmpty())
            clientsContainer.addContainerFilter(new Or(filters.toArray(new Container.Filter[filters.size()])));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initObject(final Lead obj) {
        if (obj.getId() == null) {
            obj.setStatus(Lead.Status.NEW);
            UserManagementService userService = lookup(UserManagementService.class);
            Person user = userService.getCurrentUserContact();
            if (user != null) {
                if (!isEmpty(user.getWorkPlaces())) {
                    SalePoint salePoint = user.getWorkPlaces().iterator().next();
                    obj.setVendor(salePoint);
                    obj.setPointOfSale(salePoint.getName());
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveObject(final Lead obj) {
        LeadRepository leadRepository = lookup(LeadRepository.class);
        if (qualifyForm) {
            leadRepository.qualify(obj);
            NotificationUtil.showSuccess("Лид квалифицирован");
        } else {
            Lead lead = leadRepository.secureSave(obj);
            if (obj.getId() == null)
                lookup(EntityManager.class).getEntityManagerFactory().getCache().evict(lead.getClass(), lead.getId());
            NotificationUtil.showSuccess("Лид сохранен");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkBeforeSave(final Lead obj) {
    }

    private enum Qualify {
        EXIST, NEW
    }

    private class ConactChangeListener implements Property.ValueChangeListener {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (qualifyForm)
                setClientsFilter(contactNameField.getValue());
        }
    }
}
