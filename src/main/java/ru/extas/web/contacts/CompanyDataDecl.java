/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;

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
		addMapping("phone", "Телефон", PhoneConverter.class);
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("region", "Регион");
		super.addDefaultMappings();
	}

}
