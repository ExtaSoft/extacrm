package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.contacts.Employee;
import ru.extas.model.insurance.InsuranceFileContainer;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.SaleComment;
import ru.extas.model.sale.SaleFileContainer;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.CommentsField;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FilesManageField;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.contacts.employee.*;
import ru.extas.web.contacts.person.PersonSelect;
import ru.extas.web.contacts.salepoint.SalePointField;
import ru.extas.web.motor.MotorBrandSelect;
import ru.extas.web.motor.MotorTypeSelect;

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
    private PersonSelect clientField;
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
    @PropertyId("dealer")
    private SalePointField dealerField;
    @PropertyId("comment")
    private TextArea commentField;
    @PropertyId("productInSales")
    private ProductInSaleGrid productInSaleField;
    @PropertyId("responsible")
    private EmployeeField responsibleField;
    @PropertyId("responsibleAssist")
    private EmployeeField responsibleAssistField;
    @PropertyId("dealerManager")
    private EmployeeField dealerManagerField;
    @PropertyId("bankManager")
    private EmployeeField bankManagerField;
    @PropertyId("comments")
    private CommentsField<SaleComment> commentsField;
    @PropertyId("files")
    private FilesManageField docFilesEditor;

    public SaleEditForm(final Sale sale) {
        super(sale.isNew() ? "Ввод новой продажи в систему" :
                MessageFormat.format("Редактирование продажи № {0}", sale.getNum()), sale);
        setWinWidth(770, Unit.PIXELS);
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final Sale obj) {
        final FormLayout form = new ExtaFormLayout();
//        form.setSizeFull();

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Клиент"));
        clientField = new PersonSelect("Клиент", "Введите имя клиента");
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
        form.addComponent(motorBrandField);

        motorModelField = new EditField("Модель техники", "Введите модель техники");
        motorModelField.setColumns(15);
        motorModelField.setRequired(true);
        form.addComponent(motorModelField);

        mototPriceField = new EditField("Цена техники");
        mototPriceField.setRequired(true);
        form.addComponent(mototPriceField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Дилер"));
        dealerField = new SalePointField("Мотосалон", "Введите точку продаж");
        dealerField.setRequired(true);
        dealerField.addValueChangeListener(e -> dealerManagerField.changeSalePoint());
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
        productInSaleField = new ProductInSaleGrid("Продукты в продаже", obj);
        productInSaleField.setRequired(true);
        form.addComponent(productInSaleField);

        bankManagerField = new BankEmployeeField("Менеджер банка", "Выберите или введите ответственного менеджера со стороны банка");
        form.addComponent(bankManagerField);

        ////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Коментарии"));
        commentsField = new CommentsField<>(SaleComment.class);
        form.addComponent(commentsField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Документы"));
        docFilesEditor = new FilesManageField(SaleFileContainer.class);
        form.addComponent(docFilesEditor);

        return form;
    }


    /** {@inheritDoc} */
    @Override
    protected void initObject(final Sale obj) {
        if (obj.isNew()) {
            obj.setStatus(Sale.Status.NEW);
            final UserManagementService userService = lookup(UserManagementService.class);
            final Employee user = userService.getCurrentUserEmployee();
            if(user != null) {
                if(user.getWorkPlace() != null)
                    obj.setDealer(user.getWorkPlace());
            }
            final Employee userContact = lookup(UserManagementService.class).getCurrentUserEmployee();
            obj.setResponsible(userContact);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected Sale saveObject(final Sale obj) {
        final SaleRepository leadService = lookup(SaleRepository.class);
        final Sale sale = leadService.secureSave(obj);
        if (sale.getNum() == null)
            evictCache(sale);
        NotificationUtil.showSuccess("Продажа сохранена");
        return sale;
    }


}
