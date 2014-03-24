package ru.extas.web.users;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.server.UserManagementService;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Конвертирует логин в имя пользователя с кешированием.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 20:52
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class LoginToUserNameConverter implements Converter<String, String> {

	private BiMap<String, String> nameCache = HashBiMap.create();

	@Inject
	private UserManagementService userService;

	/** {@inheritDoc} */
	@Override
	public String convertToModel(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {

		if (value == null)
			return null;

		return nameCache.inverse().get(value);
		//throw new UnsupportedOperationException("Convert from user name to login is unsupported");
	}

	/** {@inheritDoc} */
	@Override
	public String convertToPresentation(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
		if (value == null)
			return null;

		if (!nameCache.containsKey(value))
			fillNameCash(value);

		return nameCache.get(value);
	}

	private void fillNameCash(final String login) {
		Person userContact = userService.findUserContactByLogin(login);
		if (userContact != null)
			nameCache.put(login, userContact.getName());
	}

	/** {@inheritDoc} */
	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
