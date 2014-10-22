/**
 *
 */
package ru.extas.web.contacts.company;

import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UrlLinkColumnGen;

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
		addMapping("www", "WWW", new UrlLinkColumnGen());
		addMapping("region", "Регион");
		addMapping("city", "Город");
		super.addDefaultMappings();
	}

}
