/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import ru.extas.model.A7Form;
import ru.extas.model.UserRole;
import ru.extas.server.A7FormService;
import ru.extas.server.UserManagementService;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.GridDataDecl;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
public class A7FormGrid extends CustomComponent {

    private static final long serialVersionUID = 6290106109723378415L;
    private final Table table;
    private final ExtaDataContainer<A7Form> container;

    public A7FormGrid() {

        // Запрос данных
        container = new ExtaDataContainer<>(A7Form.class);
        container.addNestedContainerProperty("owner.name");
        final Subject subject = SecurityUtils.getSubject();
        // пользователю доступны только собственные записи
        if (subject.hasRole(UserRole.USER.getName())) {
            UserManagementService userService = lookup(UserManagementService.class);
            container.addContainerFilter(new Compare.Equal("owner", userService.getCurrentUserContact()));
        }

        table = new Table();
        table.setContainerDataSource(container);
        table.setSizeFull();

        final GridDataDecl ds = new A7FormDataDecl();
        ds.initTableColumns(table);

        final Button toLostBtn = new Button("Утрачен", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final A7Form.Status status = A7Form.Status.LOST;
                changeStatus(status);
            }
        });
        toLostBtn.setDescription("Перевести выделенный в списке бланк в \"Утраченные\"");
        toLostBtn.setEnabled(false);

        final Button toBrokenBtn = new Button("Испорчен", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final A7Form.Status status = A7Form.Status.BROKEN;
                changeStatus(status);
            }
        });
        toBrokenBtn.setDescription("Перевести выделенный в списке бланк в \"Испорченные\"");
        toBrokenBtn.setEnabled(false);

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);
        table.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                toBrokenBtn.setEnabled(enableBtb);
                toLostBtn.setEnabled(enableBtb);
            }
        });

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);
        commandBar.addComponent(toLostBtn);
        commandBar.addComponent(toBrokenBtn);

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();
        panel.addComponent(commandBar);
        panel.addComponent(table);

        setCompositionRoot(panel);
    }

    private void changeStatus(A7Form.Status status) {
        final Object curObjId = checkNotNull(table.getValue(), "No selected row");
        final A7Form curObj = ((EntityItem<A7Form>) table.getItem(curObjId)).getEntity();

        A7FormService formService = lookup(A7FormService.class);
        formService.changeStatus(curObj, status);
        container.refreshItem(curObjId);
    }
}
