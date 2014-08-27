package ru.extas.web.insurance;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import org.joda.time.LocalDate;
import ru.extas.model.insurance.FormTransfer;
import ru.extas.server.insurance.FormTransferRepository;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.contacts.PersonSelect;

import static com.google.common.base.Strings.isNullOrEmpty;
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
    private PersonSelect fromContactField;
    @PropertyId("toContact")
    private PersonSelect toContactField;
    @PropertyId("transferDate")
    private PopupDateField transferDateField;
    @PropertyId("formNums")
    private A7NumListEdit formNums;

    public FormTransferEditForm(FormTransfer formTransfer) {
        super(isNullOrEmpty(formTransfer.getId()) ?
                        "Новый акт приема/передачи" :
                        "Редактировать акт приема/передачи",
                new BeanItem(formTransfer));
    }

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final FormTransfer obj) {
        final FormLayout form = new ExtaFormLayout();

        // FIXME Ограничить выбор контакта только сотрудниками и СК
        fromContactField = new PersonSelect("От кого");
        fromContactField.setRequired(true);
        form.addComponent(fromContactField);

        // FIXME Ограничить выбор контакта только сотрудниками и СК
        toContactField = new PersonSelect("Кому");
        toContactField.setRequired(true);
        form.addComponent(toContactField);

        transferDateField = new LocalDateField("Дата приема/передачи", "Введите дату акта приема/передачи");
        transferDateField.setRequired(true);
        form.addComponent(transferDateField);

        formNums = new A7NumListEdit("Номера квитанций");
        formNums.setRequired(true);
        form.addComponent(formNums);

        return form;
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final FormTransfer obj) {
        if (obj.getId() == null) {
            final LocalDate now = LocalDate.now();
            obj.setTransferDate(now);
            // TODO: Инициализировать поле "От" текущим пользователем
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void saveObject(final FormTransfer obj) {
        lookup(FormTransferRepository.class).saveAndChangeOwner(obj);
        NotificationUtil.showSuccess("Акт приема/передачи сохранен");
    }

    /** {@inheritDoc} */
    @Override
    protected void checkBeforeSave(final FormTransfer obj) {
    }

}
