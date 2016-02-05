/**
 *
 */
package ru.extas.web.contacts.person;

import ru.extas.web.commons.EmailLinkColumnGen;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ClientDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for PersonDataDecl.</p>
	 */
	public ClientDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("phone", "Мобильный телефон", PhoneConverter.class);
		addMapping("email", "E-Mail", new EmailLinkColumnGen());
//		addMapping("registerAddress.region", "Регион");
//		addMapping("registerAddress.city", "Город");
		super.addDefaultMappings();
	}

}
