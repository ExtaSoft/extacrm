package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import ru.extas.model.Sale;
import ru.extas.server.SaleRegistry;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.CompanySelect;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;
import ru.extas.web.util.ComponentUtil;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 */
public class SaleEditForm extends AbstractEditForm<Sale> {

	private static final long serialVersionUID = 9510268415882116L;
	// Компоненты редактирования
	// Имя клиента
	@PropertyId("client")
	private PersonSelect clientField;
	@PropertyId("type")
	private ComboBox typeField;
	// Продавец (банк, страх. компания)
	@PropertyId("vendor")
	private CompanySelect vendorField;
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
	private CompanySelect dealerField;
	@PropertyId("comment")
	private TextArea commentField;
	@PropertyId("productInSales")
	private ProductInSaleGrid productInSaleField;

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

		clientField = new PersonSelect("Клиент", "Введите имя клиента");
		clientField.setRequired(true);
		clientField.setRequiredError("Имя контакта не может быть пустым.");
		form.addComponent(clientField);

		typeField = new ComboBox("Тип");
		typeField.setDescription("Укажите пол контакта");
		typeField.setRequired(true);
		typeField.setNullSelectionAllowed(false);
		typeField.setNewItemsAllowed(false);
		ComponentUtil.fillSelectByEnum(typeField, Sale.Type.class);
		form.addComponent(typeField);

		vendorField = new CompanySelect("Поставщик");
		form.addComponent(vendorField);

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

		dealerField = new CompanySelect("Мотосалон");
		form.addComponent(dealerField);

		commentField = new TextArea("Комментарий");
		commentField.setColumns(25);
		commentField.setNullRepresentation("");
		form.addComponent(commentField);

		productInSaleField = new ProductInSaleGrid("Продукты в продаже", obj);
		form.addComponent(productInSaleField);

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
			obj.setType(Sale.Type.CREDIT);
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
		final SaleRegistry leadService = lookup(SaleRegistry.class);
		leadService.save(obj);
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
