/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.Person;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * @author Valery Orlov
 */
@Component
public class StringToPersonPosition extends String2EnumConverter<Person.Position> {

	public StringToPersonPosition() {
		super(Person.Position.class);
	}

	@Override
	protected BiMap<Person.Position, String> createEnum2StringMap() {
		BiMap<Person.Position, String> map = HashBiMap.create();
		map.put(Person.Position.EMPLOYEE, "Сотрудник");
		map.put(Person.Position.DIRECTOR, "Генеральный директор");
		map.put(Person.Position.ACCOUNTANT, "Главный бухгалтер");
		return map;
	}
}
