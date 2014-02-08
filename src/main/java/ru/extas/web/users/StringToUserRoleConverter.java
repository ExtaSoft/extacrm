/**
 *
 */
package ru.extas.web.users;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.UserRole;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертирует роли пользователя в соответствующее перечисление
 *
 * @author Valery Orlov
 */
@Component
public class StringToUserRoleConverter extends String2EnumConverter<UserRole> {

	public StringToUserRoleConverter() {
		super(UserRole.class);
	}

	@Override
	protected BiMap<UserRole, String> createEnum2StringMap() {
		final BiMap<UserRole, String> map = HashBiMap.create();
		map.put(UserRole.USER, "Пользователь");
		map.put(UserRole.MANAGER, "Руководитель");
		map.put(UserRole.ADMIN, "Администратор");
		return map;
	}
}
