package ru.extas.web;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.extas.model.users.UserProfile;
import ru.extas.security.ExtaDomain;
import ru.extas.server.UserManagementService;
import ru.extas.web.config.ConfigView;
import ru.extas.web.contacts.ContactsView;
import ru.extas.web.dashboard.HomeView;
import ru.extas.web.insurance.InsuranceView;
import ru.extas.web.lead.LeadsView;
import ru.extas.web.product.ProductView;
import ru.extas.web.sale.SalesView;
import ru.extas.web.tasks.TasksView;
import ru.extas.web.users.ChangePasswordForm;
import ru.extas.web.users.LoginToUserNameConverter;
import ru.extas.web.users.UsersView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Основной класс приложения
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
@Scope("session")
@Theme("extacrm")
@Title("Extreme Assistance CRM")
public class ExtaCrmUI extends UI {

	private final static Logger logger = LoggerFactory.getLogger(ExtaCrmUI.class);

	private static final long serialVersionUID = -6733655391417975375L;

	private final CssLayout root = new CssLayout();

	private VerticalLayout loginLayout;

	private ExtaMainMenu mainMenu;
	private final CssLayout content = new CssLayout();

	/** {@inheritDoc} */
	@Override
	protected void init(final VaadinRequest request) {


		// Регистрируем конверторы по умолчанию

		VaadinSession.getCurrent().setConverterFactory(new ExtaConverterFactory());

		// Устанавливаем локаль
		Locale uiLocale = lookup(Locale.class);
		VaadinSession.getCurrent().setLocale(uiLocale);

		setContent(root);
		root.addStyleName("root");
		root.setSizeFull();

		// Configure the error handler for the UI
		setErrorHandler(new DefaultErrorHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void error(final com.vaadin.server.ErrorEvent event) {
				// Протоколируем ошибку
				logger.error("", event.getThrowable());

				final StringWriter strWr = new StringWriter();
				strWr.append("<div class='exceptionStackTraceBox'><pre>");
				event.getThrowable().printStackTrace(new PrintWriter(strWr, true));
				strWr.append("</pre></div>");

				// Display the error message in a custom fashion
				final Notification notif = new Notification("Непредусмотренная ошибка", strWr.toString(),
						Notification.Type.ERROR_MESSAGE);

				// Customize it
				notif.setPosition(Position.MIDDLE_CENTER);
				notif.setHtmlContentAllowed(true);

				// Show it in the page
				notif.show(Page.getCurrent());
			}
		});

		if (lookup(UserManagementService.class).isUserAuthenticated())
			buildMainView();
		else
			buildLoginView(false);

	}

	private void buildLoginView(final boolean exit) {
		if (exit) {
			root.removeAllComponents();
		}


		// Unfortunate to use an actual widget here, but since CSS generated
		// elements can't be transitioned yet, we must
		final Label bg = new Label();
		bg.setSizeUndefined();
		bg.addStyleName("login-bg");
		root.addComponent(bg);

		addStyleName("login");

		loginLayout = new VerticalLayout();
		loginLayout.setSizeFull();
		loginLayout.addStyleName("login-layout");
		root.addComponent(loginLayout);

		final CssLayout loginPanel = new CssLayout();
		loginPanel.addStyleName("login-panel");

		final HorizontalLayout labels = new HorizontalLayout();
		labels.setWidth("100%");
		labels.setMargin(true);
		labels.addStyleName("labels");
		loginPanel.addComponent(labels);

		final Label welcome = new Label("Добро пожаловать");
		welcome.setSizeUndefined();
		welcome.addStyleName("h4");
		labels.addComponent(welcome);
		labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

		final Label title = new Label("Экстрим Ассистанс CRM");
		title.setSizeUndefined();
		title.addStyleName("h2");
		title.addStyleName("light");
		labels.addComponent(title);
		labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

		final HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.setMargin(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Пользователь");
		username.focus();
		fields.addComponent(username);

		final PasswordField password = new PasswordField("Пароль");
		fields.addComponent(password);

		final Button signin = new Button("Войти");
		signin.addStyleName("default");
		fields.addComponent(signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		final ShortcutListener enter = new ShortcutListener("Войти", KeyCode.ENTER, null) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(final Object sender, final Object target) {
				signin.click();
			}
		};

		signin.addClickListener(new ClickListener() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UserManagementService userService = lookup(UserManagementService.class);

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
							final ChangePasswordForm form = new ChangePasswordForm(new BeanItem<>(
									currentUserProfile));
							form.addCloseListener(new CloseListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void windowClose(final CloseEvent e) {
									if (form.isSaved()) {
										closeLoginAndBuildMain();
									} else {
										lookup(UserManagementService.class).logout();
									}
								}
							});
							form.showModal();
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
					if (loginPanel.getComponentCount() > 2) {
						// Remove the previous error message
						loginPanel.removeComponent(loginPanel.getComponent(2));
					}
					// Add new error message
					final Label error = new Label(
							errMessage
									+ " <br/><span>Проверьте правильность пары пользователь/пароль или обратитесь к администратору</span>",
							ContentMode.HTML);
					error.addStyleName("error");
					error.setSizeUndefined();
					error.addStyleName("light");
					// Add animation
					error.addStyleName("v-animate-reveal");
					loginPanel.addComponent(error);
					username.focus();
				}
			}

			private void closeLoginAndBuildMain() {
				signin.removeShortcutListener(enter);
				removeStyleName("login");
				root.removeComponent(loginLayout);
				buildMainView();
			}
		});

		signin.addShortcutListener(enter);

		loginPanel.addComponent(fields);

		loginLayout.addComponent(loginPanel);
		loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private void buildMainView() {

		logger.info("Entering main view");
		root.addComponent(new HorizontalLayout() {
			private static final long serialVersionUID = 1L;

			{
				setSizeFull();
				addStyleName("main-view");
				addComponent(new VerticalLayout() {
					private static final long serialVersionUID = 1L;

					// Sidebar
					{
						addStyleName("sidebar");
						setWidth(null);
						setHeight("100%");

						// Branding element
						addComponent(new CssLayout() {
							{
								String appVersion = lookup("application.version", String.class);
								String appBuildTm = lookup("application.build.timestamp", String.class);
								addStyleName("branding");
								final String brandText = "<span>Экстрим Ассистанс</span> CRM";
								final Label logo = new Label(brandText, ContentMode.HTML);
								logo.setSizeUndefined();
								final String logoDesc = MessageFormat.format("Версия {0}, собрано {1}", appVersion, appBuildTm);
								logo.setDescription(logoDesc);
								addComponent(logo);
								// addComponent(new Image(null, new
								// ThemeResource(
								// "img/branding.png")));
							}
						});

						// Main menu
						mainMenu = new ExtaMainMenu(ExtaCrmUI.this, content);
						addComponent(mainMenu);
						setExpandRatio(mainMenu, 1);

						// User menu
						addComponent(new VerticalLayout() {
							private static final long serialVersionUID = 1L;

							{
								setSizeUndefined();
								addStyleName("user");
								final Image profilePic = new Image(null, new ThemeResource("img/profile-pic.png"));
								profilePic.setWidth("34px");
								addComponent(profilePic);
								String login = lookup(UserManagementService.class).getCurrentUserLogin();
								final Label userName = new Label(new ObjectProperty(login));
								userName.setSizeUndefined();
								userName.setConverter(lookup(LoginToUserNameConverter.class));
								addComponent(userName);

								final Command cmd = new Command() {
									private static final long serialVersionUID = 1L;

									@Override
									public void menuSelected(final MenuItem selectedItem) {
										Notification.show("Не реализовано пока");
									}
								};
								final MenuBar settings = new MenuBar();
								final MenuItem settingsMenu = settings.addItem("", null);
								settingsMenu.setStyleName("icon-cog");
								settingsMenu.addItem("Настройки", cmd);
								settingsMenu.addSeparator();
								settingsMenu.addItem("Профиль", cmd);
								addComponent(settings);

								final Button exit = new NativeButton("Выход");
								exit.addStyleName("icon-cancel");
								exit.setDescription("Выход из системы");
								addComponent(exit);
								exit.addClickListener(new ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(final ClickEvent event) {
										lookup(UserManagementService.class).logout();
										// Redirect to avoid keeping the removed
										// UI open in the browser
										// getUI().getPage().setLocation("");
										// Close the VaadinServiceSession
										getUI().getSession().close();
										// TODO: Исправить ошибку соединения после выхода
									}
								});
							}
						});
					}
				});
				// Content
				addComponent(content);
				content.setSizeFull();
				content.addStyleName("view-content");
				setExpandRatio(content, 1);
			}

		});

		// -------------------------------------------------------------
		// Создаем кнопки основного меню
		mainMenu.addChapter("Начало", "Начальный экран приложения", "icon-home",
				HomeView.class, ExtaDomain.DASHBOARD);
		mainMenu.addChapter("Задачи", "Мои задачи", "icon-check",
				TasksView.class, EnumSet.of(ExtaDomain.TASKS_TODAY, ExtaDomain.TASKS_WEEK, ExtaDomain.TASKS_MONTH, ExtaDomain.TASKS_ALL));
		mainMenu.addChapter("Контакты", "Клиенты, контрагенты и сотрудники", "icon-contacts",
				ContactsView.class, EnumSet.of(ExtaDomain.PERSON, ExtaDomain.COMPANY, ExtaDomain.LEGAL_ENTITY, ExtaDomain.SALE_POINT));
		mainMenu.addChapter("Лиды", "Входящие лиды", "icon-inbox-alt",
				LeadsView.class, EnumSet.of(ExtaDomain.LEADS_NEW, ExtaDomain.LEADS_QUAL, ExtaDomain.LEADS_CLOSED));
		mainMenu.addChapter("Продажи", "Раздел управления продажами", "icon-dollar",
				SalesView.class, EnumSet.of(ExtaDomain.SALES_OPENED, ExtaDomain.SALES_SUCCESSFUL, ExtaDomain.SALES_CANCELED));
		mainMenu.addChapter("Страхование", "Раздел посвященный страхованию", "icon-umbrella-1",
				InsuranceView.class, EnumSet.of(ExtaDomain.INSURANCE_PROP, ExtaDomain.INSURANCE_BSO, ExtaDomain.INSURANCE_A_7, ExtaDomain.INSURANCE_TRANSFER));
		mainMenu.addChapter("Продукты", "Раздел посвященный предоставляемым продуктам (услугам)", "icon-basket",
				ProductView.class, EnumSet.of(ExtaDomain.PROD_CREDIT, ExtaDomain.PROD_INSURANCE, ExtaDomain.PROD_INSTALL));
		mainMenu.addChapter("Пользователи", "Управление ползователями и правами доступа", "icon-users-3",
				UsersView.class, ExtaDomain.USERS);
		mainMenu.addChapter("Настройки", "Настройки приложения и пользовательского интерфейса", "icon-cog-alt",
				ConfigView.class, ExtaDomain.SETTINGS);

		mainMenu.processURI(Page.getCurrent().getUriFragment());

	}

}
