package ru.extas.web;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.UserProfile;
import ru.extas.server.security.UserManagementService;
import ru.extas.server.settings.UserSettingsService;
import ru.extas.web.analytics.AnalyticsView;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.contacts.ContactsView;
import ru.extas.web.dashboard.HomeView;
import ru.extas.web.insurance.InsuranceView;
import ru.extas.web.motor.MotorView;
import ru.extas.web.product.ProductView;
import ru.extas.web.sale.SalesView;
import ru.extas.web.settings.SettingsView;
import ru.extas.web.tasks.TasksView;
import ru.extas.web.users.ChangePasswordForm;
import ru.extas.web.users.LoginToUserNameConverter;
import ru.extas.web.users.UsersView;

import java.text.MessageFormat;
import java.util.EnumSet;

import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.UiUtils.initUi;

/**
 * Основной класс приложения
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope("session")
@Theme(ExtaTheme.NAME)
@Title("Extreme Assistance CRM")
@StyleSheet({
        "vaadin://addons/jslider/css/jslider.blue.css",
        "vaadin://addons/jslider/css/jslider.plastic.css",
        "vaadin://addons/jslider/css/jslider.round.plastic.css",
        "vaadin://addons/jslider/css/jslider.css",
        "vaadin://addons/jslider/css/jslider.round.css"})
public class ExtaCrmUI extends UI {

    private final static Logger logger = LoggerFactory.getLogger(ExtaCrmUI.class);

    private static final long serialVersionUID = -6733655391417975375L;
    private static final int POLLING_INTERVAL = 3000;

    final RootLayout root = new RootLayout();
    final ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();

    private ExtaMainMenu mainMenu;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init(final VaadinRequest request) {

        initUi(this);

        setPollInterval(POLLING_INTERVAL);

        if (lookup(UserManagementService.class).isUserAuthenticated())
            buildMainView();
        else
            buildLoginView(false);

        getPage().setTitle(lookup(UserSettingsService.class).getAppTitle());

    }

    private void buildLoginView(final boolean exit) {

        final Panel loginPanel = new Panel(lookup(UserSettingsService.class).getAppTitle());
        loginPanel.setSizeUndefined();

        final TextField username = new TextField("Пользователь");
//        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        username.setIcon(Fontello.AT);
        username.setColumns(20);
        username.focus();

        final PasswordField password = new PasswordField("Пароль");
//        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        password.setIcon(Fontello.LOCK);
        password.setColumns(20);

        final Button signin = new Button("Войти");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);

        final FormLayout loginForm = new FormLayout(username, password, signin);
        loginForm.setMargin(true);
        loginForm.setSizeUndefined();
        loginForm.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener("Войти", KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                final UserManagementService userService = lookup(UserManagementService.class);

                final String user = username.getValue();
                final String pass = password.getValue();
                String errMessage = "";
                if (user != null && pass != null) {
                    try {
                        userService.authenticate(user, pass);

                        // Получить данные текущего пользователя
                        final UserProfile currentUserProfile = userService.getCurrentUser();
                        if (currentUserProfile.isChangePassword()) {
                            // Поменять пароль
                            final ChangePasswordForm form = new ChangePasswordForm(currentUserProfile);
                            form.addCloseFormListener(event1 -> {
                                if (form.isSaved()) {
                                    closeLoginAndBuildMain();
                                } else {
                                    lookup(UserManagementService.class).logout();
                                }
                            });
                            FormUtils.showModalWin(form);
                        } else {
                            closeLoginAndBuildMain();
                        }
                    } catch (final UnknownAccountException uae) {
                        // username wasn't in the system, show them an error
                        // message?
                        errMessage = "Пользователь не найден";
                    } catch (final IncorrectCredentialsException ice) {
                        // password didn't match, try again?
                        errMessage = "Неверный пароль";
                    } catch (final LockedAccountException lae) {
                        // account for that username is locked - can't login.
                        // Show them a message?
                        errMessage = "Пользователь заблокирован";
                    } catch (final AuthenticationException ae) {
                        // unexpected condition - error?
                        errMessage = "Вход в систему невозможен";
                        logger.error("Authentication system error", ae);
                    }
                } else {
                    errMessage = "Задайте имя пользователя и пароль";
                }
                if (!userService.isUserAuthenticated()) {
                    // Show new error message
                    final String message = MessageFormat.format(
                            "{0} <br/><span>Проверьте правильность пары пользователь/пароль или обратитесь к администратору</span>", errMessage);
                    NotificationUtil.showError("Ошибка при проверке пользователя", message);
                    username.focus();
                }
            }

            private void closeLoginAndBuildMain() {
                signin.removeShortcutListener(enter);
                buildMainView();
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.setContent(loginForm);

        final VerticalLayout loginRoot = new VerticalLayout();
        loginRoot.setSizeFull();
        loginRoot.setPrimaryStyleName(ExtaTheme.LOGIN_VIEW);
        loginRoot.addComponent(loginPanel);
        loginRoot.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
        setContent(loginRoot);
    }

    private void buildMainView() {

        logger.info("Entering main view");
        // Branding element
        final CssLayout branding = new CssLayout() {
            {
                final String appVersion = lookup("application.version", String.class);
                final String appBuildTm = lookup("application.build.timestamp", String.class);
                final String brandText = MessageFormat.format("<strong>{1}</strong><br/><i>Версия {0}</i>",
                        appVersion,
                        lookup(UserSettingsService.class).getAppTitle());
                final Label logo = new Label(brandText, ContentMode.HTML);
                final String logoDesc = MessageFormat.format("Версия {0}, собрано {1}", appVersion, appBuildTm);
                logo.setDescription(logoDesc);
                addComponent(logo);
            }
        };
        root.addBranding(branding);

        // Main menu
        mainMenu = new ExtaMainMenu(ExtaCrmUI.this, viewDisplay);
        root.addMenu(mainMenu);

        // User menu
        final VerticalLayout userMenu = new VerticalLayout();
        userMenu.setSizeUndefined();
        final Button profilePic = new Button(FontAwesome.USER);
        profilePic.addStyleName(ValoTheme.BUTTON_LINK);
        profilePic.addStyleName(ExtaTheme.AVATAR);
        final String login = lookup(UserManagementService.class).getCurrentUserLogin();
        final Label userName = new Label(new ObjectProperty(login));
        userName.setSizeUndefined();
        userName.setConverter(lookup(LoginToUserNameConverter.class));
        final HorizontalLayout userData = new HorizontalLayout(profilePic, userName);
        //userData.setSizeUndefined();
        userMenu.addComponent(userData);

        final Button exit = new Button("Выход", FontAwesome.SIGN_OUT);
        exit.setDescription("Выход из системы");
        exit.addStyleName(ValoTheme.BUTTON_LINK);
        exit.addClickListener(event -> {
            lookup(UserManagementService.class).logout();
            // Close the VaadinServiceSession
            getUI().getSession().close();
            // Redirect to avoid keeping the removed
            // UI open in the browser
            getUI().getPage().setLocation("/");
        });
        userMenu.addComponent(exit);

        root.addUserBadge(userMenu);

        // -------------------------------------------------------------
        // Создаем кнопки основного меню
        mainMenu.addChapter("Рабочий стол", "Начальный экран приложения", Fontello.HOME,
                HomeView.class, ExtaDomain.DASHBOARD);
        mainMenu.addChapter("Задачи", "Мои задачи", Fontello.CHECK,
                TasksView.class, EnumSet.of(ExtaDomain.TASKS_TODAY, ExtaDomain.TASKS_WEEK, ExtaDomain.TASKS_MONTH, ExtaDomain.TASKS_ALL));
        mainMenu.addChapter("Контакты", "Клиенты, контрагенты и сотрудники", Fontello.CONTACTS,
                ContactsView.class, EnumSet.of(ExtaDomain.PERSON, ExtaDomain.COMPANY, ExtaDomain.LEGAL_ENTITY, ExtaDomain.SALE_POINT, ExtaDomain.EMPLOYEE));
        mainMenu.addChapter("Продажи", "Раздел управления продажами", Fontello.DOLLAR,
                SalesView.class, EnumSet.of(ExtaDomain.SALES_LEADS, ExtaDomain.SALES_OPENED, ExtaDomain.SALES_SUCCESSFUL, ExtaDomain.SALES_CANCELED));
        mainMenu.addChapter("Страхование", "Раздел посвященный страхованию", Fontello.UMBRELLA_1,
                InsuranceView.class, EnumSet.of(ExtaDomain.INSURANCE_PROP, ExtaDomain.INSURANCE_BSO, ExtaDomain.INSURANCE_A_7, ExtaDomain.INSURANCE_TRANSFER));
        mainMenu.addChapter("Аналитика", "Раздел аналитической и статистической информации", FontAwesome.BAR_CHART_O,
                AnalyticsView.class, ExtaDomain.ANALYTICS);
        mainMenu.addChapter("Продукты", "Раздел посвященный предоставляемым продуктам (услугам)", Fontello.BASKET,
                ProductView.class, EnumSet.of(ExtaDomain.PROD_CREDIT, ExtaDomain.PROD_INSURANCE, ExtaDomain.PROD_INSTALL));
        mainMenu.addChapter("Техника", "Раздел посвященный информации о технике", Fontello.COG,
                MotorView.class, EnumSet.of(ExtaDomain.MOTOR_MODEL, ExtaDomain.MOTOR_BRAND, ExtaDomain.MOTOR_TYPE));
        mainMenu.addChapter("Пользователи", "Управление ползователями и правами доступа", Fontello.USERS_3,
                UsersView.class, EnumSet.of(ExtaDomain.USERS, ExtaDomain.USER_GROUPS, ExtaDomain.CURATORS_GROUPS));
        mainMenu.addChapter("Настройки", "Настройки приложения и пользовательского интерфейса", Fontello.COG_ALT,
                SettingsView.class, ExtaDomain.SETTINGS);

        mainMenu.processURI(null, true);

        setContent(root);
    }

}
