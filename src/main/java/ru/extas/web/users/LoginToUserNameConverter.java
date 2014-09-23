package ru.extas.web.users;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.vaadin.data.util.converter.Converter;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.server.security.UserManagementService;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

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

    private final Map<String, String> nameCache = newHashMap();

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToModel(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {

        if (value == null)
            return null;

        for (Map.Entry<String, String> entry : nameCache.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }

        return null;// nameCache.inverse().get(value);
        //throw new UnsupportedOperationException("Convert from user name to login is unsupported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToPresentation(final String value, final Class<? extends String> targetType, final Locale locale) throws ConversionException {
        if (value == null)
            return null;

        if (!nameCache.containsKey(value))
            fillNameCash(value);

        return nameCache.get(value);
    }

    private void fillNameCash(final String login) {
        Person userContact = lookup(UserManagementService.class).findUserContactByLogin(login);
        if (userContact != null) {
            final String name = userContact.getName();
            final List<String> nameParts = Splitter.on(' ')
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(name);
            String shortName;
            if (nameParts.size() > 1)
                shortName = Joiner.on(' ').join(nameParts.get(0), nameParts.get(1));
            else
                shortName = name;
            nameCache.put(login, shortName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
