package ru.extas.web.users;

import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.UserProfile;
import ru.extas.server.UserManagementService;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Конвертирует логин в имя пользователя с кешированием.
 *
 * @author Valery Orlov
 *         Date: 05.02.14
 *         Time: 20:52
 */
@Component
public class LoginToUserProfileConverter implements Converter<UserProfile, String> {

	@Inject
	private UserManagementService userService;

	@Override
	public String convertToModel(final UserProfile value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {

		if (value == null)
			return null;

		return value.getLogin();
		//throw new UnsupportedOperationException("Convert from user name to login is unsupported");
	}

	@Override
	public UserProfile convertToPresentation(final String value, final Class<? extends UserProfile> targetType, final Locale locale) throws ConversionException {
		if (value == null)
			return null;

		return userService.findUserByLogin(value);
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<UserProfile> getPresentationType() {
		return UserProfile.class;
	}
}
