package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.*;
import ru.extas.model.security.*;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ConfirmTabSheet;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.contacts.employee.BankEmployeeField;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.util.ComponentUtil;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
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
    protected void initObject(final SecuredObject obj) {

    }

    @Override
    protected SecuredObject saveObject(SecuredObject obj) {
        obj = lookup(UserManagementService.class).saveObjectAccess(obj, rule);
        NotificationUtil.showSuccess("Настройки доступа сохранены");
        return obj;
    }

    private void updateAccess(final RefreshBeanContainer<UserObjectAccess> container) {
        rule.getUsers().clear();
        for (final UserObjectAccess access : container.getItemIds()) {
            rule.getUsers().put(access.getUser(), access);
        }
        SecuritySettingsForm.this.setModified(true);
    }

    @Override
    protected ComponentContainer createEditFields(final SecuredObject obj) {
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Вкладка - "Пользователи"
        tabsheet.addTab(createUsersForm(obj), "Пользователи");
        return tabsheet;
    }

    private Component createUsersForm(final SecuredObject obj) {
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
                    protected void initObject(final UserObjectAccess obj) {
                        if(obj.isNew()){
                            obj.setRole(AccessRole.READER);
                            obj.setSecurityRule(rule);
                        }
                    }

                    @Override
                    protected UserObjectAccess saveObject(final UserObjectAccess obj) {
                        ((RefreshBeanContainer<UserObjectAccess>) container).addBean(obj);
                        updateAccess((RefreshBeanContainer<UserObjectAccess>) container);
                        return obj;
                    }

                    @Override
                    protected ComponentContainer createEditFields(final UserObjectAccess obj) {
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
                    public void fire(final Object itemId) {
                        container.removeItem(itemId);
                        updateAccess((RefreshBeanContainer<UserObjectAccess>) container);
                    }
                });

                return actions;
            }
        };
        return usersGrid;
    }
}
