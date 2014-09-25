/**
 *
 */
package ru.extas.web.users;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.security.UserProfile;
import ru.extas.model.security.UserRole;
import ru.extas.security.UserRealm;
import ru.extas.server.security.UserRegistry;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.contacts.PersonSelect;
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
    @PropertyId("contact")
    private PersonSelect nameField;
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


    public UserEditForm(UserProfile userProfile) {
        super(userProfile.isNew() ?
                "Ввод нового пользователя в систему" :
                "Редактирование данных пользователя",
                new BeanItem<>(userProfile));
        initialPassword = userProfile.getPassword();
    }

    /** {@inheritDoc} */
    @Override
    protected void initObject(final UserProfile obj) {
        if (obj.isNew()) {
            // Инициализируем новый объект
            obj.setRole(UserRole.USER);
            obj.setChangePassword(true);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected UserProfile saveObject(UserProfile obj) {
        logger.debug("Saving user profile...");
        securePassword(obj);
        final UserRegistry userService = lookup(UserRegistry.class);
        obj = userService.save(obj);
        NotificationUtil.showSuccess("Пользователь сохранен");
        return obj;
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

    /** {@inheritDoc} */
    @Override
    protected ComponentContainer createEditFields(final UserProfile obj) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setSizeUndefined();

        // Вкладка - "Общая информация"
        final FormLayout mainTab = getMainTab(obj);
        tabsheet.addTab(mainTab).setCaption("Общие данные");

        // Вкладка - "Группы"
        final Component groupTab = createGroupTab();
        tabsheet.addTab(groupTab).setCaption("Группы");

        // Вкладка - "Права доступа"
        final Component permissionTab = createPermissionTab(obj);
        tabsheet.addTab(permissionTab).setCaption("Права доступа");

        return tabsheet;
    }

    private Component createGroupTab() {
        groupField = new UserGroupField();
        return groupField;
    }

    private Component createPermissionTab(UserProfile obj) {
        final FormLayout form = new ExtaFormLayout();

        brandsField = new MotorBrandMultiselect("Доступные бренды");
        form.addComponent(brandsField);

        regionsField = new RegionMultiselect("Доступные регионы");
        form.addComponent(regionsField);

        permissionsField = new ExtaPermissionField(obj);
        permissionsField.setCaption("Правила доступа пользователя");
        form.addComponent(permissionsField);

        return form;
    }

    private FormLayout getMainTab(UserProfile obj) {
        final FormLayout form = new ExtaFormLayout();

        // FIXME Ограничить выбор контакта только сотрудниками
        nameField = new PersonSelect("Имя");
        nameField.setImmediate(true);
        //nameField.setWidth(50, Unit.EX);
        nameField.setDescription("Введите имя (ФИО) пользователя");
        nameField.setRequired(true);
        nameField.setRequiredError("Имя пользователя не может быть пустым. Пожалуйста введите ФИО пользователя.");
        form.addComponent(nameField);

        // FIXME Проверить уникальность логина
        loginField = new TextField("Логин (e-mail)");
//        loginField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        loginField.setIcon(Fontello.AT);
        loginField.setReadOnly(!obj.isNew());
        loginField.setImmediate(true);
        loginField.setWidth(40, Unit.EX);
        loginField.setDescription("Введите имя e-mail пользователя который будет использоваться для входа в систему");
        loginField.setInputPrompt("e-mail");
        loginField.setRequired(true);
        loginField
                .setRequiredError("Логин пользователя не может быть пустым. Пожалуйста введите действительный e-mail пользователя.");
        loginField.setNullRepresentation("");
        loginField.addValidator(new EmailValidator("{0} не является допустимым адресом электронной почты."));
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
