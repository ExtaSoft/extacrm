package ru.extas.web.contacts;

import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * @author Valery Orlov
 *         Date: 21.10.2014
 *         Time: 22:52
 */
public class EmployeeSelectWindow extends CloseOnlylWindow {
    private Employee selected;
    private boolean selectPressed;

    public EmployeeSelectWindow(String caption, Company company) {
        super(caption);
        setWidth(800, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);
        setContent(new EmployeesGrid(company){
            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new DefaultAction("Выбрать", "Выбрать выделенного в списке сотрудника и закрыть окно", Fontello.CHECK) {
                    @Override
                    public void fire(final Object itemId) {
                        selected = extractBean(table.getItem(itemId));
                        selectPressed = true;
                        close();
                    }
                });

                actions.addAll(super.createActions());

                return actions;
            }

        });
    }

    public boolean isSelectPressed() {
        return selectPressed;
    }

    public Employee getSelected() {
        return selected;
    }
}
