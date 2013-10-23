package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.Sale;
import ru.extas.server.SaleService;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.EmailField;
import ru.extas.web.commons.component.PhoneField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 */
public class SaleEditForm extends AbstractEditForm<Sale> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    // Имя контакта
    @PropertyId("contactName")
    private EditField contactField;
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
    @PropertyId("comment")
    private TextArea commentField;

    public SaleEditForm(final String caption, final BeanItem<Sale> obj) {
        super(caption, obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.web.commons.window.AbstractEditForm#createEditFields(ru.extas.model
     * .AbstractExtaObject)
     */
    @Override
    protected ComponentContainer createEditFields(final Sale obj) {
        final FormLayout form = new FormLayout();

        contactField = new EditField("Клиент", "Введите имя клиента");
        contactField.setColumns(25);
        contactField.setRequired(true);
        contactField.setRequiredError("Имя контакта не может быть пустым.");
        form.addComponent(contactField);

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
    protected void initObject(final Sale obj) {
        if (obj.getId() == null) {
            obj.setStatus(Sale.Status.NEW);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.web.commons.window.AbstractEditForm#saveObject(ru.extas.model.
     * AbstractExtaObject)
     */
    @Override
    protected void saveObject(final Sale obj) {
        final SaleService leadService = lookup(SaleService.class);
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
    protected void checkBeforeSave(final Sale obj) {
    }

}
