package ru.extas.web.lead;

import com.google.common.base.Strings;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.model.contacts.*;
import ru.extas.model.lead.Lead;
import ru.extas.model.lead.LeadComment;
import ru.extas.model.lead.LeadFileContainer;
import ru.extas.model.lead.LeadMotor;
import ru.extas.model.sale.Sale;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.server.contacts.EmployeeRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.lead.LeadRepository;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.*;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.contacts.ClientField;
import ru.extas.web.contacts.employee.DealerEmployeeField;
import ru.extas.web.contacts.employee.EAEmployeeField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.contacts.legalentity.LegalEntityEditForm;
import ru.extas.web.contacts.person.ClientDataDecl;
import ru.extas.web.contacts.person.PersonEditForm;
import ru.extas.web.contacts.salepoint.DealerSalePointField;
import ru.extas.web.contacts.salepoint.SalePointsGrid;
import ru.extas.web.motor.MotorInstancesField;
import ru.extas.web.reference.RegionSelect;
import ru.extas.web.sale.ProductInstancesField;
import ru.extas.web.sale.SaleEditForm;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static ru.extas.model.common.ModelUtils.evictCache;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;
import static ru.extas.web.commons.TableUtils.initTableColumns;
import static ru.extas.web.contacts.salepoint.SalePointsDataDecl.*;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LeadEditForm extends ExtaEditForm<Lead> {

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
    // источник лида
    @PropertyId("source")
    private LeadSourceSelect sourceField;
    // Регион покупки техники
    @PropertyId("region")
    private RegionSelect regionField;
    // Техника
    @PropertyId("motorInstances")
    private MotorInstancesField motorInstancesField;
    // Мотосалон
    @PropertyId("pointOfSale")
    private EditField pointOfSaleField;
    @PropertyId("client")
    private ClientField clientField;
    @PropertyId("vendor")
    private DealerSalePointField vendorField;

    @PropertyId("productInstances")
    private ProductInstancesField productInstancesField;
    @PropertyId("comment")
    private TextArea commentField;

    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("responsibleAssist")
    private EmployeeField responsibleAssistField;
    @PropertyId("dealerManager")
    private EmployeeField dealerManagerField;

    @PropertyId("comments")
    private CommentsField<LeadComment> commentsField;

    @PropertyId("files")
    private FilesManageField docFilesEditor;

    private final boolean qualifyForm;
    private ExtaDbContainer<Client> clientsContainer;

    public LeadEditForm(final Lead lead, final boolean qualifyForm) {
        super(lead.isNew() ? "Ввод нового лида в систему" :
                qualifyForm ? MessageFormat.format("Квалификация лида № {0}", lead.getNum()) :
                        MessageFormat.format("Редактирование лида № {0}", lead.getNum()), lead);
        this.qualifyForm = qualifyForm;
        setWinWidth(800, Unit.PIXELS);
        if (qualifyForm) setWinWidth(1000, Unit.PIXELS);

        addAttachListener(e -> setFieldsStatus());
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

    private void fillClientFromLead(final Lead lead, final Client client) {
        client.setName(lead.getContactName());
        client.setPhone(lead.getContactPhone());
        client.setEmail(lead.getContactEmail());
    }

    private SalePoint createSalePointFromLead(final Lead lead) {
        final SalePoint salePoint = new SalePoint();
        salePoint.setName(lead.getPointOfSale());
        return salePoint;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Клиент"));
        if (getEntity().getStatus() == Lead.Status.NEW) {
            if (qualifyForm) {
                final Component clientPanel = createClientPanel(getEntity());
                form.addComponent(clientPanel);
            } else {
                contactNameField = new EditField("Имя", "Введите имя клиента");
                contactNameField.setColumns(25);
                contactNameField.setRequired(true);
                contactNameField.setRequiredError("Имя контакта не может быть пустым.");
                form.addComponent(contactNameField);

                cellPhoneField = new PhoneField("Телефон");
                form.addComponent(cellPhoneField);

                contactEmailField = new EmailField("E-Mail");
                form.addComponent(contactEmailField);

                sourceField = new LeadSourceSelect();
                form.addComponent(sourceField);
            }
        } else {
            clientField = new ClientField("Клиент");
            clientField.setRequired(true);
            form.addComponent(clientField);
        }

        createVendorSelectField(form);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Техника"));
        motorInstancesField = new MotorInstancesField("Техника", () -> new LeadMotor(getEntity()));
        form.addComponent(motorInstancesField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Дилер"));
        if (getEntity().getStatus() == Lead.Status.NEW) {
            if (getEntity().getVendor() == null) {
                if (qualifyForm) {
                    final Component vendorPanel = createVendorPanel(getEntity());
                    form.addComponent(vendorPanel);
                } else {
                    regionField = new RegionSelect();
                    regionField.setDescription("Укажите регион услуги");
                    form.addComponent(regionField);

                    pointOfSaleField = new EditField("Мотосалон");
                    form.addComponent(pointOfSaleField);
                }
            } else {
                placeVendorSelectField(form);
            }
        } else {
            placeVendorSelectField(form);
        }

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Продукты"));
        productInstancesField = new ProductInstancesField("Продукты в продаже", getEntity(), null,
                () -> motorInstancesField.getTotalPrice(),
                () -> motorInstancesField.getBrands(),
                () -> vendorField.getValue());
        productInstancesField.addValueChangeListener(forceModified);
        form.addComponent(productInstancesField);

        commentField = new TextArea("Примечание");
        commentField.setRows(3);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Ответственные"));
        responsibleField = new EAEmployeeField("Ответственный", "Выберите или введите ответственного менеджера");
        responsibleField.setRequired(getEntity().getStatus() != Lead.Status.NEW || qualifyForm);
        form.addComponent(responsibleField);

        responsibleAssistField = new EAEmployeeField("Заместитель", "Выберите или введите заместителя ответственного менеджера");
        form.addComponent(responsibleAssistField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Комментарии"));
        commentsField = new CommentsField<>(LeadComment.class);
        commentsField.addValueChangeListener(forceModified);
        form.addComponent(commentsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(LeadFileContainer.class);
        form.addComponent(docFilesEditor);

        return form;
    }

    private void createVendorSelectField(final FormLayout form) {
        vendorField = new DealerSalePointField("Мотосалон", "Название мотосалона");
        ///vendorField.setRequired(true);
        vendorField.addValueChangeListener(e -> {
            dealerManagerField.changeSalePoint();
            productInstancesField.refreshSalePoint();
            if (responsibleField.getValue() == null) {
                final SalePoint sp = vendorField.getValue();
                final CuratorsGroup curatorsGroup = sp.getCuratorsGroup();
                if (curatorsGroup != null && !curatorsGroup.getCurators().isEmpty()) {
                    final Iterator<Employee> employeeIterator = curatorsGroup.getCurators().iterator();
                    responsibleField.setValue(employeeIterator.next());
                    if (employeeIterator.hasNext())
                        responsibleAssistField.setValue(employeeIterator.next());
                }
            }
        });

        dealerManagerField = new DealerEmployeeField("Менеджер", "Выберите или введите ответственного менеджера со стороны дилера");
        dealerManagerField.setSalePointSupplier(() -> vendorField.getValue());
    }

    private void placeVendorSelectField(final FormLayout form) {
        form.addComponent(vendorField);
        form.addComponent(dealerManagerField);
    }

    private Component createVendorPanel(final Lead lead) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Label info = new Label(Fontello.INFO_CIRCLED.getHtml() +
                " Выберите подходящую точку продаж из списка или введите новую.<br/>Для поиска существующей торговой точки используйте поля фильтра.");
        info.setContentMode(ContentMode.HTML);
        info.addStyleName(ExtaTheme.LABEL_SMALL);
        info.addStyleName(ExtaTheme.LABEL_LIGHT);
        layout.addComponent(info);

        final SalePointsGrid grid = new SalePointsGrid() {
            @Override
            protected List<UIAction> createActions() {
                return Collections.emptyList();
            }

            @Override
            protected GridDataDecl createDataDecl() {
                final GridDataDecl dataDecl = super.createDataDecl();
                dataDecl.setColumnCollapsed("company.name", true);
                dataDecl.setColumnCollapsed("posAddress.regionWithType", true);
                dataDecl.setColumnCollapsed("posAddress.cityWithType", true);
                dataDecl.setColumnCollapsed("phone", true);
                dataDecl.setColumnCollapsed("www", true);
                dataDecl.setColumnCollapsed(SALEPOINT_LE_COLUMN, true);
                dataDecl.setColumnCollapsed(SALEPOINT_INN_COLUMN, true);
                return dataDecl;
            }

            @Override
            public List getDefaultFilterFields() {
                return newArrayList("name", SALEPOINT_BRANDS_COLUMN,
                        "posAddress.regionWithType", "posAddress.cityWithType");
            }

            @Override
            protected void initTable(final Mode mode) {
                super.initTable(mode);
                // Обрабатываем выбор контакта
                table.addValueChangeListener(event -> {
                    final SalePoint curObj = getFirstSelectedEntity();
                    lead.setVendor(curObj);
                    vendorField.setValue(curObj);
                    setModified(true);
                });
                // Задаем значения фильтра из данных лида
                // Название
                table.setFilterFieldValue("name", lead.getPointOfSale());
                // Бренд
//                table.setFilterFieldValue(SALEPOINT_BRANDS_COLUMN, lead.getMotorBrand());
                // Регион
                table.setFilterFieldValue("posAddress.regionWithType", lead.getRegion());

                table.setHeight(15, Unit.EM);
                table.addStyleName(ExtaTheme.TABLE_SMALL);
            }
        };
        grid.setToolbarVisible(false);
        grid.setFilterVisible(true);
        layout.addComponent(grid);

        final Panel panel = new Panel("Квалификация точки продаж", layout);
        final VerticalLayout components = new VerticalLayout(panel);
        components.setMargin(new MarginInfo(true, false, true, false));
        return components;
    }

    private Component createClientPanel(final Lead lead) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        final Table table = new Table();
        table.setRequired(true);

        final Label info = new Label(Fontello.INFO_CIRCLED.getHtml() +
                " Выберите подходящего клиента из списка или введите нового.<br/>Для поиска существующего клиента используйте поля фильтра.");
        info.setContentMode(ContentMode.HTML);
        info.addStyleName(ExtaTheme.LABEL_SMALL);
        info.addStyleName(ExtaTheme.LABEL_LIGHT);
        layout.addComponent(info);

        // Запрос данных
        clientsContainer = new ExtaDbContainer<>(Client.class);

        final MenuBar menuBar = new MenuBar();
        menuBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
        final MenuBar.MenuItem newMenu = menuBar.addItem("Новый клиент", Fontello.DOC_NEW, null);
        newMenu.addItem("Физическое лицо", FontAwesome.USER, e -> {
            final Person newObj = new Person();
            fillClientFromLead(lead, newObj);

            final PersonEditForm editWin = new PersonEditForm(newObj);
            editWin.setModified(true);

            editWin.addCloseFormListener(event1 -> {
                if (editWin.isSaved()) {
                    clientsContainer.refresh();
                    table.setValue(clientsContainer.getEntityItemId(editWin.getEntity()));
                }
            });
            FormUtils.showModalWin(editWin);
        });
        newMenu.addItem("Юридическое лицо", FontAwesome.BUILDING, e -> {
            final LegalEntity newObj = new LegalEntity();
            fillClientFromLead(lead, newObj);

            final LegalEntityEditForm editWin = new LegalEntityEditForm(newObj);
            editWin.setModified(true);

            editWin.addCloseFormListener(event1 -> {
                if (editWin.isSaved()) {
                    clientsContainer.refresh();
                    table.setValue(clientsContainer.getEntityItemId(editWin.getEntity()));
                }
            });
            FormUtils.showModalWin(editWin);
        });

        layout.addComponent(menuBar);

        final EditField name = new EditField("Имя");
        name.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        name.setIcon(Fontello.FILTER);
        name.setValue(lead.getContactName());

        final PhoneField phone = new PhoneField("Телефон");
        phone.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        phone.setIcon(Fontello.FILTER);
        phone.setValue(lead.getContactPhone());

        name.addTextChangeListener(e -> setClientsFilter(e.getText(), getPartOfPhoneValue(phone.getValue())));
        phone.addTextChangeListener(e -> setClientsFilter(name.getValue(), getPartOfPhoneValue(e.getText())));
        setClientsFilter(name.getValue(), getPartOfPhoneValue(phone.getValue()));

        final HorizontalLayout searchLay = new HorizontalLayout(name, phone);
        searchLay.setSpacing(true);
        layout.addComponent(searchLay);

        // Общие настройки таблицы
        table.setContainerDataSource(clientsContainer);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setNullSelectionAllowed(false);
        table.setHeight(10, Unit.EM);
        table.setWidth(100, Unit.PERCENTAGE);
        table.addStyleName(ExtaTheme.TABLE_SMALL);
        // Настройка столбцов таблицы
        table.setColumnHeaderMode(Table.ColumnHeaderMode.EXPLICIT);
        final GridDataDecl dataDecl = new ClientDataDecl();
        initTableColumns(table, dataDecl);
        // Обрабатываем выбор контакта
        table.addValueChangeListener(event -> {
            final Client curObj = extractBean(table.getItem(table.getValue()));
            lead.setClient(curObj);
            setModified(true);
        });
        layout.addComponent(table);


        final Panel panel = new Panel("Квалификация клиента", layout);
        final VerticalLayout components = new VerticalLayout(panel);
        components.setMargin(new MarginInfo(true, false, true, false));
        return components;
    }

    private String getPartOfPhoneValue(final String phone) {
        String value = null;
        try {
            value = lookup(PhoneConverter.class).convertToModel(phone, String.class, null);
        } catch (final Converter.ConversionException e) {
            value = phone;
        }
        return value;
    }

    private void setClientsFilter(final String name, final String cellPhone) {
        clientsContainer.removeAllContainerFilters();
        final List<Container.Filter> filters = newArrayListWithCapacity(3);
        if (!Strings.isNullOrEmpty(name)) {
            filters.add(new Like("name", MessageFormat.format("%{0}%", name), false));
        }
        if (!Strings.isNullOrEmpty(cellPhone)) {
            filters.add(new Like("phone", MessageFormat.format("%{0}%", cellPhone), false));
            filters.add(new Like("secondPhone", MessageFormat.format("%{0}%", cellPhone), false));
        }
        if (!filters.isEmpty())
            clientsContainer.addContainerFilter(new Or(filters.toArray(new Container.Filter[filters.size()])));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final Lead lead) {
        if (lead.isNew()) {
            lead.setStatus(Lead.Status.NEW);
            final UserManagementService userService = lookup(UserManagementService.class);
            final Employee user = userService.getCurrentUserEmployee();
            if (user != null) {
                final EmployeeRepository employeeRepository = lookup(EmployeeRepository.class);
                if (employeeRepository.isEAEmployee(user)) {
                    // Для сотрудника ЕА:
                    // * Устанавливаем ТТ которую курирует сотрудник
                    final SalePointRepository salePointRepository = lookup(SalePointRepository.class);
                    salePointRepository.findByCurator(user)
                            .stream().findFirst().ifPresent(sp -> lead.setVendor(sp));
                    // * Ответственный - сотрудник вводящий лид.
                    lead.setResponsible(user);
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - пусто.
                    // * Источник лида
                    lead.setSource(LeadSourceSelect.EA_MANAGER);
                } else if (employeeRepository.isDealerEmployee(user)) {
                    // Для сотрудника дилера:
                    final SalePoint salePoint = user.getWorkPlace();
                    if (salePoint != null) {
                        // * Устанавливаем ТТ сотрудника дилера
                        lead.setVendor(salePoint);
                        lead.setPointOfSale(salePoint.getName());
                        // * Ответственный - сотрудник ЕА который курирует данную торговую точку дилера.
                        final CuratorsGroup curatorsGroup = salePoint.getCuratorsGroup();
                        if (curatorsGroup != null && !curatorsGroup.getCurators().isEmpty())
                            lead.setResponsible(curatorsGroup.getCurators().iterator().next());
                    }
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - сотрудник который вводит лид.
                    lead.setDealerManager(user);
                    // * Источник лида
                    lead.setSource(LeadSourceSelect.DEALER_MANAGER);
                } else if (employeeRepository.isCallcenterEmployee(user)) {
                    // Для сотрудника колл-центра:
                    // * Ответственный - сотрудник ЕА который курирует торговую точку дилера (если определена).
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - пусто.
                    // * Источник лида
                    lead.setSource(LeadSourceSelect.CLIENT_CALL);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Lead saveEntity(Lead lead) {
        final LeadRepository leadRepository = lookup(LeadRepository.class);
        final SaleRepository saleRepository = lookup(SaleRepository.class);
        if (qualifyForm) {
            lead = leadRepository.qualify(lead);
            evictCache(saleRepository.findByLead(lead));
            final Sale sale = saleRepository.findByLead(lead);
            NotificationUtil.showSuccess("Лид квалифицирован");
            ConfirmDialog.show(UI.getCurrent(),
                    "Лид успешно квалифицирован...",
                    MessageFormat.format("Лид № {0} был успешно квалифицирован, на его основе была создана продажа № {1}. " +
                            "Продажа доступна для просмотра/редактирования в разделе \"Продажи\". " +
                            "Хотите просмотреть/отредактировать эту продажу сейчас?", lead.getNum(), sale.getNum()),
                    "Да", "Нет", () -> {
                        final SaleEditForm saleEditForm = new SaleEditForm(sale);
                        FormUtils.showModalWin(saleEditForm);
                    });
        } else {
            lead = leadRepository.secureSave(lead);
            NotificationUtil.showSuccess("Лид сохранен");
        }

        // Решаем проблему с автоинкрементами базы о  которых не знает JPA
        if (lead.getNum() == null)
            evictCache(lead);

        return lead;
    }

}
