/**
 *
 */
package ru.extas.web;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс создает и управляей основным меню разделов
 *
 * @author Valery Orlov
 */
public class ExtaMainMenu extends CssLayout {

    private static final long serialVersionUID = 4672093745206168652L;
    private final Navigator navigator;
    private final Map<String, Button> fragmentToButton;

    /**
     * @param ui
     * @param content
     */
    public ExtaMainMenu(UI ui, ComponentContainer content) {

        // URI навигатор
        navigator = new Navigator(ui, content);

        fragmentToButton = new HashMap<>();

        addStyleName("menu");
        setHeight("100%");

    }

    /**
     * Создает раздел основного меню
     *
     * @param fragment Фрагмент адреса
     * @param name     Имя раздела
     * @param desc     Описание раздела
     * @param btnStyle Стиль кнопки раздела
     * @param viewCls  Класс раздела
     * @param reqRerm  Право доступа к разделу
     */
    public void addChapter(String fragment, // Фрагмент адреса
                           String name, // Имя раздела
                           String desc, // Описание раздела
                           String btnStyle, // Стиль кнопки раздела
                           Class<? extends View> viewCls, // Класс раздела
                           String reqRerm // Право доступа к разделу
    ) {

        final String normFragment = "/" + fragment;

        // Регистрируем в навигаторе
        navigator.addView(normFragment, viewCls);

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

    private void clearMenuSelection() {
        for (Component next : this) {
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            }
        }
    }

    /**
     * @param uriFragment
     */
    public void processURI(String uriFragment) {
        if (uriFragment == null || uriFragment.equals(""))
            uriFragment = "/";

        if (uriFragment.startsWith("!")) {
            uriFragment = uriFragment.substring(1);
        }
        navigator.navigateTo(uriFragment);
        fragmentToButton.get(uriFragment).addStyleName("selected");

    }
}
