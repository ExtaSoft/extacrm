/**
 * 
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения контактов в списке
 * 
 * @author Valery Orlov
 * 
 */
public class ContactDataDecl extends GridDataDecl {

	public ContactDataDecl() {
		super();
		addMapping("name", "Имя", true, true, false);
		addMapping("birthday", "Дата рождения", true, true, false);
		addMapping("sex", "Пол", true, true, false);
		addMapping("cellPhone", "Мобильный телефон", true, true, false);
		addMapping("email", "E-Mail", true, true, false);
		addMapping("region", "Регион", true, true, false);
		// TODO Add create/modify markers
	}

}
