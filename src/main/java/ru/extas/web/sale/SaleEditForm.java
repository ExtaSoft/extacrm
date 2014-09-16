package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.sale.Sale;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.contacts.SalePointSelect;
import ru.extas.web.motor.MotorBrandSelect;
import ru.extas.web.motor.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import javax.persistence.EntityManager;

import static org.springframework.util.CollectionUtils.isEmpty;
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
    @PropertyId("dealer")
    private SalePointSelect dealerField;
    @PropertyId("comment")
    private TextArea commentField;
    @PropertyId("productInSales")
    private ProductInSaleGrid productInSaleField;

    public SaleEditForm(Sale sale) {
        super(sale.isNew() ? "Ввод новой продажи в систему" : "Редактирование продажи", new BeanItem(sale));
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final Sale obj) {
        final FormLayout form = new ExtaFormLayout();

        clientField = new PersonSelect("Клиент", "Введите имя клиента");
        clientField.setRequired(true);
        clientField.setRequiredError("Имя контакта не может быть пустым.");
        form.addComponent(clientField);

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

        dealerField = new SalePointSelect("Мотосалон", "Введите точку продаж", null);
        form.addComponent(dealerField);

        commentField = new TextArea("Комментарий");
        commentField.setColumns(25);
        commentField.setNullRepresentation("");
        form.addComponent(commentField);

        productInSaleField = new ProductInSaleGrid("Продукты в продаже", obj);
        form.addComponent(productInSaleField);

        return form;
    }


    /** {@inheritDoc} */
    @Override
    protected void initObject(final Sale obj) {
        if (obj.isNew()) {
            obj.setStatus(Sale.Status.NEW);
            UserManagementService userService = lookup(UserManagementService.class);
            Person user = userService.getCurrentUserContact();
            if(user != null) {
                if(!isEmpty(user.getWorkPlaces()))
                    obj.setDealer(user.getWorkPlaces().iterator().next());
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    protected Sale saveObject(final Sale obj) {
        final SaleRepository leadService = lookup(SaleRepository.class);
        Sale sale = leadService.secureSave(obj);
        if (sale.getNum() == null)
            lookup(EntityManager.class).getEntityManagerFactory().getCache().evict(sale.getClass(), sale.getId());
        NotificationUtil.showSuccess("Продажа сохранена");
        return sale;
    }


}
