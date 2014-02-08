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
 * @author Valery Orlov
 */
@Component
public class StringToPersonSex extends String2EnumConverter<Person.Sex> {

	public StringToPersonSex() {
		super(Person.Sex.class);
	}

	@Override
	protected BiMap<Sex, String> createEnum2StringMap() {
		BiMap<Sex, String> map = HashBiMap.create();
		map.put(Person.Sex.MALE, "Мужской");
		map.put(Person.Sex.FEMALE, "Женский");
		return map;
	}
}
