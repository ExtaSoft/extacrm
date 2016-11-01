package ru.extas.web.sale;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.product.ProdCredit;
import ru.extas.model.product.Product;
import ru.extas.model.product.ProductInstance;
import ru.extas.model.sale.*;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.server.contacts.EmployeeRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.contacts.ClientField;
import ru.extas.web.contacts.employee.DealerEmployeeField;
import ru.extas.web.contacts.employee.EAEmployeeField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.contacts.legalentity.SPLegalEntityField;
import ru.extas.web.contacts.salepoint.DealerSalePointField;
import ru.extas.web.lead.LeadSourceSelect;
import ru.extas.web.motor.MotorInstancesField;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import static ru.extas.model.common.ModelUtils.evictCache;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SaleEditForm extends ExtaEditForm<Sale> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    // Имя клиента
    @PropertyId("client")
    private ClientField clientField;
    @PropertyId("clientContact")
    private EmployeeField clientContactField;
    // Техника
    @PropertyId("motorInstances")
    private MotorInstancesField motorInstancesField;
    // Мотосалон
    @PropertyId("dealer")
    private DealerSalePointField dealerField;
    @PropertyId("dealerLE")
    private SPLegalEntityField dealerLEField;
    @PropertyId("comment")
    private TextArea commentField;
    @PropertyId("productInstances")
    private ProductInstancesField productInstancesField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("responsibleAssist")
    private EmployeeField responsibleAssistField;
    @PropertyId("dealerManager")
    private EmployeeField dealerManagerField;
    @PropertyId("comments")
    private CommentsField<SaleComment> commentsField;
    @PropertyId("privateComments")
    private PrivateCommentsField<SalePrivateComment> privateCommentsField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;
    // источник лида
    @PropertyId("source")
    private LeadSourceSelect sourceField;

    public SaleEditForm(final Sale sale) {
        super(sale.isNew() ? "Ввод новой продажи в систему" :
                MessageFormat.format("Редактирование продажи № {0}", sale.getNum()), sale);
        setWinWidth(930, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {

        final TabSheet tab = new TabSheet();
        tab.addTab(createMainForm(), "Данные продажи");

        ////////////////////////////////////////////////////////////////////////////
        if (lookup(UserManagementService.class).isPermitPrivateComments()) {
            privateCommentsField = new PrivateCommentsField<>(SalePrivateComment.class);
            privateCommentsField.addValueChangeListener(forceModified);
            tab.addTab(privateCommentsField, "Закрытые комментарии");
        }

        return tab;
    }

    private FormLayout createMainForm() {
        final FormLayout form = new ExtaFormLayout();
//        form.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Клиент"));
        clientField = new ClientField("Клиент", "Введите имя клиента");
        clientField.setRequired(true);
        clientField.setRequiredError("Имя контакта не может быть пустым.");
        clientField.addValueChangeListener(e -> {
            final Client client = (Client) e.getProperty().getValue();
            if(client instanceof LegalEntity) {
                clientContactField.changeCompany();
                clientContactField.setVisible(true);
            } else
                clientContactField.setVisible(false);

        });
        form.addComponent(clientField);

        clientContactField = new EmployeeField("Контактное лицо", "Укажите контактное лицо клиента");
        clientContactField.setVisible(false);
        clientContactField.setCompanySupplier(() -> {
            final Client client = clientField.getValue();
            if(client instanceof LegalEntity)
                return ((LegalEntity)client).getCompany();
            else
                return null;
        });
        form.addComponent(clientContactField);

        sourceField = new LeadSourceSelect();
        form.addComponent(sourceField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Техника"));
        motorInstancesField = new MotorInstancesField("Техника", () -> new SaleMotor(getEntity()));
        form.addComponent(motorInstancesField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Дилер"));
        dealerField = new DealerSalePointField("Мотосалон", "Введите точку продаж");
        dealerField.setRequired(true);
        dealerField.addValueChangeListener(e -> {
            productInstancesField.refreshSalePoint();
            dealerManagerField.changeSalePoint();
            dealerLEField.changeSalePoint();
            if (responsibleField.getValue() == null) {
                final SalePoint sp = dealerField.getValue();
                final CuratorsGroup curatorsGroup = sp.getCuratorsGroup();
                if (curatorsGroup != null && !curatorsGroup.getCurators().isEmpty()) {
                    final Iterator<Employee> employeeIterator = curatorsGroup.getCurators().iterator();
                    responsibleField.setValue(employeeIterator.next());
                    if (employeeIterator.hasNext())
                        responsibleAssistField.setValue(employeeIterator.next());
                }
            }
        });
        form.addComponent(dealerField);

        dealerLEField = new SPLegalEntityField("Юр. лицо", "Укажите юридическое лицо дилера осуществляющее продажу");
        dealerLEField.setSalePointSupplier(() -> dealerField.getValue());
        dealerLEField.addValidator(value -> {
            if(value != null && value instanceof LegalEntity) {
                final LegalEntity legalEntity = (LegalEntity) value;
                // Проверить что юрик работает с этим брендом
                final List<String> brands = motorInstancesField.getBrands();
                if(!legalEntity.getMotorBrands().containsAll(brands))
                    throw new Validator.InvalidValueException("Выбранное Юридическое лицо не работает с данным брендом техники");
                // Проверить что юрик аккредитован для данного продукта
                final List<ProductInstance> productInstanceList = productInstancesField.getValue();
                if(productInstanceList != null) {
                    for (final ProductInstance prodInSale : productInstanceList) {
                        final Product prod = prodInSale.getProduct();
                        if(prod instanceof ProdCredit) {
                            if( !legalEntity.getCredProducts().contains(prod))
                                throw new Validator.InvalidValueException(
                                        MessageFormat.format("Выбранное Юридическое лицо не аккредитовано для продукта ''{0}''", prod.getName()));
                        }
                    }
                }
            }
        });
        form.addComponent(dealerLEField);

        dealerManagerField = new DealerEmployeeField("Менеджер", "Выберите или введите ответственного менеджера со стороны дилера");
        dealerManagerField.setSalePointSupplier(() -> dealerField.getValue());
        form.addComponent(dealerManagerField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Ответственные"));
        responsibleField = new EAEmployeeField("Ответственный", "Выберите или введите ответственного менеджера");
        responsibleField.setRequired(true);
        form.addComponent(responsibleField);

        responsibleAssistField = new EAEmployeeField("Заместитель", "Выберите или введите заместителя ответственного менеджера");
        form.addComponent(responsibleAssistField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Дополнительно"));
        commentField = new TextArea("Примечание");
        commentField.setRows(3);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Продукты"));
        productInstancesField = new ProductInstancesField("Продукты в продаже", null, getEntity(),
                () -> motorInstancesField.getTotalPrice(),
                () -> motorInstancesField.getBrands(),
                () -> dealerField.getValue());
        productInstancesField.addValueChangeListener(forceModified);
        form.addComponent(productInstancesField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Коментарии"));
        commentsField = new CommentsField<>(SaleComment.class);
        commentsField.addValueChangeListener(forceModified);
        form.addComponent(commentsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(SaleFileContainer.class);
        form.addComponent(docFilesEditor);

        motorInstancesField.addValueChangeListener(e -> productInstancesField.markAsDirtyRecursive());
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final Sale sale) {
        if (sale.isNew()) {
            sale.setStatus(Sale.Status.NEW);
            final UserManagementService userService = lookup(UserManagementService.class);
            final Employee user = userService.getCurrentUserEmployee();
            if (user != null) {
                final EmployeeRepository employeeRepository = lookup(EmployeeRepository.class);
                if (employeeRepository.isEAEmployee(user)) {
                    // Для сотрудника ЕА:
                    // * Устанавливаем ТТ которую курирует сотрудник
                    final SalePointRepository salePointRepository = lookup(SalePointRepository.class);
                    salePointRepository.findByCurator(user)
                            .stream().findFirst().ifPresent(sp -> sale.setDealer(sp));
                    // * Ответственный - сотрудник вводящий лид.
                    sale.setResponsible(user);
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - пусто.
                    // * Источник лида
                    sale.setSource(LeadSourceSelect.EA_MANAGER);
                } else if (employeeRepository.isDealerEmployee(user)) {
                    // Для сотрудника дилера:
                    final SalePoint salePoint = user.getWorkPlace();
                    if (salePoint != null) {
                        // * Устанавливаем ТТ сотрудника дилера
                        sale.setDealer(salePoint);
                        // * Ответственный - сотрудник ЕА который курирует данную торговую точку дилера.
                        final CuratorsGroup curatorsGroup = salePoint.getCuratorsGroup();
                        if (curatorsGroup != null && !curatorsGroup.getCurators().isEmpty())
                            sale.setResponsible(curatorsGroup.getCurators().iterator().next());
                    }
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - сотрудник который вводит лид.
                    sale.setDealerManager(user);
                    // * Источник лида
                    sale.setSource(LeadSourceSelect.DEALER_MANAGER);
                } else if (employeeRepository.isCallcenterEmployee(user)) {
                    // Для сотрудника колл-центра:
                    // * Ответственный - сотрудник ЕА который курирует торговую точку дилера (если определена).
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - пусто.
                    // * Источник лида
                    sale.setSource(LeadSourceSelect.CLIENT_CALL);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Sale saveEntity(Sale sale) {
        final SaleRepository leadService = lookup(SaleRepository.class);
        sale = leadService.secureSave(sale);
        if (sale.getNum() == null)
            evictCache(sale);
        NotificationUtil.showSuccess("Продажа сохранена");
        return sale;
    }


}
