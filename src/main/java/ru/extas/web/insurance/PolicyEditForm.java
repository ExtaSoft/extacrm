/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import ru.extas.model.insurance.Policy;
import ru.extas.server.insurance.PolicyRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.DateTimeField;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Ввод редактирование полиса в рамках БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PolicyEditForm extends ExtaEditForm<Policy> {

    private static final long serialVersionUID = 44314371625923505L;

    // Компоненты редактирования
    @PropertyId("regNum")
    private TextField regNumField;
    @PropertyId("bookTime")
    private PopupDateField bookTimeField;
    @PropertyId("issueDate")
    private PopupDateField issueDateField;

    public PolicyEditForm(final Policy policy) {
        super(policy.isNew() ?
                "Новый бланк" :
                "Редактировать бланк", policy);
    }

    /** {@inheritDoc} */
    @Override
    protected void initEntity(final Policy policy) {

    }

    /** {@inheritDoc} */
    @Override
    protected Policy saveEntity(Policy policy) {
        final PolicyRepository policyRepository = lookup(PolicyRepository.class);
        policy = policyRepository.save(policy);
        NotificationUtil.showSuccess("Бланк сохранен");
        return policy;
    }


    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields() {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

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
