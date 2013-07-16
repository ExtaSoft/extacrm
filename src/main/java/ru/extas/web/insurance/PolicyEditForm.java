/**
 * 
 */
package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;
import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;
import ru.extas.web.commons.AbstractEditForm;
import ru.extas.web.commons.component.DateTimeField;
import ru.extas.web.commons.component.EditField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

/**
 * Ввод редактирование полиса в рамках БСО
 * 
 * @author Valery Orlov
 * 
 */
public class PolicyEditForm extends AbstractEditForm<Policy> {

	private static final long serialVersionUID = 44314371625923505L;

	// Компоненты редактирования
	@PropertyId("regNum")
	private TextField regNumField;
	@PropertyId("bookTime")
	private PopupDateField bookTimeField;
	@PropertyId("issueDate")
	private PopupDateField issueDateField;

	/**
	 * @param caption
	 * @param obj
	 */
	public PolicyEditForm(String caption, Policy obj) {
		super(caption, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#initObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void initObject(Policy obj) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.web.commons.AbstractEditForm#saveObject(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void saveObject(Policy obj) {
		final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
		policyRepository.persist(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#checkBeforeSave(ru.extas.model.
	 * AbstractExtaObject)
	 */
	@Override
	protected void checkBeforeSave(Policy obj) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ru.extas.web.commons.AbstractEditForm#createEditFields(ru.extas.model
	 * .AbstractExtaObject)
	 */
	@Override
	protected FormLayout createEditFields(Policy obj) {
		FormLayout form = new FormLayout();

		regNumField = new EditField("Номер полиса", "Введите номер полиса.");
		regNumField.setColumns(20);
		regNumField.setRequired(true);
		form.addComponent(regNumField);

		bookTimeField = new DateTimeField("Полис забронирован", "Введите дату бронирования");
		bookTimeField.setWidth(15, Unit.EM);
		bookTimeField.setDateFormat("dd.MM.yyyy HH:mm:ss");
		form.addComponent(bookTimeField);

		issueDateField = new DateTimeField("Дата реализации", "Введите дату оформления полиса");
		issueDateField.setWidth(15, Unit.EM);
		form.addComponent(issueDateField);

		return form;
	}

}
