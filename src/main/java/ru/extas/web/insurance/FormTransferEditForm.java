package ru.extas.web.insurance;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import ru.extas.model.insurance.FormTransfer;
import ru.extas.server.insurance.FormTransferRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.contacts.person.PersonSelect;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма ввода/редактирования имущественной страховки
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class FormTransferEditForm extends ExtaEditForm<FormTransfer> {

    private static final long serialVersionUID = 9510268415882116L;
    // Компоненты редактирования
    @PropertyId("fromContact")
    private EmployeeField fromContactField;
    @PropertyId("toContact")
    private EmployeeField toContactField;
    @PropertyId("transferDate")
    private PopupDateField transferDateField;
    @PropertyId("formNums")
    private A7NumListEdit formNums;

    public FormTransferEditForm(final FormTransfer formTransfer) {
        super(formTransfer.isNew() ?
                        "Новый акт приема/передачи" :
                        "Редактировать акт приема/передачи",
                formTransfer);
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final FormTransfer obj) {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

        // FIXME Ограничить выбор контакта только сотрудниками и СК
        fromContactField = new EmployeeField("От кого", "Сотрудник от которого приходят формы");
        fromContactField.setRequired(true);
        form.addComponent(fromContactField);

        // FIXME Ограничить выбор контакта только сотрудниками и СК
        toContactField = new EmployeeField("Кому", "Сотрудник которому передаются формы");
        toContactField.setRequired(true);
        form.addComponent(toContactField);

        transferDateField = new LocalDateField("Дата приема/передачи", "Введите дату акта приема/передачи");
        transferDateField.setRequired(true);
        form.addComponent(transferDateField);

        formNums = new A7NumListEdit("Номера квитанций");
        formNums.setRequired(true);
        formNums.setWidth(100, Unit.PERCENTAGE);
        form.addComponent(formNums);

        return form;
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final FormTransfer obj) {
        if (obj.isNew()) {
            final LocalDate now = LocalDate.now(lookup(DateTimeZone.class));
            obj.setTransferDate(now);
            // TODO: Инициализировать поле "От" текущим пользователем
        }
    }

    /** {@inheritDoc} */
    @Override
    protected FormTransfer saveObject(FormTransfer obj) {
        obj = lookup(FormTransferRepository.class).saveAndChangeOwner(obj);
        NotificationUtil.showSuccess("Акт приема/передачи сохранен");
        return obj;
    }

}
