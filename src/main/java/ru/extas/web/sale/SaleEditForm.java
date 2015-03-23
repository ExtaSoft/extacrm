package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.SaleComment;
import ru.extas.model.sale.SaleFileContainer;
import ru.extas.server.contacts.EmployeeRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.CommentsField;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.contacts.ClientField;
import ru.extas.web.contacts.employee.DealerEmployeeField;
import ru.extas.web.contacts.employee.EAEmployeeField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.contacts.salepoint.SalePointField;
import ru.extas.web.motor.MotorBrandSelect;
import ru.extas.web.motor.MotorModelSelect;
import ru.extas.web.motor.MotorTypeSelect;

import java.math.BigDecimal;
import java.text.MessageFormat;

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
    // Тип техники
    @PropertyId("motorType")
    private MotorTypeSelect motorTypeField;
    // Марка техники
    @PropertyId("motorBrand")
    private MotorBrandSelect motorBrandField;
    // Модель техники
    @PropertyId("motorModel")
    private MotorModelSelect motorModelField;
    // Стоимость техники
    @PropertyId("motorPrice")
    private EditField mototPriceField;
    // Мотосалон
    @PropertyId("dealer")
    private SalePointField dealerField;
    @PropertyId("comment")
    private TextArea commentField;
    @PropertyId("productInSales")
    private ProductInSaleField productInSaleField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("responsibleAssist")
    private EmployeeField responsibleAssistField;
    @PropertyId("dealerManager")
    private EmployeeField dealerManagerField;
    @PropertyId("comments")
    private CommentsField<SaleComment> commentsField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;

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
        final FormLayout form = new ExtaFormLayout();
//        form.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Клиент"));
        clientField = new ClientField("Клиент", "Введите имя клиента");
        clientField.setRequired(true);
        clientField.setRequiredError("Имя контакта не может быть пустым.");
        form.addComponent(clientField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Техника"));
        motorTypeField = new MotorTypeSelect();
        motorTypeField.setRequired(true);
        form.addComponent(motorTypeField);

        motorBrandField = new MotorBrandSelect();
        motorBrandField.setRequired(true);
        motorBrandField.linkToType(motorTypeField);
        form.addComponent(motorBrandField);

        motorModelField = new MotorModelSelect("Модель техники");
        motorModelField.setRequired(true);
        motorModelField.linkToTypeAndBrand(motorTypeField, motorBrandField);
        if(getEntity().getMotorModel() != null)
            motorModelField.addItem(getEntity().getMotorModel());
        form.addComponent(motorModelField);

        mototPriceField = new EditField("Цена техники");
        mototPriceField.setRequired(true);
        form.addComponent(mototPriceField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Дилер"));
        dealerField = new SalePointField("Мотосалон", "Введите точку продаж");
        dealerField.setRequired(true);
        dealerField.addValueChangeListener(e -> {
            dealerManagerField.changeSalePoint();
            if (responsibleField.getValue() == null) {
                SalePoint sp = dealerField.getValue();
                if (sp.getCurator() != null)
                    responsibleField.setValue(sp.getCurator());
            }
        });
        form.addComponent(dealerField);

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
        productInSaleField = new ProductInSaleField("Продукты в продаже", getEntity(),
                () -> (BigDecimal) mototPriceField.getConvertedValue(),
                () -> (String) motorBrandField.getValue());
//        productInSaleField.setRequired(true);
        form.addComponent(productInSaleField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Коментарии"));
        commentsField = new CommentsField<>(SaleComment.class);
        form.addComponent(commentsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(SaleFileContainer.class);
        form.addComponent(docFilesEditor);

        mototPriceField.addValueChangeListener(e -> productInSaleField.markAsDirtyRecursive());

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
                } else if (employeeRepository.isDealerEmployee(user)) {
                    // Для сотрудника дилера:
                    final SalePoint salePoint = user.getWorkPlace();
                    if (salePoint != null) {
                        // * Устанавливаем ТТ сотрудника дилера
                        sale.setDealer(salePoint);
                        // * Ответственный - сотрудник ЕА который курирует данную торговую точку дилера.
                        if (salePoint.getCurator() != null)
                            sale.setResponsible(salePoint.getCurator());
                    }
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - сотрудник который вводит лид.
                    sale.setDealerManager(user);
                } else if (employeeRepository.isCallcenterEmployee(user)) {
                    // Для сотрудника колл-центра:
                    // * Ответственный - сотрудник ЕА который курирует торговую точку дилера (если определена).
                    // * Заместитель - пусто.
                    // * Менеджер(дилер) - пусто.
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
