package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.Sale;
import ru.extas.server.SaleRegistry;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;
import ru.extas.web.contacts.PersonSelect;
import ru.extas.web.contacts.SalePointSelect;
import ru.extas.web.reference.MotorBrandSelect;
import ru.extas.web.reference.MotorTypeSelect;
import ru.extas.web.reference.RegionSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования лида
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class SaleEditForm extends AbstractEditForm<Sale> {

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

	/**
	 * <p>Constructor for SaleEditForm.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param obj a {@link com.vaadin.data.util.BeanItem} object.
	 */
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
	/** {@inheritDoc} */
	@Override
	protected ComponentContainer createEditFields(final Sale obj) {
		final FormLayout form = new FormLayout();

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

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.web.commons.window.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	/** {@inheritDoc} */
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
	/** {@inheritDoc} */
	@Override
	protected void saveObject(final Sale obj) {
		final SaleRegistry leadService = lookup(SaleRegistry.class);
		leadService.save(obj);
		Notification.show("Продажа сохранена", Notification.Type.TRAY_NOTIFICATION);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.extas.web.commons.window.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	/** {@inheritDoc} */
	@Override
	protected void checkBeforeSave(final Sale obj) {
	}

}
