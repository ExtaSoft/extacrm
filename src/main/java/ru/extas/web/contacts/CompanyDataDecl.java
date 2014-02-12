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
public class CompanyDataDecl extends GridDataDecl {

	public CompanyDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("phone", "Телефон");
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		super.addCreateModifyMarkers();
	}

}
