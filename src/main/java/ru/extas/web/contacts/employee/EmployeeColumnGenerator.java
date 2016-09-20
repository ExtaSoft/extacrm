package ru.extas.web.contacts.employee;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import ru.extas.model.contacts.Employee;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.GridDataDecl;

/**
 * Показывает сотрудника как ссылку в ячейке таблици
 * <p>
 * Created by valery on 15.09.16.
 */
public class EmployeeColumnGenerator extends GridDataDecl.ComponentColumnGenerator {

    private final String employeePropId;

    public EmployeeColumnGenerator(final String employeePropId) {
        super();
        this.employeePropId = employeePropId;
    }

    @Override
    public Object generateCell(final Object columnId, final Item item, final Object itemId) {
        final Employee employee = (Employee) item.getItemProperty(employeePropId).getValue();
        final Button link = new Button();
        link.addStyleName(ExtaTheme.BUTTON_LINK);
        if (employee != null) {
            link.setCaption(employee.getName());
            link.addClickListener(event -> {
                final EmployeeEditForm editWin = new EmployeeEditForm(employee);
//                    editWin.setReadOnly(true);
                FormUtils.showModalWin(editWin);
            });
            return link;
        } else {
            return null;
        }
    }

}
