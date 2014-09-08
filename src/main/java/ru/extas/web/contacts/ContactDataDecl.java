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
public class ContactDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for ContactDataDecl.</p>
	 */
	public ContactDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("phone", "Телефон", PhoneConverter.class);
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("regAddress.region", "Регион");
		super.addDefaultMappings();
	}

}
