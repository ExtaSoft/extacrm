/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import ru.extas.model.A7Form;
import ru.extas.model.UserRole;
import ru.extas.server.UserManagementService;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.GridDataDecl;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
public class A7FormGrid extends CustomComponent {

    private static final long serialVersionUID = 6290106109723378415L;

    public A7FormGrid() {

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Запрос данных
        final JPAContainer<A7Form> container = new ExtaDataContainer<>(A7Form.class);
        container.addNestedContainerProperty("owner.name");
        final Subject subject = SecurityUtils.getSubject();
        // пользователю доступны только собственные записи
        if (subject.hasRole(UserRole.USER.getName())) {
            UserManagementService userService = lookup(UserManagementService.class);
            container.addContainerFilter(new Compare.Equal("owner", userService.getCurrentUserContact()));
        }

        final Table table = new Table();
        table.setContainerDataSource(container);
        table.setSizeFull();

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);

        final GridDataDecl ds = new A7FormDataDecl();
        ds.initTableColumns(table);

        panel.addComponent(table);

        setCompositionRoot(panel);
    }
}
