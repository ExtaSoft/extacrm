/**
 * 
 */
package ru.extas.web;

import ru.extas.web.commons.DateToJodaDTConverter;
import ru.extas.web.commons.DateToJodaLDConverter;
import ru.extas.web.commons.StringBigdecimalConverter;
import ru.extas.web.commons.StringToJodaDTConverter;
import ru.extas.web.commons.StringToJodaLDConverter;
import ru.extas.web.contacts.StringToContactSex;
import ru.extas.web.users.StringToUserRoleConverter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.ui.UI;

/**
 * Инъекции для пользовательского интерфейса
 * 
 * @author Valery Orlov
 * 
 */
public class WebUIModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		// Конверторы дат
		bind(DateToJodaDTConverter.class).in(ServletScopes.SESSION);
		bind(DateToJodaLDConverter.class).in(ServletScopes.SESSION);
		bind(StringToJodaDTConverter.class).in(ServletScopes.SESSION);
		bind(StringToJodaLDConverter.class).in(ServletScopes.SESSION);

		// Конвертер флага
		bind(StringToBooleanConverter.class).in(ServletScopes.SESSION);

		// Конвертер денег
		bind(StringBigdecimalConverter.class).in(ServletScopes.SESSION);

		// конвертер ролей пользователя
		bind(StringToUserRoleConverter.class).in(ServletScopes.SESSION);

		// Конвертер половой принадлежности
		bind(StringToContactSex.class).in(ServletScopes.SESSION);
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

}
