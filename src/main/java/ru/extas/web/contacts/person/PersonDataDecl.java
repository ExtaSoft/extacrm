/**
 *
 */
package ru.extas.web.contacts.person;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.EmailLinkColumnGen;
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
public class PersonDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for PersonDataDecl.</p>
	 */
	public PersonDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("birthday", "Дата рождения"/* , StringToJodaLDConverter.class */);
		addMapping("sex", "Пол");
		addMapping("phone", "Мобильный телефон", PhoneConverter.class);
		addMapping("secondPhone", "Доп. мобильный телефон", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), PhoneConverter.class);
		addMapping("email", "E-Mail", new EmailLinkColumnGen());
		addMapping("regAddress.region", "Регион");
		super.addDefaultMappings();
	}

}
