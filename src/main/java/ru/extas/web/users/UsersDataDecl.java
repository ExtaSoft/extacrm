package ru.extas.web.users;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отрбражения пользователей
 * 
 * @author Valery Orlov
 * 
 */
public class UsersDataDecl extends GridDataDecl {

	public UsersDataDecl() {
		super();
		addMapping("name", "Имя", true, true, false);
		addMapping("login", "Логин (e-mail)", true, true, false);
		addMapping("role", "Роль", true, true, false);
		addMapping("blocked", "Блокирован", true, true, false);
		// TODO Add create/modify markers
	}

}
