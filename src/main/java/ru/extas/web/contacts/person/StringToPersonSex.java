/**
 *
 */
package ru.extas.web.contacts.person;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person.Sex;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonSex class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToPersonSex extends String2EnumConverter<Person.Sex> {

	/**
	 * <p>Constructor for StringToPersonSex.</p>
	 */
	public StringToPersonSex() {
		super(Person.Sex.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<Sex, String> createEnum2StringMap() {
        final HashBiMap<Sex, String> map = HashBiMap.create();
		map.put(Person.Sex.MALE, "Мужской");
		map.put(Person.Sex.FEMALE, "Женский");
		return map;
	}
}
