/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package ru.extas.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Основной класс приложения
 * 
 * @author Valery Orlov
 * 
 */
@Theme("extacrm")
@Title("Extrime Assistance CRM")
public class ExtaCrmUI extends UI {

	// Описание раздела
	// - фрагмент адреса
	// - класс вью
	// - право доступа
	// - имя раздела
	// - описатие раздела
	// - иконка
	
	private static final long serialVersionUID = 1L;

	CssLayout root = new CssLayout();

	VerticalLayout loginLayout;

	CssLayout menu = new CssLayout();
	CssLayout content = new CssLayout();

	HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>() {
		{
			put("/insurance", InsuranceView.class);
		}
	};

	HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();

	private Navigator nav;

	@Override
	protected void init(VaadinRequest request) {

		setContent(root);
		root.addStyleName("root");
		root.setSizeFull();

		// Configure the error handler for the UI
		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(final com.vaadin.server.ErrorEvent event) {
				event.getThrowable().printStackTrace();

				final StringWriter strWr = new StringWriter();
				strWr.append("<div class='exceptionStackTraceBox'><pre>");
				event.getThrowable().printStackTrace(new PrintWriter(strWr, true));
				strWr.append("</pre></div>");

				// Display the error message in a custom fashion
				final Notification notif = new Notification("Uncaught Exception", strWr.toString(), Notification.Type.ERROR_MESSAGE);

				// Customize it
				notif.setPosition(Position.MIDDLE_CENTER);
				notif.setHtmlContentAllowed(true);

				// Show it in the page
				notif.show(Page.getCurrent());
			}
		});

		// TODO: Move to injection
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated())
			buildMainView();
		else
			buildLoginView(false);

	}

	private void buildLoginView(boolean exit) {
		if (exit) {
			root.removeAllComponents();
		}

		// Unfortunate to use an actual widget here, but since CSS generated
		// elements can't be transitioned yet, we must
		Label bg = new Label();
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

		HorizontalLayout labels = new HorizontalLayout();
		labels.setWidth("100%");
		labels.setMargin(true);
		labels.addStyleName("labels");
		loginPanel.addComponent(labels);

		Label welcome = new Label("Добро пожаловать");
		welcome.setSizeUndefined();
		welcome.addStyleName("h4");
		labels.addComponent(welcome);
		labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

		Label title = new Label("Экстрим Ассистанс CRM");
		title.setSizeUndefined();
		title.addStyleName("h2");
		title.addStyleName("light");
		labels.addComponent(title);
		labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

		HorizontalLayout fields = new HorizontalLayout();
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
			@Override
			public void handleAction(Object sender, Object target) {
				signin.click();
			}
		};

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String errMessage = "";
				final String user = username.getValue();
				final String pass = password.getValue();
				// TODO: Move to injection
				Subject currentUser = SecurityUtils.getSubject();
				if (user != null && pass != null) {
					UsernamePasswordToken token = new UsernamePasswordToken(user, pass);
					// token.setRememberMe(true);
					try {
						currentUser.login(token);
						signin.removeShortcutListener(enter);
						removeStyleName("login");
						root.removeComponent(loginLayout);
						buildMainView();
					} catch (UnknownAccountException uae) {
						// username wasn't in the system, show them an error
						// message?
						errMessage = "Пользователь не найден";
					} catch (IncorrectCredentialsException ice) {
						// password didn't match, try again?
						errMessage = "Неверный пароль";
					} catch (LockedAccountException lae) {
						// account for that username is locked - can't login.
						// Show them a message?
						errMessage = "Пользователь заблокирован";
					} catch (AuthenticationException ae) {
						// unexpected condition - error?
						errMessage = "Вход в систему невозможен";
					}
				} else {
					errMessage = "Задайте имя пользователя и пароль";
				}
				if (!currentUser.isAuthenticated()) {
					if (loginPanel.getComponentCount() > 2) {
						// Remove the previous error message
						loginPanel.removeComponent(loginPanel.getComponent(2));
					}
					// Add new error message
					Label error = new Label(errMessage
							+ " <br/><span>Проверьте правильность пары пользователь/пароль или обратитесь к администратору</span>", ContentMode.HTML);
					error.addStyleName("error");
					error.setSizeUndefined();
					error.addStyleName("light");
					// Add animation
					error.addStyleName("v-animate-reveal");
					loginPanel.addComponent(error);
					username.focus();
				}
			}
		});

		signin.addShortcutListener(enter);

		loginPanel.addComponent(fields);

		loginLayout.addComponent(loginPanel);
		loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private void buildMainView() {

		nav = new Navigator(this, content);

		for (String route : routes.keySet()) {
			nav.addView(route, routes.get(route));
		}

		root.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				addStyleName("main-view");
				addComponent(new VerticalLayout() {
					// Sidebar
					{
						addStyleName("sidebar");
						setWidth(null);
						setHeight("100%");

						// Branding element
						addComponent(new CssLayout() {
							{
								addStyleName("branding");
								Label logo = new Label("<span>Экстрим Ассистанс</span> CRM", ContentMode.HTML);
								logo.setSizeUndefined();
								addComponent(logo);
								// addComponent(new Image(null, new
								// ThemeResource(
								// "img/branding.png")));
							}
						});

						// Main menu
						addComponent(menu);
						setExpandRatio(menu, 1);

						// User menu
						addComponent(new VerticalLayout() {
							{
								setSizeUndefined();
								addStyleName("user");
								Image profilePic = new Image(null, new ThemeResource("img/profile-pic.png"));
								profilePic.setWidth("34px");
								addComponent(profilePic);
								Label userName = new Label("UserName");
								userName.setSizeUndefined();
								addComponent(userName);

								Command cmd = new Command() {
									@Override
									public void menuSelected(MenuItem selectedItem) {
										Notification.show("Не реализовано пока");
									}
								};
								MenuBar settings = new MenuBar();
								MenuItem settingsMenu = settings.addItem("", null);
								settingsMenu.setStyleName("icon-cog");
								settingsMenu.addItem("Настройки", cmd);
								settingsMenu.addSeparator();
								settingsMenu.addItem("Профиль", cmd);
								addComponent(settings);

								Button exit = new NativeButton("Выход");
								exit.addStyleName("icon-cancel");
								exit.setDescription("Выход из системы");
								addComponent(exit);
								exit.addClickListener(new ClickListener() {
									@Override
									public void buttonClick(ClickEvent event) {
										buildLoginView(true);
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

		menu.removeAllComponents();

		for (final String view : new String[] { "insurance"/*
															 * , "sales",
															 * "transactions",
															 * "reports",
															 * "schedule"
															 */}) {
			Button b = new NativeButton(view.substring(0, 1).toUpperCase() + view.substring(1).replace('-', ' '));
			b.addStyleName("icon-" + view);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					clearMenuSelection();
					event.getButton().addStyleName("selected");
					if (!nav.getState().equals("/" + view))
						nav.navigateTo("/" + view);
				}
			});

			menu.addComponent(b);

			viewNameToMenuButton.put("/" + view, b);
		}
		menu.addStyleName("menu");
		menu.setHeight("100%");

		String f = Page.getCurrent().getUriFragment();
		if (f != null && f.startsWith("!")) {
			f = f.substring(1);
		}
		if (f == null || f.equals("") || f.equals("/")) {
			nav.navigateTo("/insurance");
			menu.getComponent(0).addStyleName("selected");
		} else {
			nav.navigateTo(f);
			viewNameToMenuButton.get(f).addStyleName("selected");
		}

		nav.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
			}
		});

	}

	private void clearMenuSelection() {
		for (Iterator<Component> it = menu.iterator(); it.hasNext();) {
			Component next = it.next();
			if (next instanceof NativeButton) {
				next.removeStyleName("selected");
			} else if (next instanceof DragAndDropWrapper) {
				// Wow, this is ugly (even uglier than the rest of the code)
				((DragAndDropWrapper) next).iterator().next().removeStyleName("selected");
			}
		}
	}

}
