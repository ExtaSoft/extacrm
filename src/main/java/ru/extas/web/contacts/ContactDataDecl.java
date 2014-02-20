/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 */
public class ContactDataDecl extends GridDataDecl {

	public ContactDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("phone", "Телефон");
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("actualAddress.region", "Регион");
		super.addCreateModifyMarkers();
	}

}
