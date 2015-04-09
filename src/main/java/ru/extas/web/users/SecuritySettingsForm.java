package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.*;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.ObjectSecurityRule;
import ru.extas.model.security.SecuredObject;
import ru.extas.model.security.UserObjectAccess;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.container.RefreshBeanContainer;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.util.ComponentUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма просмотра и редактирования прав доступа на конкретный объект
 *
 * @author Valery Orlov
 *         Date: 10.11.2014
 *         Time: 3:08
 */
public class SecuritySettingsForm extends ExtaEditForm<SecuredObject> {

    private final ObjectSecurityRule rule;
    private ExtaGrid<UserObjectAccess> usersGrid;

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *  @param caption a {@link String} object.
     * @param bean
     */
    public SecuritySettingsForm(final String caption, final SecuredObject bean) {
        super(caption, bean);

        rule = Optional.ofNullable(bean.getSecurityRule()).orElse(new ObjectSecurityRule());

        setWinHeight(500, Unit.PIXELS);
    }

    @Override
    protected void initEntity(final SecuredObject securedObject) {

    }

    @Override
    protected SecuredObject saveEntity(SecuredObject securedObject) {
        securedObject = lookup(UserManagementService.class).saveObjectAccess(securedObject, rule);
        NotificationUtil.showSuccess("Настройки доступа сохранены");
        return securedObject;
    }

    private void updateAccess(final RefreshBeanContainer<UserObjectAccess> container) {
        rule.getUsers().clear();
        for (final UserObjectAccess access : container.getItemIds()) {
            rule.getUsers().put(access.getUser(), access);
        }
        SecuritySettingsForm.this.setModified(true);
    }

    @Override
    protected ComponentContainer createEditFields() {
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Вкладка - "Пользователи"
        tabsheet.addTab(createUsersForm(), "Пользователи");
        return tabsheet;
    }

    private Component createUsersForm() {
        usersGrid = new ExtaGrid<UserObjectAccess>(UserObjectAccess.class) {

            @Override
            public ExtaEditForm<UserObjectAccess> createEditForm(final UserObjectAccess userObjectAccess, final boolean isInsert) {
                return new ExtaEditForm<UserObjectAccess>("Доступ пользователя к объекту", userObjectAccess) {

                    {
                        setWinWidth(500, Unit.PIXELS);
                        setWinHeight(250, Unit.PIXELS);
                    }

                    @PropertyId("role")
                    public OptionGroup roleField;
                    @PropertyId("user")
                    private EmployeeField employeeField;

                    @Override
                    protected void initEntity(final UserObjectAccess objectAccess) {
                        if(objectAccess.isNew()){
                            objectAccess.setRole(AccessRole.READER);
                            objectAccess.setSecurityRule(rule);
                        }
                    }

                    @Override
                    protected UserObjectAccess saveEntity(final UserObjectAccess objectAccess) {
                        ((RefreshBeanContainer<UserObjectAccess>) container).addBean(objectAccess);
                        updateAccess((RefreshBeanContainer<UserObjectAccess>) container);
                        return objectAccess;
                    }

                    @Override
                    protected ComponentContainer createEditFields() {
                        final FormLayout formLayout = new ExtaFormLayout();
                        formLayout.setMargin(true);
                        formLayout.setSizeFull();

                        employeeField = new EmployeeField("Сотрудник",
                                "Укажите сотрудника которому необходимо предоставить доступ");
                        employeeField.setRequired(true);
                        formLayout.addComponent(employeeField);

                        roleField = new OptionGroup("Уровень доступа");
                        roleField.setDescription("Укажите уровень доступа пользователя к объекту");
                        roleField.setNullSelectionAllowed(false);
                        roleField.setRequired(true);
                        //roleField.setWidth(25, Unit.EM);
                        ComponentUtil.fillSelectByEnum(roleField, AccessRole.class);
                        formLayout.addComponent(roleField);

                        return formLayout;
                    }
                };
            }

            @Override
            protected GridDataDecl createDataDecl() {
                return new GridDataDecl(){{
                    addMapping("user.name", "Имя сотрудника");
                    addMapping("user.company.name", "Компания");
                    addMapping("role", "Уровень доступа");
                    super.addDefaultMappings();
                }};
            }

            @Override
            protected Container createContainer() {
                final RefreshBeanContainer<UserObjectAccess> cnt =
                        new RefreshBeanContainer<>(UserObjectAccess.class, rule.getUsers().values());
                cnt.addNestedContainerProperty("user.name");
                cnt.addNestedContainerProperty("user.company.name");
                return cnt;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new NewObjectAction("Добавить", "Дать доступ к объекту новому пользователю", Fontello.USER_ADD));
                actions.add(new EditObjectAction("Изменить", "Изменить уроветь доступа пользователя к объекту", Fontello.USER_1));
                actions.add(new ItemAction("Удалить", "Удалить доступ пользователю", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> container.removeItem(id));
                        updateAccess((RefreshBeanContainer<UserObjectAccess>) container);
                    }
                });

                return actions;
            }
        };
        return usersGrid;
    }
}
