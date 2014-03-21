/**
 *
 */
package ru.extas.web;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import ru.extas.security.ExtaDomain;
import ru.extas.server.UserManagementService;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Класс создает и управляей основным меню разделов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ExtaMainMenu extends CssLayout implements Page.UriFragmentChangedListener {

    private static final long serialVersionUID = 4672093745206168652L;
    private final Navigator navigator;
    private final Map<String, Button> fragmentToButton;

    /**
     * <p>Constructor for ExtaMainMenu.</p>
     *
     * @param ui a {@link com.vaadin.ui.UI} object.
     * @param content a {@link com.vaadin.ui.ComponentContainer} object.
     */
    public ExtaMainMenu(UI ui, ComponentContainer content) {

        // URI навигатор
        navigator = new Navigator(ui, content);

        fragmentToButton = new HashMap<>();

        addStyleName("menu");
        setHeight("100%");

	    ui.getPage().addUriFragmentChangedListener(this);
    }

	/**
	 * <p>addChapter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param desc a {@link java.lang.String} object.
	 * @param btnStyle a {@link java.lang.String} object.
	 * @param viewCls a {@link java.lang.Class} object.
	 * @param domain a {@link ru.extas.security.ExtaDomain} object.
	 */
	public void addChapter(String name, // Имя раздела
	                       String desc, // Описание раздела
	                       String btnStyle, // Стиль кнопки раздела
	                       Class<? extends View> viewCls, // Класс раздела
	                       ExtaDomain domain // Раздел
	) {
		addChapter(name, desc, btnStyle, viewCls, EnumSet.of(domain));
	}
    /**
     * Создает раздел основного меню
     *
     * @param name     Имя раздела
     * @param desc     Описание раздела
     * @param btnStyle Стиль кнопки раздела
     * @param viewCls  Класс раздела
     * @param domains  Раздел или подразделы системы
     */
    public void addChapter(String name, // Имя раздела
                           String desc, // Описание раздела
                           String btnStyle, // Стиль кнопки раздела
                           Class<? extends View> viewCls, // Класс раздела
                           Set<ExtaDomain> domains // Раздел или подразделы
    ) {
	    checkNotNull(domains);
	    checkState(!domains.isEmpty());

	    // Проверяем права доступа
	    UserManagementService userService = lookup(UserManagementService.class);
	    if (userService.isPermittedOneOf(domains)) {
		    // Фрагмент адреса
		    final String domainUrl = Iterables.getFirst(domains, null).getName();
		    String fragment = Iterables.getFirst(Splitter.on('/').split(domainUrl), domainUrl);

		    final String normFragment = fragment;

		    // Регистрируем в навигаторе
		    navigator.addView(fragment, viewCls);
		    //navigator.addProvider(new DomainViewProvider(domains, viewCls));

		    // Кнопка раздела
		    Button b = new NativeButton(name);
		    b.addStyleName(btnStyle);
		    b.setDescription(desc);
		    b.addClickListener(new ClickListener() {
			    private static final long serialVersionUID = 1L;

			    @Override
			    public void buttonClick(ClickEvent event) {
				    clearMenuSelection();
				    event.getButton().addStyleName("selected");
				    if (!navigator.getState().equals(normFragment))
					    navigator.navigateTo(normFragment);
			    }
		    });

		    // Добавляем кнопку
		    addComponent(b);
		    fragmentToButton.put(normFragment, b);
	    }
    }

    private void clearMenuSelection() {
        for (Component next : this) {
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            }
        }
    }

    /**
     * <p>processURI.</p>
     *
     * @param uriFragment a {@link java.lang.String} object.
     */
    public void processURI(String uriFragment) {
        if (uriFragment == null || uriFragment.equals(""))
            uriFragment = ExtaDomain.DASHBOARD.getName();

        if (uriFragment.startsWith("!"))
            uriFragment = uriFragment.substring(1);

	    // Берем только первую часть фрагмента
	    String firstFragment = Iterables.getFirst(Splitter.on('/').split(uriFragment), uriFragment);

        navigator.navigateTo(uriFragment);
        fragmentToButton.get(firstFragment).addStyleName("selected");

    }

	/** {@inheritDoc} */
	@Override
	public void uriFragmentChanged(final Page.UriFragmentChangedEvent event) {

		String uriFragment = event.getUriFragment();

		if (uriFragment == null || uriFragment.equals(""))
			uriFragment = ExtaDomain.DASHBOARD.getName();

		if (uriFragment.startsWith("!"))
			uriFragment = uriFragment.substring(1);

		// Берем только первую часть фрагмента
		String firstFragment = Iterables.getFirst(Splitter.on('/').split(uriFragment), uriFragment);

		clearMenuSelection();
		fragmentToButton.get(firstFragment).addStyleName("selected");
	}

	private class DomainViewProvider implements ViewProvider {

		private final Set<ExtaDomain> domains;
		private final Class<? extends View> viewCls;

		public DomainViewProvider(final Set<ExtaDomain> domains, final Class<? extends View> viewCls) {
			this.domains = domains;
			this.viewCls = viewCls;
		}

		@Override
		public String getViewName(String navigationState) {
			if (null == navigationState) {
				return null;
			}
			if (navigationState.startsWith("!"))
				navigationState = navigationState.substring(1);
			for (ExtaDomain domain:domains) {
				final String viewName = domain.getName();
				if (navigationState.equals(viewName)
						|| navigationState.startsWith(viewName + "/")
						|| viewName.startsWith(navigationState + "/")) {
					return viewName;
				}
			}
			return null;
		}

		@Override
		public View getView(final String viewName) {
			if (isOurName(viewName)) {
				try {
					View view = viewCls.newInstance();
					return view;
				} catch (InstantiationException e) {
					throw Throwables.propagate(e);
				} catch (IllegalAccessException e) {
					throw Throwables.propagate(e);
				}
			}
			return null;
		}

		private boolean isOurName(final String viewName) {
			return Iterables.tryFind(domains, new Predicate<ExtaDomain>() {
				@Override
				public boolean apply(final ExtaDomain input) {
					return input.getName().equals(viewName);
				}
			}).isPresent();
		}
	}
}
