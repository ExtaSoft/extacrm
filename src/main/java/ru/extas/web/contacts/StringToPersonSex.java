/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.Person;
import ru.extas.model.Person.Sex;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonSex class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
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
	protected BiMap<Sex, String> createEnum2StringMap() {
		BiMap<Sex, String> map = HashBiMap.create();
		map.put(Person.Sex.MALE, "Мужской");
		map.put(Person.Sex.FEMALE, "Женский");
		return map;
	}
}
