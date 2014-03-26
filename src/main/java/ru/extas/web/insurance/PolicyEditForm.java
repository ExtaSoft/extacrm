/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.insurance.Policy;
import ru.extas.server.insurance.PolicyRegistry;
import ru.extas.web.commons.component.DateTimeField;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.window.AbstractEditForm;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Ввод редактирование полиса в рамках БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
     * <p>Constructor for PolicyEditForm.</p>
     *
     * @param caption   a {@link java.lang.String} object.
     * @param editedObj a {@link com.vaadin.data.util.BeanItem} object.
     */
    public PolicyEditForm(final String caption, final BeanItem<Policy> editedObj) {
        super(caption, editedObj);
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final Policy obj) {

    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(final Policy obj) {
        final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
        policyRepository.save(obj);
        Notification.show("Бланк сохранен", Notification.Type.TRAY_NOTIFICATION);
    }


    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final Policy obj) {
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final Policy obj) {
        final FormLayout form = new FormLayout();

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
