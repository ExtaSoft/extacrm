/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanyDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for CompanyDataDecl.</p>
	 */
	public CompanyDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("phone", "Телефон");
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("actualAddress.region", "Регион");
		super.addDefaultMappings();
	}

}
