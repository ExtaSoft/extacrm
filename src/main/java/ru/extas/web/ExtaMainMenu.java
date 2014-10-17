/**
 *
 */
package ru.extas.web;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import ru.extas.ExtaException;
import ru.extas.model.security.ExtaDomain;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.ExtaUri;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;
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
    private String defaultUriFragment;

    /**
     * <p>Constructor for ExtaMainMenu.</p>
     *
     * @param ui      a {@link com.vaadin.ui.UI} object.
     * @param content a {@link com.vaadin.ui.ComponentContainer} object.
     */
    public ExtaMainMenu(final UI ui, final ComponentContainer content) {

        // URI навигатор
        navigator = new Navigator(ui, content);
        navigator.setErrorView(ErrorView.class);

        fragmentToButton = new HashMap<>();

        ui.getPage().addUriFragmentChangedListener(this);
    }

    /**
     * <p>addChapter.</p>
     *
     * @param name    a {@link String} object.
     * @param desc    a {@link String} object.
     * @param btnIcon a {@link String} object.
     * @param viewCls a {@link Class} object.
     * @param domain  a {@link ru.extas.model.security.ExtaDomain} object.
     */
    public void addChapter(final String name, // Имя раздела
                           final String desc, // Описание раздела
                           final Resource btnIcon, // Стиль кнопки раздела
                           final Class<? extends View> viewCls, // Класс раздела
                           final ExtaDomain domain // Раздел
    ) {
        addChapter(name, desc, btnIcon, viewCls, EnumSet.of(domain));
    }

    /**
     * Создает раздел основного меню
     *
     * @param name    Имя раздела
     * @param desc    Описание раздела
     * @param btnIcon Стиль кнопки раздела
     * @param viewCls Класс раздела
     * @param domains Раздел или подразделы системы
     */
    public void addChapter(final String name, // Имя раздела
                           final String desc, // Описание раздела
                           final Resource btnIcon, // Стиль кнопки раздела
                           final Class<? extends View> viewCls, // Класс раздела
                           final Set<ExtaDomain> domains // Раздел или подразделы
    ) {
        checkNotNull(domains);
        checkState(!domains.isEmpty());

        // Проверяем права доступа
        final UserManagementService userService = lookup(UserManagementService.class);
        if (userService.isPermittedOneOf(domains)) {
            // Фрагмент адреса
            final String domainUrl = Iterables.getFirst(domains, null).getName();
            final String fragment = Iterables.getFirst(Splitter.on('/').split(domainUrl), domainUrl);

            final String normFragment = fragment;

            // Регистрируем в навигаторе
            navigator.addView(fragment, viewCls);

            // Кнопка раздела
            final Button b = new Button(name);
            b.setIcon(btnIcon);
            b.setDescription(desc);
            b.setPrimaryStyleName(ExtaTheme.MENU_ITEM);
            b.addClickListener(new ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    clearMenuSelection();
                    event.getButton().addStyleName(ExtaTheme.SELECTED);
                    if (!navigator.getState().equals(normFragment))
                        navigator.navigateTo(normFragment);
                }
            });

            // Добавляем кнопку
            addComponent(b);
            fragmentToButton.put(normFragment, b);
            if (isNullOrEmpty(defaultUriFragment))
                defaultUriFragment = normFragment;
        }
    }

    private void clearMenuSelection() {
        for (final Component next : this) {
            if (next instanceof Button) {
                next.removeStyleName(ExtaTheme.SELECTED);
            }
        }
    }

    /**
     * <p>processURI.</p>
     *
     * @param uriStr a {@link java.lang.String} object.
     */
    public void processURI(final String uriStr, final boolean navigate) {
        final ExtaUri uri = new ExtaUri(uriStr);
        final String uriFragment;
        if (isNullOrEmpty(uri.getDomainPrefix())) {
            uriFragment = getDefaultUriFragment();
        } else
            uriFragment = uri.toString();

        if(isNullOrEmpty(uriFragment))
            throw new ExtaException("Не определен раздел для перехода. Возможно не заданы права доступа к разделу системы.");

        final Button selButton = fragmentToButton.get(uri.getDomainPrefix());
        if (selButton != null)
            selButton.addStyleName(ExtaTheme.SELECTED);

        if(navigate)
            navigator.navigateTo(uriFragment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uriFragmentChanged(final Page.UriFragmentChangedEvent event) {
        processURI(event.getUriFragment(), false);
    }

    public String getDefaultUriFragment() {
        return defaultUriFragment;
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
            for (final ExtaDomain domain : domains) {
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
                    final View view = viewCls.newInstance();
                    return view;
                } catch (final InstantiationException e) {
                    throw Throwables.propagate(e);
                } catch (final IllegalAccessException e) {
                    throw Throwables.propagate(e);
                }
            }
            return null;
        }

        private boolean isOurName(final String viewName) {
            return Iterables.tryFind(domains, input -> input.getName().equals(viewName)).isPresent();
        }
    }
}
