/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.EnumSet;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntityDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for LegalEntityDataDecl.</p>
	 */
	public LegalEntityDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("company.name", "Компания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("phone", "Телефон", PhoneConverter.class);
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("actualAddress.region", "Регион");
		super.addDefaultMappings();
	}

}
