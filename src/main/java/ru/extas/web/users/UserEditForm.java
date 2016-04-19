/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import ru.extas.model.security.UserProfile;
import ru.extas.model.security.UserRole;
import ru.extas.security.UserRealm;
import ru.extas.server.security.UserRegistry;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.contacts.employee.EmployeeField;
import ru.extas.web.contacts.salepoint.SalePointsSelectField;
import ru.extas.web.motor.MotorBrandMultiselect;
import ru.extas.web.reference.RegionMultiselect;
import ru.extas.web.util.ComponentUtil;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>UserEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class UserEditForm extends ExtaEditForm<UserProfile> {

    private static final long serialVersionUID = -5016687382646391930L;
    private final static Logger logger = LoggerFactory.getLogger(UserEditForm.class);
    private final String initialPassword;
    // Компоненты редактирования
    @PropertyId("employee")
    private EmployeeField nameField;
    @PropertyId("login")
    private TextField loginField;
    @PropertyId("role")
    private ComboBox roleField;
    @PropertyId("blocked")
    private CheckBox blockedField;
    @PropertyId("changePassword")
    private CheckBox changePasswordField;
    @PropertyId("password")
    private PasswordField passField;
    private PasswordField passConfField;

    @PropertyId("permitRegions")
    private RegionMultiselect regionsField;
    @PropertyId("permitBrands")
    private MotorBrandMultiselect brandsField;

    @PropertyId("groupList")
    private UserGroupField groupField;
    @PropertyId("permissions")
    private ExtaPermissionField permissionsField;

    @PropertyId("salePoints")
    private SalePointsSelectField salePointsField;

    public UserEditForm(final UserProfile userProfile) {
        super(userProfile.isNew() ?
                        "Ввод нового пользователя в систему" :
                        String.format("Редактирование данных пользователя: %s", userProfile.getEmployee().getName()),
                userProfile);
        initialPassword = userProfile.getPassword();

        setWinWidth(800, Unit.PIXELS);
        setWinHeight(600, Unit.PIXELS);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initEntity(final UserProfile profile) {
        if (profile.isNew()) {
            // Инициализируем новый объект
            profile.setRole(UserRole.USER);
            profile.setChangePassword(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UserProfile saveEntity(UserProfile profile) {
        logger.debug("Saving user profile...");
        securePassword(profile);
        final UserRegistry userService = lookup(UserRegistry.class);
        profile = userService.save(profile);
        lookup("cacheManager", CacheManager.class).getCache("userByLogin").evict(profile.getLogin());
        NotificationUtil.showSuccess("Пользователь сохранен");
        return profile;
    }

    /**
     * @param user
     */
    private void securePassword(final UserProfile user) {
        // Проверить менялся ли пароль
        if (!passField.getValue().equals(initialPassword)) {
            UserRealm.securePassword(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields() {
        final TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeFull();

        // Вкладка - "Общая информация"
        final FormLayout mainTab = getMainTab(getEntity());
        tabsheet.addTab(mainTab).setCaption("Общие данные");

        // Вкладка - "Группы"
        groupField = new UserGroupField();
        groupField.setSizeFull();
        tabsheet.addTab(groupField, "Группы");

        // Вкладка - "Права доступа"
        final Component permissionTab = createPermissionTab(getEntity());
        tabsheet.addTab(permissionTab).setCaption("Права доступа");

        // Вкладка - "Доступные торговые точки"
        salePointsField = new SalePointsSelectField();
        salePointsField.setSizeFull();
        tabsheet.addTab(salePointsField, "Доступные торговые точки");

        return tabsheet;
    }

    private Component createPermissionTab(final UserProfile userProfile) {
        final FormLayout form = new ExtaFormLayout();
        form.setMargin(true);
        form.setSizeFull();

        brandsField = new MotorBrandMultiselect("Доступные бренды");
        form.addComponent(brandsField);

        regionsField = new RegionMultiselect("Доступные регионы");
        form.addComponent(regionsField);

        permissionsField = new ExtaPermissionField(userProfile);
        permissionsField.setWidth(100, Unit.PERCENTAGE);
        permissionsField.setCaption("Правила доступа пользователя");
        form.addComponent(permissionsField);

        return form;
    }

    private FormLayout getMainTab(final UserProfile userProfile) {
        final FormLayout form = new ExtaFormLayout();
        form.setMargin(true);
        form.setSizeFull();

        nameField = new EmployeeField("Имя сотрудника", "Введите или выберете сотрудника которому предоставляется доступ к системе");
        nameField.setRequired(true);
        nameField.setRequiredError("Сотрудник обязательно должен быть указан!");
        form.addComponent(nameField);

        // FIXME Проверить уникальность логина
        loginField = new TextField("Логин (e-mail)");
//        loginField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        loginField.setIcon(Fontello.AT);
        loginField.setReadOnly(!userProfile.isNew());
        loginField.setImmediate(true);
        loginField.setWidth(40, Unit.EX);
        loginField.setDescription("Введите имя или e-mail пользователя который будет использоваться для входа в систему");
        loginField.setInputPrompt("имя или e-mail");
        loginField.setRequired(true);
        loginField.setRequiredError("Логин пользователя не может быть пустым. Пожалуйста введите имя или e-mail пользователя.");
        loginField.setNullRepresentation("");
        loginField.addValidator(new UserNameValidator());
        loginField.addValidator(new UserNameUniqueValidator(userProfile));
        loginField.addValueChangeListener(e -> {
            if(loginField.isValid()) {
                final String newLogin = (String) e.getProperty().getValue();
                passField.setValue(null);
                passConfField.setValue(null);
                changePasswordField.setValue(true);
                getEntity().getAliases().add(newLogin);
            }
        });
        form.addComponent(loginField);

        passField = new PasswordField("Пароль");
//        passField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        passField.setIcon(Fontello.LOCK);
        passField.setImmediate(true);
        passField.setDescription("Введите пароль для входа в систему");
        passField.setInputPrompt("Пароль");
        passField.setRequired(true);
        passField.setRequiredError("Пароль пользователя не может быть пустым.");
        passField.setNullRepresentation("");
        form.addComponent(passField);

        passConfField = new PasswordField("Подтверждение пароля");
//        passConfField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        passConfField.setIcon(Fontello.LOCK);
        passConfField.setImmediate(true);
        passConfField.setDescription("Введите повторно пароль для для его подтвержедения");
        passConfField.setInputPrompt("Подтверждение пароля");
        passConfField.setRequired(true);
        passConfField.setNullRepresentation("");
        // TODO: Сделать симметричную проверку пароля
        passConfField.addValidator(new Validator() {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(final Object value) throws InvalidValueException {
                if (!value.equals(passField.getValue()))
                    throw new InvalidValueException("Пароли не совпадают!");

            }
        });
        passConfField.setValue(initialPassword);
        form.addComponent(passConfField);

        roleField = new ComboBox("Роль");
        roleField.setDescription(
                "Роль пользователя в системе. Определяет основные права доступа к разделам и объектам системы.");
        roleField.setRequired(true);
        roleField.setNullSelectionAllowed(false);
        roleField.setNewItemsAllowed(false);
        ComponentUtil.fillSelectByEnum(roleField, UserRole.class);
        form.addComponent(roleField);

        blockedField = new CheckBox("Блокировать");
        blockedField.setDescription("Установите, чтобы блокировать вход пользователя в систему.");
        form.addComponent(blockedField);

        changePasswordField = new CheckBox("Сменить пароль");
        changePasswordField
                .setDescription("Установите, чтобы потребовать у пользователя смены пароля при следующем входе с систему.");
        form.addComponent(changePasswordField);
        return form;
    }
}
