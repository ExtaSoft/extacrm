/**
 *
 */
package ru.extas.web;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.ui.UI;
import ru.extas.web.commons.converters.*;
import ru.extas.web.contacts.StringToPersonPosition;
import ru.extas.web.contacts.StringToPersonSex;
import ru.extas.web.insurance.StringToA7FormConverter;
import ru.extas.web.insurance.StringToA7StatusConverter;
import ru.extas.web.insurance.StringToPolicyConverter;
import ru.extas.web.users.StringToUserRoleConverter;

import java.util.Locale;

/**
 * Инъекции для пользовательского интерфейса
 *
 * @author Valery Orlov
 */
public class WebUIModule extends AbstractModule {

    /*
     * (non-Javadoc)
     *
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {

        bind(UI.class).to(ExtaCrmUI.class);

        // Конверторы дат
        bind(DateToJodaDTConverter.class).in(ServletScopes.SESSION);
        bind(DateToJodaLDConverter.class).in(ServletScopes.SESSION);
        bind(StringToJodaDTConverter.class).in(ServletScopes.SESSION);
        bind(StringToJodaLDConverter.class).in(ServletScopes.SESSION);

        // Конвертер флага
        bind(StringToBooleanConverter.class).in(ServletScopes.SESSION);

        // Конвертер денег
        bind(StringToMoneyConverter.class).in(ServletScopes.SESSION);

        // конвертер ролей пользователя
        bind(StringToUserRoleConverter.class).in(ServletScopes.SESSION);

        // Конвертер половой принадлежности
        bind(StringToPersonSex.class).in(ServletScopes.SESSION);

        // Конвертер полиса БСО
        bind(StringToPolicyConverter.class).in(ServletScopes.SESSION);

        // Конвертер строк в длинное целое
        bind(StringToLongConverter.class).in(ServletScopes.SESSION);

        // Конвертируем Квитанцию А-7 в строку (номер квитанции)
        bind(StringToA7FormConverter.class).in(ServletScopes.SESSION);

        // Конвертер должностей
        bind(StringToPersonPosition.class).in(ServletScopes.SESSION);

        // Конвертер статусов А-7
        bind(StringToA7StatusConverter.class).in(ServletScopes.SESSION);
    }

    /**
     * Предоставляет класс пользовательского интерфейса
     *
     * @return класс пользовательского интерфейса
     */
    @Provides
    private Class<? extends UI> provideUIClass() {
        return ExtaCrmUI.class;
    }

    //    @Provides
//    private UI provideUIInstance() {
//        return new ExtaCrmUI();
//    }
//
    @Provides
    private Locale provideDefaultLocale() {
        return new Locale("ru", "RU");
    }
}
