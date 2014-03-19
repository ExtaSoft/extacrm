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
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToPersonPosition extends String2EnumConverter<Person.Position> {

	/**
	 * <p>Constructor for StringToPersonPosition.</p>
	 */
	public StringToPersonPosition() {
		super(Person.Position.class);
	}

	/** {@inheritDoc} */
	@Override
	protected BiMap<Person.Position, String> createEnum2StringMap() {
		BiMap<Person.Position, String> map = HashBiMap.create();
		map.put(Person.Position.EMPLOYEE, "Сотрудник");
		map.put(Person.Position.DIRECTOR, "Генеральный директор");
		map.put(Person.Position.ACCOUNTANT, "Главный бухгалтер");
		return map;
	}
}
