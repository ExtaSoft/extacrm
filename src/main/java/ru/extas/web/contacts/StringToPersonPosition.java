/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
	protected HashBiMap<Person.Position, String> createEnum2StringMap() {
        HashBiMap<Person.Position, String> map = HashBiMap.create();
		map.put(Person.Position.EMPLOYEE, "Сотрудник");
		map.put(Person.Position.DIRECTOR, "Генеральный директор");
		map.put(Person.Position.ACCOUNTANT, "Главный бухгалтер");
		return map;
	}
}
