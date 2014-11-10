/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.security.AccessRole;
import ru.extas.model.security.UserRole;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует роли доступа в соответствующее перечисление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToAccessRoleConverter extends String2EnumConverter<AccessRole> {

	/**
	 * <p>Constructor for StringToUserRoleConverter.</p>
	 */
	public StringToAccessRoleConverter() {
		super(AccessRole.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<AccessRole, String> createEnum2StringMap() {
		final HashBiMap<AccessRole, String> map = HashBiMap.create();
		map.put(AccessRole.OWNER, "Владелец");
		map.put(AccessRole.EDITOR, "Редактор");
		map.put(AccessRole.READER, "Читатель");
		return map;
	}
}
