/**
 *
 */
package ru.extas.web.contacts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.contacts.EducationKind;
import ru.extas.model.contacts.MaritalStatus;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * <p>StringToPersonPosition class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToEducationKind extends String2EnumConverter<EducationKind> {

	/**
	 * <p>Constructor for StringToPersonPosition.</p>
	 */
	public StringToEducationKind() {
		super(EducationKind.class);
	}

	/** {@inheritDoc} */
	@Override
	protected BiMap<EducationKind, String> createEnum2StringMap() {
		BiMap<EducationKind, String> map = HashBiMap.create();
		map.put(EducationKind.SUBAVERAGE, "Ниже среднего");
		map.put(EducationKind.SECONDARY, "Среднее");
		map.put(EducationKind.SECONDARY_SPECIAL, "Среднее специальное");
		map.put(EducationKind.INCOMPLETE_HIGHER, "Незаконченное высшее");
		map.put(EducationKind.HIGHER, "Высшее");
		map.put(EducationKind.INCOMPLETE_HIGHER, "Второе высшее");
		return map;
	}
}
